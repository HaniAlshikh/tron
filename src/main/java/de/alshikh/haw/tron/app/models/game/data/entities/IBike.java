package de.alshikh.haw.tron.app.models.game.data.entities;

import de.alshikh.haw.tron.app.models.game.data.datatypes.Direction;
import de.alshikh.haw.tron.app.views.view_library.Coordinate;
import javafx.scene.paint.Color;

import java.util.List;

public interface IBike {
    void steer(Direction newDirection);

    void lockDirection();

    void move();

    List<Coordinate> getCoordinates();

    Coordinate getPosition();

    List<Coordinate> getTrail();

    Direction getMovingDirection();

    void setMovingDirection(Direction movingDirection);

    Color getColor();
}
