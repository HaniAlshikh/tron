package de.alshikh.haw.tron.client.controllers.game;

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
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class GameController implements IGameController, InvalidationListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final IGameModel gameModel;
    private final IGameView gameView;
    private final ILobbyController lobbyController;

    private IGameController opponentController;
    private PlayerUpdate opponentUpdate;
    private final Object lock = new Object();
    // TODO: reason of this was to solve update redering skip if we received
    //  a new update that changes the game state before it's even rendered
    //  which causes missing trail point of the update
    //  yet it's still not working
    private final Object UILock = new Object();

    private final Timeline gameLoop;

    ExecutorService exec = Executors.newFixedThreadPool(10);

    public GameController(IGameModel gameModel, IGameView gameView, ILobbyController lobbyController) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.lobbyController = lobbyController;


        this.gameModel.addListener(this); // on model update update the view
        this.gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(0.1),
                e -> exec.execute(this::updateGame))
        );
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
        if (observable instanceof PlayerUpdate) {
            synchronized (lock) {
                logger.debug("lock: receive opponent update");
                this.opponentUpdate = (PlayerUpdate) observable;
                logger.debug("unlock: receive opponent update");
                return;
            }
        }


        // TODO: maybe publisher subscriber pattern?
        synchronized (UILock) {
            logger.debug("lock: rendering game state");
            if (gameModel.getGame().ended()) {
                Platform.runLater(this::endGame);
                return;
            }
            gameView.showGame(gameModel.getGame().getPlayer(), gameModel.getGame().getOpponent()); // TODO: bind game view to game
            logger.debug("unlock: rendering game state");
        }
    }

    @Override
    public void admit(IGameController opponentController) {
        // server
        this.logger = LoggerFactory.getLogger("Server " + this.getClass().getSimpleName());
        this.opponentController = opponentController;
        // TODO: update lister should be added before the initial position update is created
        this.opponentUpdate = this.opponentController.getPlayerUpdate();
        this.opponentController.getPlayerUpdate().addListener(this);
    }

    @Override
    public void joinGame(IGameController opponentController) {
        // client
        this.logger = LoggerFactory.getLogger("Client " + this.getClass().getSimpleName());
        this.opponentController = opponentController;
        // TODO: update lister should be added before the initial position update is created
        this.opponentUpdate = this.opponentController.getPlayerUpdate();
        this.opponentController.getPlayerUpdate().addListener(this);
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
        synchronized (lock) {
            logger.debug("lock: consume opponent update");
            if (this.opponentUpdate == null) return; // we received no updates yet
            if (!fairPlayEnsured()) {
                // TODO: after x attempts end the game?
                //  player version < opponent -> player lost
                //Platform.runLater(() -> endGame("Game ended because of a network error"));
                return; // wait for resend or correct upddate version
            }
            // no need to run this asynchronously as the opponent will keep pushing it's update
            // on each tick and wait for us to send our update
            // we will have to wait for the game state update to give the player the chance to react
            // before consuming the next update
            synchronized (UILock) {
                logger.debug("lock: update game state");
                gameModel.updateGameState(this.opponentUpdate);
                logger.debug("unlock: update game state");
            }

            this.opponentUpdate = null; // consume the update (no need to keep processing the same update if no new one is received)
            logger.debug("unlock: consume opponent update");
        }
        gameModel.getGame().getPlayer().move();
    }

    private boolean fairPlayEnsured() {
        logger.debug("Player version: " + gameModel.getGame().getPlayer().getVersion() + " " + this.opponentUpdate.getVersion() + " :Opponent version");
        if (gameModel.getGame().getPlayer().getVersion() == this.opponentUpdate.getVersion() ||
                // Player is lacking behind and should have the opportunity to continue moving
                // the opponent will wait as he has a grater version
                gameModel.getGame().getPlayer().getVersion() < this.opponentUpdate.getVersion()) {
            return true;
        }

        exec.execute(() -> gameModel.getGame().getPlayer().getUpdate().publishUpdate());
        return false;
    }

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
