package de.alshikh.haw.tron.client.controllers.game.helpers;

import de.alshikh.haw.tron.client.controllers.game.IGameController;
import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: WIP the skip problem
//  see if the idee of per version updating make any sense

public class GameUpdater implements InvalidationListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    Map<Integer, PlayerUpdate> receivedUpdates = new ConcurrentHashMap<>();

    private PlayerUpdate receivedOpponentUpdate;
    private final Object gameStateLock = new Object();
    private final Object UILock = new Object();
    private int retries = 0;
    private int currentTact; // insure fair play

    private final IGameController gameController;

    public GameUpdater(IGameController gameController) {
        this.gameController = gameController;
        reset();
    }

    public void updateGame() {
        // on each tick we publish the update which locks the steering direction
        //gameController.getEs().execute(() -> gameController.getGameModel().getGame().getPlayer().getUpdate().publishUpdate());
        synchronized (gameStateLock) {
            // insure fair play
            logger.debug("Current Tact: " + currentTact);
            this.receivedOpponentUpdate = receivedUpdates.remove(currentTact);
            if (this.receivedOpponentUpdate == null) {
                if (retries == gameController.getNumberOfRetries())
                    Platform.runLater(() -> gameController.endGame("You won because of a network error"));
                retries++;
                return;
            }

            logger.debug("lock: consume opponent update: " + receivedOpponentUpdate);
            logger.debug("it took " + retries + " retries to get the update");
            retries = 0;

            // no need to run this asynchronously as the opponent will keep pushing it's update
            // on each tick and wait for us to send our update
            // we will have to wait for the game state update to give the player the chance to react
            // before consuming the next update
            synchronized (UILock) {
                logger.debug("lock: update game state");
                gameController.getGameModel().updateGameState(this.receivedOpponentUpdate); // consumes to old and creates new update
                gameController.getGameModel().getGame().getPlayer().getUpdate().publishUpdate();
                logger.debug("unlock: update game state");
            }
            currentTact++;
            //logger.debug("Moving player");
            //gameController.getGameModel().getGame().getPlayer().move();
            //logger.debug("New player update: " + gameController.getGameModel().getGame().getPlayer().getUpdate());
        }
    }

    private boolean fairPlayEnsured() {
        logger.debug("Player version: " + getUpdateVersion() + " " + this.receivedOpponentUpdate.getVersion() + " :Opponent version");
        return getUpdateVersion() == this.receivedOpponentUpdate.getVersion();
        //if (gameModel.getGame().getPlayer().getUpdateVersion() == this.receivedOpponentUpdate.getVersion() ||
                // Player is lacking behind and should have the opportunity to continue moving
                // the opponent will wait as he has a grater version
                // TODO: this doesn't work as the other player might send 3 updates while we are stil on the first one
                //  but only process the third one as the catche up and there fore skip the second
                //gameModel.getGame().getPlayer().getUpdateVersion() < this.receivedOpponentUpdate.getVersion()) {


        //if (gameModel.getGame().getPlayer().getUpdateVersion() == this.receivedOpponentUpdate.getVersion()) {
        //    return true;
        //}
        //
        //if (gameModel.getGame().getPlayer().getUpdateVersion() > this.receivedOpponentUpdate.getVersion()) {
        //    es.execute(() -> gameModel.getGame().getPlayer().getUpdate().publishPreviousUpdate());
        //    return false;
        //}
        //
        //if (gameModel.getGame().getPlayer().getUpdateVersion() < this.receivedOpponentUpdate.getVersion()) {
        //    receivedOpponentUpdate = null;
        //    es.execute(() -> gameModel.getGame().getPlayer().getUpdate().publishUpdate());
        //    return false;
        //}
        //
        //return false;
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
                return;
            }
            gameController.getGameView().showGame(gameModel.getGame());
            logger.debug("unlock: rendering game state");
        }
    }

    private void playerUpdateObserved(PlayerUpdate playerUpdate) {
        logger.debug("received opponent update: " + playerUpdate);

        receivedUpdates.put(playerUpdate.getVersion(), playerUpdate);

        //if (getUpdateVersion() > playerUpdate.getVersion()) {
        //    retries++;
        //    logger.debug("resending previous update");
        //    gameController.getEs().execute(() -> gameController.getGameModel().getGame().getPlayer().getUpdate().publishPreviousUpdate());
        //}
        //
        //else if (getUpdateVersion() < playerUpdate.getVersion()) {
        //    retries++;
        //    logger.debug("resending update");
        //    gameController.getEs().execute(() -> gameController.getGameModel().getGame().getPlayer().getUpdate().publishUpdate());
        //}
    }

    private int getUpdateVersion() {
        return gameController.getGameModel().getGame().getPlayer().getUpdateVersion();
    }

    public void reset() {
        currentTact = 1;
    }
}
