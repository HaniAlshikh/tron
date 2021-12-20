package de.alshikh.haw.tron.client.views.game;

import de.alshikh.haw.tron.client.models.game.data.entities.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public interface IGameView {

    void showGame(Game game);

    void showStartMenu(EventHandler<ActionEvent> startBtnHandler, EventHandler<ActionEvent> joinBtnHandler);

    void showWaitingMenu();

    void showWinnerMenu(String message, EventHandler<ActionEvent> startBtnHandler);

    void reset();

    Scene getScene();

}
