package de.alshikh.haw.tron.client.common.data.entites;

import de.alshikh.haw.tron.client.models.data.entities.Bike;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 623286517279433635L;

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

    public boolean isDead() {
        return dead;
    }

    //@Override
    //public boolean equals(Object o) {
    //    if (this == o) return true;
    //    if (o == null || getClass() != o.getClass()) return false;
    //    Player player = (Player) o;
    //    return name.equals(player.name) && bike.equals(player.bike);
    //}
    //
    //@Override
    //public int hashCode() {
    //    return Objects.hash(name, bike);
    //}

    @Override
    public String toString() {
        return name;
    }
}
