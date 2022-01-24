package de.alshikh.haw.tron.app.controllers.game.helpers;

import javafx.beans.InvalidationListener;

public interface IGameUpdater extends InvalidationListener {
    void start();

    void updateGame();

    void stop();
}
