package de.alshikh.haw.tron.client.views;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import java.util.Set;

public interface IGameView {

    void showGame(Set<Player> players);

    void showStartMenu(EventHandler<ActionEvent> startBtnHandler);

    void showWinnerMenu(Player winner, EventHandler<ActionEvent> startBtnHandler);

    void reset();

    Scene getScene();

}
