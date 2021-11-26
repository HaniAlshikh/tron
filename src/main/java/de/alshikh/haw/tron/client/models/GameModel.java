package de.alshikh.haw.tron.client.models;

import de.alshikh.haw.tron.client.models.InputHandler.IInputHandler;
import de.alshikh.haw.tron.client.models.data.datatypes.BikeStartingPosition;
import de.alshikh.haw.tron.client.models.InputHandler.InputHandler;
import de.alshikh.haw.tron.client.models.data.datatypes.GameMode;
import de.alshikh.haw.tron.client.models.data.entities.Bike;
import de.alshikh.haw.tron.client.models.data.entities.Game;
import de.alshikh.haw.tron.client.common.data.entites.Player;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.Set;

public class GameModel implements IGameModel {
    GameMode gameMode;

    Player p1;
    Player p2;
    Game game;

    IInputHandler inputHandler;

    public GameModel() {
        this.inputHandler = new InputHandler();
    }

    @Override
    public void createGame(GameMode gameMode) {
        switch (gameMode) {
            case VS: game = createVSGame(); break;
            default: throw new EnumConstantNotPresentException(gameMode.getClass(), gameMode.name());
        }
        this.gameMode = gameMode;
    }

    @Override
    public void updateGame() {
        game.update();
    }

    @Override
    public EventHandler<KeyEvent> getKeyInputHandler() {
        return inputHandler.getHandler();
    }

    @Override
    public Set<Player> getPlayers() {
        return game.getPlayers();
    }

    @Override
    public Player getWinner() {
        return game.getWinner();
    }

    private Game createVSGame() {
        this.p1 = new Player("P1", new Bike(BikeStartingPosition.LEFT, Color.RED));
        this.p2 = new Player("P2", new Bike(BikeStartingPosition.RIGHT, Color.BLUE));
        inputHandler.setAWSDPlayer(p1);
        inputHandler.setJIKLPlayer(p2);
        return new Game(p1, p2);
    }
}
