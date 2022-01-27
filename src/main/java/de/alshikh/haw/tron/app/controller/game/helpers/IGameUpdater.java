package de.alshikh.haw.tron.app.controller.game.helpers;

import javafx.beans.InvalidationListener;

public interface IGameUpdater extends InvalidationListener {
    void start();

    void updateGame();

    void stop();
}
