package de.alshikh.haw.tron.app.model.lobby.data.datatypes;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.UUID;

public interface IPlayerUpdateChannel extends InvalidationListener, Observable {
    String getPlayerName();

    UUID getPlayerId();
}
