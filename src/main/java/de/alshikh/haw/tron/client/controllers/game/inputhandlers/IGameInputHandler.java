package de.alshikh.haw.tron.client.controllers.game.inputhandlers;

import de.alshikh.haw.tron.client.models.game.data.entities.Player;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public interface IGameInputHandler {
    Player getAWSDPlayer();

    void setAWSDPlayer(Player AWSDPlayer);

    Player getJIKLPlayer();

    void setJIKLPlayer(Player JIKLPlayer);

    EventHandler<KeyEvent> getHandler();
}
