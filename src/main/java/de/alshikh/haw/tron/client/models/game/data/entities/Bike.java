package de.alshikh.haw.tron.client.models.game.data.entities;

import de.alshikh.haw.tron.client.models.game.data.datatypes.BikeStartingPosition;
import de.alshikh.haw.tron.client.models.game.data.datatypes.Direction;
import de.alshikh.haw.tron.client.views.view_library.Coordinate;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Bike {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    Color color;
    List<Coordinate> coordinates = new ArrayList<>();

    Direction movingDirection;
    Direction steeringDirection;
    private final Object steerLock = new Object();

    public Bike(BikeStartingPosition startPosition, Color color) {
        this.coordinates.add(startPosition.getCoordinate());
        this.color = color;
        this.steeringDirection = this.movingDirection = startPosition.getMovingDirection();
    }

    public void steer(Direction newDirection) {
        synchronized (steerLock) {
            this.steeringDirection = newDirection;
        }
    }

    public void lockDirection() {
        synchronized (steerLock) {
            if (steeringDirection.isAllowed(movingDirection))
                this.movingDirection = Direction.valueOf(steeringDirection.name());
        }
    }

    public void move() {
        log.debug("Moving " + movingDirection);
        coordinates.add(movingDirection.calculateNewPosition(getPosition()));
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

    public void setMovingDirection(Direction movingDirection) {
        this.movingDirection = movingDirection;
    }

    public Direction getSteeringDirection() {
        return steeringDirection;
    }

    public void setSteeringDirection(Direction steeringDirection) {
        this.steeringDirection = steeringDirection;
    }

    public Color getColor() {
        return color;
    }
}
