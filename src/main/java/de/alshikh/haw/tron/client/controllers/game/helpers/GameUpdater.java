package de.alshikh.haw.tron.client.controllers.game.helpers;

import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class GameUpdater implements InvalidationListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private PlayerUpdate receivedOpponentUpdate;
    private final Object gameStateLock = new Object();

    private final IGameModel gameModel;
    private final ExecutorService es;
    private final Object UILock;

    public GameUpdater(IGameModel gameModel, ExecutorService es, Object UILock) {
        this.gameModel = gameModel;
        this.es = es;
        this.UILock = UILock;
    }

    public void updateGame() {
        synchronized (gameStateLock) {
            logger.debug("lock: consume opponent update: " + receivedOpponentUpdate);
            if (!fairPlayEnsured()) {
                // TODO: after x attempts end the game?
                //  player version < opponent -> player lost
                //Platform.runLater(() -> endGame("Game ended because of a network error"));
                return; // wait for resend or correct upddate version
            }
            //if (this.receivedOpponentUpdate == null) return; // we received no updates yet
            // no need to run this asynchronously as the opponent will keep pushing it's update
            // on each tick and wait for us to send our update
            // we will have to wait for the game state update to give the player the chance to react
            // before consuming the next update
            synchronized (UILock) {
                logger.debug("lock: update game state");
                gameModel.updateGameState(this.receivedOpponentUpdate);
                logger.debug("unlock: update game state");
            }

            //this.receivedOpponentUpdate = null; // consume the update
            logger.debug("unlock: consume opponent update");
        }

        logger.debug("Moving player");
        gameModel.getGame().getPlayer().move();
        logger.debug("New player update: " + gameModel.getGame().getPlayer().getUpdate());
    }

    private boolean fairPlayEnsured() {
        logger.debug("Player version: " + gameModel.getGame().getPlayer().getUpdateVersion() + " " + this.receivedOpponentUpdate.getVersion() + " :Opponent version");
        return gameModel.getGame().getPlayer().getUpdateVersion() == this.receivedOpponentUpdate.getVersion();
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
    }

    private void playerUpdateObserved(PlayerUpdate playerUpdate) {
        //logger.debug("received opponent update: " + playerUpdate);
        //synchronized (gameStateLock) {
        //    logger.debug("set opponent update for consumption");
        //    receivedOpponentUpdate = playerUpdate;
        //}

        //logger.debug("received opponent update: " + playerUpdate);
        //if (receivedOpponentUpdate == null) {
        //    logger.debug("set opponent update for consumption");
        //    receivedOpponentUpdate = playerUpdate;
        //}

        synchronized (gameStateLock) {
            logger.debug("received opponent update: " + playerUpdate);
            if (gameModel.getGame().getPlayer().getUpdateVersion() == playerUpdate.getVersion()) {
                logger.debug("set opponent update for consumption");
                receivedOpponentUpdate = playerUpdate;
            }

            if (gameModel.getGame().getPlayer().getUpdateVersion() > playerUpdate.getVersion()) {
                logger.debug("resending previous update");
                es.execute(() -> gameModel.getGame().getPlayer().getUpdate().publishPreviousUpdate());
            }

            if (gameModel.getGame().getPlayer().getUpdateVersion() < playerUpdate.getVersion()) {
                logger.debug("resending update");
                es.execute(() -> gameModel.getGame().getPlayer().getUpdate().publishUpdate());
            }
        }
    }
}
