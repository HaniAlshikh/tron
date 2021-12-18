package de.alshikh.haw.tron.client.common.data.entites;

import de.alshikh.haw.tron.client.models.game.data.entities.Bike;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;

//
public class Player {

    private final String name;
    private final Bike bike;
    private boolean dead = false;
    private boolean pauseGame = false;
    private PlayerUpdate update;
    int version;

    public Player(String name, Bike bike) {
        this.name = name;
        this.bike = bike;
        createUpdate(); // start position
    }

    public void move() {
        bike.move();
        createUpdate();
    }

    private void createUpdate() {
        this.update = new PlayerUpdate(bike.getPosition().x, bike.getPosition().y, pauseGame, ++version);
    }

    public void applyUpdate(PlayerUpdate update) {
        bike.move(update.getX(), update.getY());
        pauseGame = update.pauseGame();
        version = update.getVersion();
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

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return name;
    }
}
