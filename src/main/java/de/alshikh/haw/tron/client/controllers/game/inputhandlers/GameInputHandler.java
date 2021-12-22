package de.alshikh.haw.tron.client.controllers.game.inputhandlers;

import de.alshikh.haw.tron.client.models.game.data.datatypes.Direction;
import de.alshikh.haw.tron.client.models.game.data.entities.Player;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameInputHandler implements IGameInputHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Object steeringLock;
    private final Player player;

    public GameInputHandler(Object steeringLock, Player player) {
        this.steeringLock = steeringLock;
        this.player = player;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        log.debug("pressed key: " + code);

        synchronized (steeringLock) {
            handleAWSD(code);
        }

        // if no critical key is pressed, we do not need to do anything to the
        // velocity and direction of the LightCycles.
        keyEvent.consume();
    }

    private void handleAWSD(KeyCode keyCode) {
        switch (keyCode) {
            case A: player.getBike().steer(Direction.LEFT); break;
            case D: player.getBike().steer(Direction.RIGHT); break;
            case W: player.getBike().steer(Direction.UP); break;
            case S: player.getBike().steer(Direction.DOWN); break;
        }
    }

    @Override
    public EventHandler<KeyEvent> getHandler() {
        return this;
    }
}
