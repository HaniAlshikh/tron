package de.alshikh.haw.tron.client.models.game.data.datatypes;

import de.alshikh.haw.tron.client.models.game.data.exceptions.GameFullException;
import de.alshikh.haw.tron.client.views.view_library.Coordinate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public enum BikeStartingPosition {
    // only two players are supported for now
    LEFT(Direction.RIGHT), RIGHT(Direction.LEFT);

    boolean taken;
    int ROWS;
    int COLUMNS;
    Direction movingDirection;

    BikeStartingPosition(String configFile, Direction movingDirection) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(configFile));
            // TODO: see if this make sense and deal with the error
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ROWS = Integer.parseInt(prop.getProperty("rows"));
        this.COLUMNS = Integer.parseInt(prop.getProperty("columns"));
        this.movingDirection = movingDirection;
    }

    BikeStartingPosition(Direction movingDirection) {
        this("view.properties", movingDirection);
    }

    public static BikeStartingPosition random() {
        for (BikeStartingPosition value : BikeStartingPosition.values()) {
            if (!value.taken) {
                return value;
            }
        }
        throw new GameFullException();
    }

    public Coordinate getCoordinate() {
        switch (this) {
            case LEFT: if (!isTaken()) { return new Coordinate(COLUMNS / 12, ROWS / 2); }
            case RIGHT: if (!isTaken()) { return new Coordinate( COLUMNS - (COLUMNS / 12), ROWS / 2); }
            default: throw new IllegalArgumentException("Position is already taken");
        }
    }

    public boolean isTaken() {
        return taken;
    }

    public Direction getMovingDirection() {
        return movingDirection;
    }
}
