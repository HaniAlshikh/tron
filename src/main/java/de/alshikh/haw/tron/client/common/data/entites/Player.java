package de.alshikh.haw.tron.client.common.data.entites;

import de.alshikh.haw.tron.client.models.game.data.entities.Bike;

//
public class Player {

    private final String name;
    private final Bike bike;
    private boolean dead = false;
    private boolean pauseGame = false;

    public Player(String name, Bike bike) {
        this.name = name;
        this.bike = bike;
    }

    public void move() {
        bike.move();
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

    @Override
    public String toString() {
        return name;
    }
}
