package de.alshikh.haw.tron.app.models.game.data.entities;

import javafx.beans.property.StringProperty;

import java.util.UUID;

public class Player implements IPlayer {

    private final UUID id = UUID.randomUUID();
    private final StringProperty name;
    private IBike bike;
    private IPlayerUpdate update;
    private IPlayerUpdate previousUpdate;
    private boolean dead;

    public Player(StringProperty name) {
        this(name, null);
    }

    public Player(StringProperty name, IBike bike) {
        this.name = name;
        reset(bike);
    }

    @Override
    public void reset(IBike bike) {
        this.bike = bike;
        this.update = new PlayerUpdate();
        this.dead = false;
    }

    @Override
    public void move() {
        bike.move();
    }

    @Override
    public void createUpdate() {
        bike.lockDirection();
        renewUpdate();
    }

    @Override
    public void renewUpdate() {
        previousUpdate = update.copy();
        update.renew(bike.getMovingDirection(), dead);
    }

    @Override
    public void applyUpdate(IPlayerUpdate update) {
        bike.setMovingDirection(update.getMovingDirection());
        dead = update.isDead();
        this.update = update;
    }

    @Override
    public void publishUpdate() {
        update.publishUpdate();
    }

    @Override
    public void publishPreviousUpdate() {
        update.publishUpdate(previousUpdate);
    }

    @Override
    public void die() {
        this.dead = true;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public IBike getBike() {
        return bike;
    }

    @Override
    public IPlayerUpdate getUpdate() {
        return update;
    }

    @Override
    public int getUpdateVersion() {
        return update.getVersion();
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public String toString() {
        return getName();
    }
}
