package de.alshikh.haw.tron.client.models.game.data.entities;

import javafx.beans.property.StringProperty;

public class Player {

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

    @Override
    public String toString() {
        return getName();
    }
}
