package de.alshikh.haw.tron.app.models.game.data.entities;

import de.alshikh.haw.tron.app.models.game.data.datatypes.Direction;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlayerUpdate implements Observable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final List<InvalidationListener> listeners = new ArrayList<>();
    private PlayerUpdate previousUpdate;

    private Direction movingDirection;
    private boolean pauseGame;
    private boolean dead = false;
    private int version;

    public PlayerUpdate() {}

    public PlayerUpdate(Direction movingDirection, boolean pauseGame, boolean dead, int version) {
        setValues(movingDirection, pauseGame, dead, version);
    }

    public void setValues(Direction movingDirection, boolean pauseGame, boolean dead, int version) {
        this.movingDirection = movingDirection;
        this.pauseGame = pauseGame;
        this.dead = dead;
        this.version = version;
    }

    public void update(Direction movingDirection, boolean pauseGame, boolean dead, int version) {
        previousUpdate = this.copy();
        setValues(Direction.valueOf(movingDirection.name()), pauseGame, dead, version);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
        listener.invalidated(this);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    public void publishUpdate() {
        logger.debug("publishing player update: " + this);
        listeners.forEach(l -> l.invalidated(this));
    }


    public void publishPreviousUpdate() {
        logger.debug("publishing previous player update: " + previousUpdate);
        listeners.forEach(l -> l.invalidated(previousUpdate));
    }

    public PlayerUpdate copy() {
        return new PlayerUpdate(this.movingDirection, this.pauseGame, this.dead, this.version);
    }

    public Direction getMovingDirection() {
        return movingDirection;
    }

    public void setMovingDirection(Direction movingDirection) {
        this.movingDirection = movingDirection;
    }

    public boolean pauseGame() {
        return pauseGame;
    }

    public void setPauseGame(boolean pauseGame) {
        this.pauseGame = pauseGame;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "PlayerUpdate{" +
                "movingDirection=" + movingDirection +
                ", pauseGame=" + pauseGame +
                ", dead=" + dead +
                ", version=" + version +
                '}';
    }
}
