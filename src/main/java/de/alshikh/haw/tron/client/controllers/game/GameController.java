package de.alshikh.haw.tron.client.controllers.game;

import de.alshikh.haw.tron.client.controllers.game.helpers.GameUpdater;
import de.alshikh.haw.tron.client.controllers.game.helpers.RandomNameGenerator;
import de.alshikh.haw.tron.client.controllers.game.inputhandlers.GameInputHandler;
import de.alshikh.haw.tron.client.controllers.lobby.ILobbyController;
import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import de.alshikh.haw.tron.client.views.game.IGameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public final class GameController implements IGameController, InvalidationListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final IGameModel gameModel;
    private final IGameView gameView;
    private final ILobbyController lobbyController;

    private final Object UILock = new Object();
    private final GameUpdater gameUpdater;
    private final Timeline gameLoop;

    // TODO: Player preferences
    private final StringProperty playerName;

    public GameController(IGameModel gameModel, IGameView gameView, ILobbyController lobbyController, ExecutorService es) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.lobbyController = lobbyController;

        this.playerName = new SimpleStringProperty(RandomNameGenerator.get());
        this.gameUpdater = new GameUpdater(gameModel, es, UILock);
        this.gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(0.1),
                e -> es.execute(gameUpdater::updateGame))
        );

        this.gameModel.addListener(this); // on model update update the view
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void showStartMenu() {
        gameView.showStartMenu(
                e -> createGame(),
                e -> joinGame(),
                playerName
        );
    }

    private void createGame() {
        logger.info("Starting a new Game as host");
        gameModel.createGame(playerName);
        lobbyController.createRoom(gameModel.getGame().getPlayer().getName(), this);
        gameView.reset();
        gameView.showWaitingMenu();
    }

    @Override
    public void joinGame() {
        logger.info("Starting a new Game as player");
        gameModel.joinGame(playerName);
        lobbyController.showRoomsMenu(this);
    }

    @Override
    public void admit(IGameController opponentController) {
        gameModel.getGame().getOpponent().nameProperty().set(opponentController.playerNameProperty().get());
        opponentController.getPlayerUpdate().addListener(gameUpdater);
    }

    @Override
    public void startGame() {
        gameView.reset();
        gameView.getScene().setOnKeyPressed(new GameInputHandler(gameModel.getGame().getPlayer()));
        gameLoop.play();
    }

    @Override
    public PlayerUpdate getPlayerUpdate() {
        return gameModel.getGame().getPlayer().getUpdate();
    }

    @Override
    public void invalidated(Observable observable) {
        if (observable instanceof IGameModel)
            gameStateChangeObserved((IGameModel) observable);
    }

    private void gameStateChangeObserved(IGameModel gameModel) {
        synchronized (UILock) {
            logger.debug("lock: rendering game state");
            if (gameModel.getGame().ended()) {
                Platform.runLater(this::endGame);
                return;
            }
            gameView.showGame(gameModel.getGame());
            logger.debug("unlock: rendering game state");
        }
    }

    private void endGame() {
        endGame(gameModel.getGame().getWinner() == null ? "It's a tie" : gameModel.getGame().getWinner() + " won");
    }

    private void endGame(String message) {
        gameLoop.stop();
        gameView.showWinnerMenu(message, e -> createGame());
    }

    @Override
    public IGameModel getGameModel() {
        return gameModel;
    }

    @Override
    public IGameView getGameView() {
        return gameView;
    }

    @Override
    public StringProperty playerNameProperty() {
        return playerName;
    }
}
