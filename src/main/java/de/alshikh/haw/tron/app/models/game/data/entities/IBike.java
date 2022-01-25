package de.alshikh.haw.tron.app.models.game.data.entities;

import de.alshikh.haw.tron.app.models.game.data.datatypes.Direction;
import edu.cads.bai5.vsp.tron.view.Coordinate;
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
