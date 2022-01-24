package de.alshikh.haw.tron.app.controllers.game.helpers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.UUID;

public interface IPlayerUpdateChannel extends InvalidationListener, Observable {
    String getPlayerName();

    UUID getPlayerId();
}
