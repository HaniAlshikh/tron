package de.alshikh.haw.tron.client.models.game.data.entities;

import de.alshikh.haw.tron.client.models.game.data.datatypes.BikeStartingPosition;
import de.alshikh.haw.tron.client.models.game.data.datatypes.Direction;
import de.alshikh.haw.tron.client.views.view_library.Coordinate;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Bike {
    Direction movingDirection;
    Direction steeringDirection;
    List<Coordinate> coordinates = new ArrayList<>();
    Color color;

    public Bike(BikeStartingPosition startPosition, Color color) {
        this.coordinates.add(startPosition.getCoordinate());
        this.color = color;
        this.steeringDirection = this.movingDirection = startPosition.getMovingDirection();
    }

    public Bike(Color color) {
        this.color = color;
    }

    public void steer(Direction newDirection) {
        this.steeringDirection = newDirection;
    }

    public void move() {
        if (steeringDirection.isAllowed(movingDirection))
            this.movingDirection = Direction.valueOf(steeringDirection.name());
        coordinates.add(movingDirection.calculateNewPosition(getPosition()));
    }

    public void move(int x, int y) {
        coordinates.add(new Coordinate(x, y));
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public Coordinate getPosition() {
        return coordinates.get(coordinates.size() - 1);
    }

    public List<Coordinate> getTrail() {
        List<Coordinate> trail = new ArrayList<>(coordinates);
        trail.remove(getPosition());
        return trail;
    }

    public Direction getMovingDirection() {
        return movingDirection;
    }

    public Color getColor() {
        return color;
    }
}
