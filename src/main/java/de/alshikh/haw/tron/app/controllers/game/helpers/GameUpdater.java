package de.alshikh.haw.tron.app.controllers.game.helpers;

import de.alshikh.haw.tron.app.controllers.game.IGameController;
import de.alshikh.haw.tron.app.models.game.IGameModel;
import de.alshikh.haw.tron.app.models.game.data.entities.PlayerUpdate;
import javafx.application.Platform;
import javafx.beans.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameUpdater implements IUpdater {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    Map<Integer, PlayerUpdate> opponentUpdatesCache = new ConcurrentHashMap<>();

    private PlayerUpdate receivedOpponentUpdate;
    private final Object gameStateLock = new Object();
    private final Object UILock = new Object();
    private int retries = 0;

    private final IGameController gameController;

    public GameUpdater(IGameController gameController) {
        this.gameController = gameController;
    }

    public void updateGame() {
        synchronized (gameStateLock) { // in case updating the state took longer than the tick
            if (!fairPlayInsured()) return;
            logger.debug("it took " + retries + " retries to get the update");
            retries = 0;

            // no need to run this asynchronously as the opponent will keep pushing it's update
            // on each tick and wait for us to send our update
            // we will have to wait for the game state update to give the player the chance to react
            // before consuming the next update
            logger.debug("lock: consume opponent update: " + receivedOpponentUpdate);
            synchronized (UILock) {
                logger.debug("lock: update game state");
                gameController.getGameModel().updateGameState(this.receivedOpponentUpdate); // consumes to old and creates new update
                gameController.getGameModel().createNewPlayerUpdate();
                logger.debug("unlock: update game state");
            }
        }
    }

    private boolean fairPlayInsured() {
        this.receivedOpponentUpdate = opponentUpdatesCache.remove(getPlayerUpdateVersion());
        if (this.receivedOpponentUpdate == null) {
            if (retries == gameController.getNumberOfRetries())
                Platform.runLater(() -> gameController.endGame("You won because "
                        + gameController.getGameModel().getGame().getOpponent().getName() + " stopped responding")); // TODO
            retries++;
            logger.debug("resending update");
            gameController.getEs().execute(() -> gameController.getGameModel().getGame().getPlayer().getUpdate().publishUpdate());
            return false;
        }
        return true;
    }

    @Override
    public void invalidated(Observable observable) {
        if (observable instanceof PlayerUpdate)
            playerUpdateObserved((PlayerUpdate) observable);

        else if (observable instanceof IGameModel)
            gameStateChangeObserved((IGameModel) observable);
    }

    private void gameStateChangeObserved(IGameModel gameModel) {
        synchronized (UILock) {
            logger.debug("lock: rendering game state");
            if (gameModel.getGame().ended()) {
                Platform.runLater(gameController::endGame);
            }
            gameController.getGameView().showGame(gameModel.getGame());
            logger.debug("unlock: rendering game state");
        }
    }

    private void playerUpdateObserved(PlayerUpdate opponentUpdate) {
        logger.debug("received opponent update: " + opponentUpdate);
        opponentUpdatesCache.put(opponentUpdate.getVersion(), opponentUpdate);

        // TODO: where is the correct place for this?
        if (getPlayerUpdateVersion() > opponentUpdate.getVersion()) {
            logger.debug("resending previous update");
            gameController.getEs().execute(() -> gameController.getGameModel().getGame().getPlayer().getUpdate().publishPreviousUpdate());
        }

        //else if (getPlayerUpdateVersion() < opponentUpdate.getVersion()) {
        //    //retries++;
        //    logger.debug("resending update");
        //    gameController.getEs().execute(() -> gameController.getGameModel().getGame().getPlayer().getUpdate().publishUpdate());
        //}
    }

    private int getPlayerUpdateVersion() {
        return gameController.getGameModel().getGame().getPlayer().getUpdateVersion();
    }
}
