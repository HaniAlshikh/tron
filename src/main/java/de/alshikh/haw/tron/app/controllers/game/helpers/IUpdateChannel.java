package de.alshikh.haw.tron.app.controllers.game.helpers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.UUID;

public interface IUpdateChannel extends InvalidationListener, Observable {
    String getName();

    UUID getId();
}
