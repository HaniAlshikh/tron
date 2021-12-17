package de.alshikh.haw.tron.client.models.game;

import de.alshikh.haw.tron.client.models.game.data.entities.Game;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public interface IGameModel extends Observable {

    void createGame();

    void joinGame();

    void updateGame();

    EventHandler<KeyEvent> getKeyInputHandler();

    Game getGame();
}
