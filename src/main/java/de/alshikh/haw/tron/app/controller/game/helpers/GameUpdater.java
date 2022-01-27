package de.alshikh.haw.tron.app.controller.game.helpers;

import de.alshikh.haw.tron.manager.Config;
import de.alshikh.haw.tron.app.controller.game.IGameController;
import de.alshikh.haw.tron.app.model.game.IGameModel;
import de.alshikh.haw.tron.app.model.game.data.entities.IPlayer;
import de.alshikh.haw.tron.app.model.game.data.entities.IPlayerUpdate;
import de.alshikh.haw.tron.app.model.game.data.entities.PlayerUpdate;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameUpdater implements IGameUpdater {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // TD01

    private final Object gameStateLock = new Object();
    private final Object UILock = new Object();
    private boolean running;
    private int updateRetries;

    private final Timeline gameLoop;
    private IPlayerUpdate receivedOpponentUpdate;
    private Map<Integer, IPlayerUpdate> opponentUpdatesCache; // TD02

    private final IGameController gameController;

    public GameUpdater(IGameController gameController) {
        this.gameController = gameController;
        this.gameLoop = new Timeline(new KeyFrame(
                Duration.seconds(1.0 / Config.FRAMES_PER_SECOND),
                e -> updateGame()));
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void start() {
        running = true;
        updateRetries = 0;
        opponentUpdatesCache = new ConcurrentHashMap<>();
        gameLoop.play();
    }

    @Override
    public void updateGame() {
        synchronized (gameStateLock) { // TD03
            logger.debug("lock: update game state");
            if (!fairPlayInsured()) return; // TD04
            logger.debug("consuming opponent update: " + receivedOpponentUpdate);
            logger.debug("it took " + updateRetries + " retries to get the update");
            updateRetries = 0;
            synchronized (UILock) { // TD05
                updateGameState();
            }
            logger.debug("unlock: update game state");
        }
    }

    private void updateGameState() {
        gameController.getGameModel().updateGameState(receivedOpponentUpdate);
        gameController.getGameModel().createNewPlayerUpdate();
    }

    private boolean fairPlayInsured() {
        receivedOpponentUpdate = opponentUpdatesCache.remove(getPlayer().getUpdateVersion());
        if (receivedOpponentUpdate == null) {
            if (updateRetries == Config.UPDATE_MAX_RETRIES)
                Platform.runLater(() -> gameController.endGame(
                        "You won because " + getOpponent().getName() + " stopped responding"));
            updateRetries++;
            logger.debug("resending update");
            es.execute(() -> gameController.getGameModel().publishPlayerUpdate());
            return false;
        }
        return true;
    }

    @Override
    public void invalidated(Observable observable) {
        if (!running)
            return;

        if (observable instanceof PlayerUpdate)
            playerUpdateObserved((IPlayerUpdate) observable);

        else if (observable instanceof IGameModel)
            gameStateChangeObserved((IGameModel) observable);
    }

    private void gameStateChangeObserved(IGameModel gameModel) {
        synchronized (UILock) {
            logger.debug("lock: rendering game state");
            if (gameModel.getGame().hasEnded())
                Platform.runLater(gameController::endGame);
            gameController.getGameView().showGame(gameModel.getGame());
            logger.debug("unlock: rendering game state");
        }
    }

    private void playerUpdateObserved(IPlayerUpdate opponentUpdate) {
        logger.debug("received opponent update: " + opponentUpdate);
        opponentUpdatesCache.put(opponentUpdate.getVersion(), opponentUpdate);
        if (getPlayer().getUpdateVersion() > opponentUpdate.getVersion()) {
            logger.debug("resending previous update");
            es.execute(() -> getPlayer().publishPreviousUpdate());
        }
    }

    @Override
    public void stop() {
        running = false;
        gameLoop.stop();
    }

    private IPlayer getPlayer() {
        return gameController.getGameModel().getGame().getPlayer();
    }

    private IPlayer getOpponent() {
        return gameController.getGameModel().getGame().getOpponent();
    }
}