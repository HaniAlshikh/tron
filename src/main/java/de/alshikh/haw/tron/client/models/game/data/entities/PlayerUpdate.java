package de.alshikh.haw.tron.client.models.game.data.entities;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;
import java.util.List;

public class PlayerUpdate implements Observable {

    private final List<InvalidationListener> listeners = new ArrayList<>();

    private int x, y;
    private boolean pauseGame;
    private int version;

    public PlayerUpdate() {}

    public PlayerUpdate(int x, int y, boolean pauseGame, int version) {
        setValues(x, y, pauseGame, version);
    }

    public void setValues(int x, int y, boolean pauseGame, int version) {
        this.x = x;
        this.y = y;
        this.pauseGame = pauseGame;
        this.version = version;
        publishUpdate();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    public void publishUpdate() {
        listeners.forEach(l -> l.invalidated(this));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean pauseGame() {
        return pauseGame;
    }

    public void setPauseGame(boolean pauseGame) {
        this.pauseGame = pauseGame;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "PlayerUpdate{" +
                "x=" + x +
                ", y=" + y +
                ", pauseGame=" + pauseGame +
                ", version=" + version +
                '}';
    }
}
