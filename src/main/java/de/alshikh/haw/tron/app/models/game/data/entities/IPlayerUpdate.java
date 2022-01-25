package de.alshikh.haw.tron.app.models.game.data.entities;

import de.alshikh.haw.tron.app.models.game.data.datatypes.Direction;
import javafx.beans.Observable;

public interface IPlayerUpdate extends Observable {
    void setValues(Direction movingDirection, boolean dead, int version);

    void renew(Direction movingDirection, boolean dead);

    IPlayerUpdate copy();

    void publishUpdate();

    void publishUpdate(IPlayerUpdate playerUpdate);

    Direction getMovingDirection();

    boolean isDead();

    int getVersion();
}
