package de.alshikh.haw.tron.client.models.game.data.entities;

public class Player {

    private final PlayerUpdate update = new PlayerUpdate();
    private int updateVersion;
    private boolean dead = false;
    private boolean pauseGame = false;

    private final String name;
    private final Bike bike;

    public Player(String name, Bike bike) {
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
        return name;
    }
}
