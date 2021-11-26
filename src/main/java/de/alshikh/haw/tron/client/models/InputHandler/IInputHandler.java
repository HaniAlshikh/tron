package de.alshikh.haw.tron.client.models.InputHandler;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public interface IInputHandler {
    Player getAWSDPlayer();

    void setAWSDPlayer(Player AWSDPlayer);

    Player getJIKLPlayer();

    void setJIKLPlayer(Player JIKLPlayer);

    EventHandler<KeyEvent> getHandler();
}
