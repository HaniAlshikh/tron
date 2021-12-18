package de.alshikh.haw.tron.client.controllers.game;

import de.alshikh.haw.tron.client.controllers.game.inputhandlers.GameInputHandler;
import de.alshikh.haw.tron.client.controllers.lobby.ILobbyController;
import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import de.alshikh.haw.tron.client.views.game.IGameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameController implements IGameController, InvalidationListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IGameModel gameModel;
    private final IGameView gameView;
    private final ILobbyController lobbyController;

    private IGameController opponentController;

    private final Timeline gameLoop;

    public GameController(IGameModel gameModel, IGameView gameView, ILobbyController lobbyController) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.lobbyController = lobbyController;


        this.gameModel.addListener(this);
        this.gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> updateGame()));
    }

    @Override
    public void showStartMenu() {
        gameView.showStartMenu(
                e -> setupGame(),
                e -> lobbyController.showRoomsMenu(this)
        );
    }

    private void setupGame() {
        logger.info("Starting a new Game");
        gameModel.createGame();
        lobbyController.createRoom(gameModel.getGame().getPlayer().getName(), this);
        gameView.reset();
        gameView.showWaitingMenu();
    }

    @Override
    public void invalidated(Observable observable) {
        gameView.showGame(gameModel.getGame().getPlayer(), gameModel.getGame().getOpponent()); // TODO: bind game view to game
    }

    @Override
    public void admit(IGameController opponentController) {
        this.opponentController = opponentController;
    }

    @Override
    public void joinGame(IGameController opponentController) {
        this.opponentController = opponentController;
        gameModel.joinGame();
    }

    @Override
    public void startGame() {
        gameView.reset();
        gameView.getScene().setOnKeyPressed(new GameInputHandler(gameModel.getGame().getPlayer()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    @Override
    public PlayerUpdate getPlayerUpdate() {
        return gameModel.getGame().getPlayer().getUpdate();
    }

    private void updateGame() {
        gameModel.applyOpponentUpdate(opponentController.getPlayerUpdate());
        if (!fairPlayEnsured()) {
            endGame("Game ended because of a network error");
        }
        gameModel.updateGame();
        if (gameModel.getGame().ended()) {
            endGame();
        }

        gameModel.getGame().getPlayer().move();
    }

    private boolean fairPlayEnsured() {
        return gameModel.comparePlayerVersions() == 0;
    }

    //// TODO: publisher subscriber pushes the update and waits for an update
    //private void pushUpdate() {
    //
    //}
    //
    //// ensure we receives the correct update to ensure fairness
    //private GameUpdate getOpponentUpdate() {
    //    GameUpdate opponentGameUpdate;
    //    do {
    //        opponentGameUpdate = opponentController.getGameUpdate();
    //    } while (opponentGameUpdate == null);
    //    return opponentGameUpdate;
    //}

    private void endGame() {
        endGame(gameModel.getGame().getWinner() == null ? "It's a tie" : gameModel.getGame().getWinner() + " won");
    }

    private void endGame(String message) {
        gameLoop.stop();
        gameView.showWinnerMenu(message, e -> setupGame());
    }

    @Override
    public IGameModel getGameModel() {
        return gameModel;
    }

    @Override
    public IGameView getGameView() {
        return gameView;
    }
}
