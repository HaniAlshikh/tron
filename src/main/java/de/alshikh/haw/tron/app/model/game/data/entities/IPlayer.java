package de.alshikh.haw.tron.app.model.game.data.entities;

import javafx.beans.property.StringProperty;

import java.util.UUID;

public interface IPlayer {
    void reset(IBike bike);

    void move();

    void createUpdate();

    void renewUpdate();

    void applyUpdate(IPlayerUpdate update);

    void publishUpdate();

    void publishPreviousUpdate();

    void die();

    UUID getId();

    String getName();

    StringProperty nameProperty();

    IBike getBike();

    IPlayerUpdate getUpdate();

    int getUpdateVersion();

    boolean isDead();
}
