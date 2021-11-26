package de.alshikh.haw.tron.client.models;

import de.alshikh.haw.tron.client.models.data.datatypes.GameMode;
import de.alshikh.haw.tron.client.common.data.entites.Player;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.util.Set;

public interface IGameModel {


    void createGame(GameMode gameMode);

    void updateGame();

    EventHandler<KeyEvent> getKeyInputHandler();

    Set<Player> getPlayers();

    Player getWinner();
}
