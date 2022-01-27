package de.alshikh.haw.tron.app.controller.game.inputhandlers;

import de.alshikh.haw.tron.app.model.game.data.datatypes.Direction;
import de.alshikh.haw.tron.app.model.game.data.entities.IPlayer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameInputHandler implements IGameInputHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final IPlayer player;

    public GameInputHandler(IPlayer player) {
        this.player = player;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        log.debug("pressed key: " + code);
        handleWASD(code);
        keyEvent.consume();
    }

    private void handleWASD(KeyCode keyCode) {
        switch (keyCode) {
            case A: player.getBike().steer(Direction.LEFT); break;
            case D: player.getBike().steer(Direction.RIGHT); break;
            case W: player.getBike().steer(Direction.UP); break;
            case S: player.getBike().steer(Direction.DOWN); break;
        }
    }
}
