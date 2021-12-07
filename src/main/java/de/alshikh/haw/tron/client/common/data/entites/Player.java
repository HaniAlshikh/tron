package de.alshikh.haw.tron.client.common.data.entites;

import de.alshikh.haw.tron.client.models.game.data.entities.Bike;

// game is observable and player is iobserver
public class Player {

    String name;
    Bike bike;
    boolean dead = false;

    public Player(String name, Bike bike) {
        this.name = name;
        this.bike = bike;
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

    @Override
    public String toString() {
        return name;
    }
}
