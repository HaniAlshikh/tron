package de.alshikh.haw.tron.client.models.game.data.entities;

import javafx.beans.property.StringProperty;

import java.util.UUID;

public class Player {

    // TODO: player uuid to associate with update
    private final UUID id = UUID.randomUUID();
    private final PlayerUpdate update;
    private boolean pauseGame = false;
    private boolean dead = false;
    private int updateVersion;

    private final StringProperty name;
    private final Bike bike;

    public Player(StringProperty name, Bike bike) {
        this.name = name;
        this.bike = bike;
        this.update = new PlayerUpdate();
    }

    public void move() {
        bike.move(); // move the old/update move
        bike.lockDirection();
        resetUpdate(); // lock the next move
    }

    public void die() {
        this.dead = true;
    }

    public void togglePauseGame() {
        this.pauseGame = !this.pauseGame;
    }

    public void resetUpdate() {
        this.update.update(bike.getMovingDirection(), pauseGame, dead, ++updateVersion);
    }

    public void applyUpdate(PlayerUpdate update) {
        bike.setMovingDirection(update.getMovingDirection());
        pauseGame = update.pauseGame();
        dead = update.isDead();
        updateVersion = update.getVersion();
    }

    public PlayerUpdate getUpdate() {
        return update;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Bike getBike() {
        return bike;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean pausedGame() {
        return pauseGame;
    }

    public int getUpdateVersion() {
        return updateVersion;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }
}
