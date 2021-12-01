package de.alshikh.haw.tron.client.views;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public interface IGameView {

    void showGame(Player... players);

    void showStartMenu(EventHandler<ActionEvent> startBtnHandler, EventHandler<ActionEvent> joinBtnHandler);

    void showWaitingMenu();

    void showWinnerMenu(Player winner, EventHandler<ActionEvent> startBtnHandler);

    void reset();

    Scene getScene();

}
