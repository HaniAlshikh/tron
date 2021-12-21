package de.alshikh.haw.tron.client.models.game.data.entities;

import javafx.beans.property.StringProperty;

import java.util.UUID;

public class Player {

    private final UUID uuid = UUID.randomUUID();
    private final PlayerUpdate update = new PlayerUpdate();
    private int updateVersion;
    private boolean dead = false;
    private boolean pauseGame = false;

    private final StringProperty name;
    private final Bike bike;

    public Player(StringProperty name, Bike bike) {
        this.name = name;
        this.bike = bike;
        resetUpdate(); // start position
    }

    public void move() {
        bike.move();
        resetUpdate();
    }

    private void resetUpdate() {
        this.update.setValues(bike.getPosition().x, bike.getPosition().y, pauseGame, ++updateVersion);
    }

    public void applyUpdate(PlayerUpdate update) {
        bike.move(update.getX(), update.getY());
        pauseGame = update.pauseGame();
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

    public void die() {
        this.dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean pausedGame() {
        return pauseGame;
    }

    public void togglePauseGame() {
        this.pauseGame = !this.pauseGame;
    }

    public int getUpdateVersion() {
        return updateVersion;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return getName();
    }
}
