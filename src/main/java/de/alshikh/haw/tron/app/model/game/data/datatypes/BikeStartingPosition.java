package de.alshikh.haw.tron.app.model.game.data.datatypes;

import de.alshikh.haw.tron.manager.Config;
import edu.cads.bai5.vsp.tron.view.Coordinate;

public enum BikeStartingPosition {
    LEFT(Direction.RIGHT), RIGHT(Direction.LEFT);

    private final Direction movingDirection;

    BikeStartingPosition(Direction movingDirection) {
        this.movingDirection = movingDirection;
    }

    public Coordinate getCoordinate() {
        switch (this) {
            case LEFT: return new Coordinate(2, Config.ROWS / 2);
            case RIGHT: return new Coordinate(Config.COLUMNS - 2, Config.ROWS / 2);
            default: throw new IllegalArgumentException("Position is not available");
        }
    }

    public Direction getMovingDirection() {
        return movingDirection;
    }
}
