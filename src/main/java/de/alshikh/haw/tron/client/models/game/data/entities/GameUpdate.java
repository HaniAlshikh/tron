package de.alshikh.haw.tron.client.models.game.data.entities;

import de.alshikh.haw.tron.client.views.view_library.Coordinate;

import java.util.ArrayList;
import java.util.List;


// TODO: this should use primitive types only to easy network transfer
public class GameUpdate {

    List<Coordinate> trail = new ArrayList<>();
    boolean pause = false;

    public GameUpdate(List<Coordinate> trail, boolean pause) {
        this.trail = trail;
        this.pause = pause;
    }

    public List<Coordinate> getTrail() {
        return trail;
    }

    public void setTrail(List<Coordinate> trail) {
        this.trail = trail;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }
}
