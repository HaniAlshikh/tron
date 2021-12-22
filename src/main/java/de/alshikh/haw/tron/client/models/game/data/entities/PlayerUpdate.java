package de.alshikh.haw.tron.client.models.game.data.entities;

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

    private int x, y;
    private boolean pauseGame;
    private int version;
    private boolean dead = false;

    public PlayerUpdate() {}

    public PlayerUpdate(int x, int y, boolean pauseGame, boolean dead, int version) {
        setValues(x, y, pauseGame, dead, version);
    }

    public void setValues(int x, int y, boolean pauseGame, boolean dead, int version) {
        this.x = x;
        this.y = y;
        this.pauseGame = pauseGame;
        this.dead = dead;
        this.version = version;
    }

    public void update(int x, int y, boolean pauseGame, boolean dead, int version) {
        previousUpdate = this.copy();
        setValues(x, y, pauseGame, dead, version);
        publishUpdate();
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
        return new PlayerUpdate(this.x, this.y, this.pauseGame, this.dead, this.version);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean pauseGame() {
        return pauseGame;
    }

    public boolean isDead() {
        return dead;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "PlayerUpdate{" +
                "x=" + x +
                ", y=" + y +
                ", pauseGame=" + pauseGame +
                ", version=" + version +
                ", dead=" + dead +
                '}';
    }
}
