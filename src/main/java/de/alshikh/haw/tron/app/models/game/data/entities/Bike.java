package de.alshikh.haw.tron.app.models.game.data.entities;

import de.alshikh.haw.tron.app.models.game.data.datatypes.BikeStartingPosition;
import de.alshikh.haw.tron.app.models.game.data.datatypes.Direction;
import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Bike implements IBike {

    private Direction movingDirection;
    private Direction steeringDirection;

    private final List<Coordinate> coordinates = new ArrayList<>();
    private final Color color;

    public Bike(BikeStartingPosition startPosition, Color color) {
        this.coordinates.add(startPosition.getCoordinate());
        this.color = color;
        this.steeringDirection = this.movingDirection = startPosition.getMovingDirection();
    }

    @Override
    public void steer(Direction newDirection) {
        this.steeringDirection = newDirection;
    }

    @Override
    public void lockDirection() {
        if (steeringDirection.isAllowed(movingDirection))
            this.movingDirection = Direction.valueOf(steeringDirection.name());
    }

    @Override
    public void move() {
        coordinates.add(movingDirection.calculateNewPosition(getPosition()));
    }

    @Override
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    @Override
    public Coordinate getPosition() {
        return coordinates.get(coordinates.size() - 1);
    }

    @Override
    public List<Coordinate> getTrail() {
        List<Coordinate> trail = new ArrayList<>(coordinates);
        trail.remove(getPosition());
        return trail;
    }

    @Override
    public Direction getMovingDirection() {
        return movingDirection;
    }

    @Override
    public void setMovingDirection(Direction movingDirection) {
        this.movingDirection = movingDirection;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
