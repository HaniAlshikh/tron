package de.alshikh.haw.tron.app.models.game.data.entities;

import de.alshikh.haw.tron.app.models.game.data.datatypes.Direction;
import javafx.beans.InvalidationListener;

import java.util.ArrayList;
import java.util.List;

public class PlayerUpdate implements IPlayerUpdate {
    private final List<InvalidationListener> listeners = new ArrayList<>();

    private Direction movingDirection;
    private boolean dead;
    private int version;

    public PlayerUpdate() {}

    public PlayerUpdate(Direction movingDirection, boolean dead, int version) {
        setValues(movingDirection, dead, version);
    }

    @Override
    public void setValues(Direction movingDirection, boolean dead, int version) {
        this.movingDirection = movingDirection;
        this.dead = dead;
        this.version = version;
    }

    @Override
    public void renew(Direction movingDirection, boolean dead) {
        setValues(Direction.valueOf(movingDirection.name()), dead, ++version);
    }

    @Override
    public IPlayerUpdate copy() {
        return new PlayerUpdate(this.movingDirection, this.dead, this.version);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
        listener.invalidated(this);
    }

    @Override
    public void publishUpdate() {
        listeners.forEach(l -> l.invalidated(this));
    }

    @Override
    public void publishUpdate(IPlayerUpdate playerUpdate) {
        listeners.forEach(l -> l.invalidated(playerUpdate));
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Direction getMovingDirection() {
        return movingDirection;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "PlayerUpdate{" +
                "movingDirection=" + movingDirection +
                ", dead=" + dead +
                ", version=" + version +
                '}';
    }
}
