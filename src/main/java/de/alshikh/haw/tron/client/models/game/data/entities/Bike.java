package de.alshikh.haw.tron.client.models.game.data.entities;

import de.alshikh.haw.tron.client.models.game.data.datatypes.BikeStartingPosition;
import de.alshikh.haw.tron.client.models.game.data.datatypes.Direction;
import de.alshikh.haw.tron.client.views.view_library.Coordinate;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Bike {
    private static final long serialVersionUID = 623241517279433635L;

    Direction movingDirection;
    List<Coordinate> trail = new ArrayList<>();
    Color color;

    public Bike(BikeStartingPosition startPosition, Color color) {
        this.trail.add(startPosition.getCoordinate());
        this.color = color;
        this.movingDirection = startPosition.getMovingDirection();
    }

    public void steer(Direction newDirection) {
        if (newDirection.isAllowed(movingDirection)) {
            this.movingDirection = newDirection;
        }
    }

    public void move() {
        trail.add(movingDirection.calculateNewPosition(getPosition()));
    }

    public Direction getMovingDirection() {
        return movingDirection;
    }

    public List<Coordinate> getTrail() {
        return trail;
    }

    public Coordinate getPosition() {
        return trail.get(trail.size() - 1);
    }

    public Color getColor() {
        return color;
    }
}
