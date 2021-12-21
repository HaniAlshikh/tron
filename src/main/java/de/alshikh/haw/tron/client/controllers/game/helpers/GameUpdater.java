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
            logger.debug("lock: consume opponent update");
            if (this.receivedOpponentUpdate == null) return; // we received no updates yet
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
                gameModel.updateGameState(this.receivedOpponentUpdate);
                logger.debug("unlock: update game state");
            }

            this.receivedOpponentUpdate = null; // consume the update (no need to keep processing the same update if no new one is received)
            logger.debug("unlock: consume opponent update");
        }
        gameModel.getGame().getPlayer().move();
    }

    private boolean fairPlayEnsured() {
        logger.debug("Player version: " + gameModel.getGame().getPlayer().getUpdateVersion() + " " + this.receivedOpponentUpdate.getVersion() + " :Opponent version");
        if (gameModel.getGame().getPlayer().getUpdateVersion() == this.receivedOpponentUpdate.getVersion() ||
                // Player is lacking behind and should have the opportunity to continue moving
                // the opponent will wait as he has a grater version
                gameModel.getGame().getPlayer().getUpdateVersion() < this.receivedOpponentUpdate.getVersion()) {
            return true;
        }

        es.execute(() -> gameModel.getGame().getPlayer().getUpdate().publishUpdate());
        return false;
    }

    @Override
    public void invalidated(Observable observable) {
        if (observable instanceof PlayerUpdate)
            playerUpdateObserved((PlayerUpdate) observable);
    }

    private void playerUpdateObserved(PlayerUpdate playerUpdate) {
        synchronized (gameStateLock) {
            logger.debug("lock: receive opponent update");
            this.receivedOpponentUpdate = playerUpdate;
            logger.debug("unlock: receive opponent update");
        }
    }

}
