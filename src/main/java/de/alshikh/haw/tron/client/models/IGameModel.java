package de.alshikh.haw.tron.client.models;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public interface IGameModel {

    void createGame();

    void joinGame();

    void updateGame(Player opponent);

    EventHandler<KeyEvent> getKeyInputHandler();

    Player getPlayer();

    Player getWinner();

    boolean gameEnded();
}
