package de.alshikh.haw.tron.app.models.game.data.entities;

import javafx.beans.property.StringProperty;

import java.util.UUID;

public class Player {
    private final UUID id = UUID.randomUUID();
    private final StringProperty name;

    private Bike bike;
    private PlayerUpdate update;
    private boolean pauseGame;
    private boolean dead;
    private int updateVersion;

    public Player(StringProperty name) {
        this(name, null);
    }

    public Player(StringProperty name, Bike bike) {
        this.name = name;
        reset(bike);
    }

    public void reset(Bike bike) {
        this.bike = bike;
        this.update = new PlayerUpdate();
        this.pauseGame = false;
        this.dead = false;
        this.updateVersion = 0;
    }

    public void move() {
        bike.move();
    }

    public void createUpdate() {
        bike.lockDirection();
        resetUpdate();
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
