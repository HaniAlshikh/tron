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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public final class GameController implements IGameController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final IGameModel gameModel;
    private final IGameView gameView;
    private final ILobbyController lobbyController;
    private final ExecutorService es;

    private final GameUpdater gameUpdater;
    private final Timeline gameLoop;

    // TODO: Player preferences
    private final StringProperty playerName;

    // TODO: implement managed ExecutorService
    private Future<GameUpdater> gameUpdaterFuture;
    private final int numberOfRetries = 10;

    public GameController(IGameModel gameModel, IGameView gameView, ILobbyController lobbyController, ExecutorService es) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.lobbyController = lobbyController;
        this.es = es;

        this.playerName = new SimpleStringProperty(RandomNameGenerator.get());
        this.gameUpdater = new GameUpdater(this);
        this.gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(0.08),
                e -> this.gameUpdaterFuture = this.es.submit(gameUpdater::updateGame, gameUpdater))
        );

        this.gameLoop.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void showStartMenu(String message) {
        gameView.showStartMenu(
                playerName,
                message,
                e -> createGame(),
                e -> joinGame()
        );
    }

    private void createGame() {
        logger.info("Starting a new Game as host");
        gameModel.createGame(playerName);
        lobbyController.createRoom(gameModel.getGame().getPlayer().getId(), gameModel.getGame().getPlayer().getName(), this);
        gameView.showWaitingMenu(e -> cancelGame());
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

    private void cancelGame() {
        lobbyController.removeRoom(gameModel.getGame().getPlayer().getId());
        gameView.reset();
        showStartMenu("Ready?");
    }

    @Override
    public void startGame() {
        gameView.reset();
        GameInputHandler gameInputHandler = new GameInputHandler(gameModel.getGame().getPlayer());
        gameView.getScene().setOnKeyPressed(gameInputHandler);
        gameModel.addListener(gameUpdater); // on model update update the view
        gameLoop.play();
    }

    @Override
    public void endGame() {
        endGame(gameModel.getGame().getWinner() == null ? "It's a tie" : gameModel.getGame().getWinner() + " won");
    }

    @Override
    public void endGame(String message) {
        gameLoop.stop();
        gameUpdaterFuture.cancel(true);
        gameView.highlightCell(gameModel.getGame().getPlayer().getBike().getPosition(),
                gameModel.getGame().getOpponent().getBike().getPosition());
        showStartMenu(message);
    }

    @Override
    public void closeGame() {
        if (gameModel.getGame() != null) {
            gameLoop.stop();
            gameUpdaterFuture.cancel(true);
        }
    }

    @Override
    public PlayerUpdate getPlayerUpdate() {
        return gameModel.getGame().getPlayer().getUpdate();
    }

    @Override
    public StringProperty playerNameProperty() {
        return playerName;
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
    public ExecutorService getEs() {
        return es;
    }

    @Override
    public int getNumberOfRetries() {
        return numberOfRetries;
    }
}
