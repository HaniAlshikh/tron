package de.alshikh.haw.tron.client.models.game.inputhandler;

import de.alshikh.haw.tron.client.models.game.inputhandler.data.datatypes.Controllers;
import de.alshikh.haw.tron.client.models.game.data.datatypes.Direction;
import de.alshikh.haw.tron.client.common.data.entites.Player;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputHandler implements IInputHandler, EventHandler<KeyEvent> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    Player AWSDPlayer;
    Player JIKLPlayer;

    public InputHandler() {}

    public InputHandler(Player... players) {
        try {
            for (int i = 0; i < players.length; i++) {
                Controllers.values()[i].setPlayer(players[i]);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("too many players: "
                    + "available controllers: " +  Controllers.values().length
                    + "Number of players: " + players.length);
        }

        AWSDPlayer = Controllers.AWSD.getPlayer();
        JIKLPlayer = Controllers.AWSD.getPlayer();
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        log.debug("pressed key: " + code);

        handleAWSD(code);
        handleJIKL(code);

        // if no critical key is pressed, we do not need to do anything to the
        // velocity and direction of the LightCycles.
        keyEvent.consume();
    }

    private void handleAWSD(KeyCode keyCode) {
        switch (keyCode) {
            case A: AWSDPlayer.getBike().steer(Direction.LEFT); break;
            case D: AWSDPlayer.getBike().steer(Direction.RIGHT); break;
            case W: AWSDPlayer.getBike().steer(Direction.UP); break;
            case S: AWSDPlayer.getBike().steer(Direction.DOWN); break;
        }
    }

    private void handleJIKL(KeyCode keyCode) {
        switch (keyCode) {
            case J: JIKLPlayer.getBike().steer(Direction.LEFT); break;
            case L: JIKLPlayer.getBike().steer(Direction.RIGHT); break;
            case I: JIKLPlayer.getBike().steer(Direction.UP); break;
            case K: JIKLPlayer.getBike().steer(Direction.DOWN); break;
        }
    }

    @Override
    public Player getAWSDPlayer() {
        return AWSDPlayer;
    }

    @Override
    public void setAWSDPlayer(Player AWSDPlayer) {
        this.AWSDPlayer = AWSDPlayer;
    }

    @Override
    public Player getJIKLPlayer() {
        return JIKLPlayer;
    }

    @Override
    public void setJIKLPlayer(Player JIKLPlayer) {
        this.JIKLPlayer = JIKLPlayer;
    }

    @Override
    public EventHandler<KeyEvent> getHandler() {
        return this;
    }
}
