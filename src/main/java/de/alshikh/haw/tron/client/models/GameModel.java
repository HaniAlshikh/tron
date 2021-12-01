package de.alshikh.haw.tron.client.models;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.models.data.datatypes.SerializableColor;
import de.alshikh.haw.tron.client.models.InputHandler.IInputHandler;
import de.alshikh.haw.tron.client.models.InputHandler.InputHandler;
import de.alshikh.haw.tron.client.models.data.datatypes.BikeStartingPosition;
import de.alshikh.haw.tron.client.models.data.entities.Bike;
import de.alshikh.haw.tron.client.models.data.entities.Game;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class GameModel implements IGameModel {
    Player player;
    Game game;

    IInputHandler inputHandler;

    public GameModel() {
        this.inputHandler = new InputHandler();
    }

    @Override
    public void createGame() {
        this.player = new Player("P1", new Bike(BikeStartingPosition.LEFT, new SerializableColor(Color.RED)));
        prepGame();
    }

    @Override
    public void joinGame() {
        this.player = new Player("P2", new Bike(BikeStartingPosition.RIGHT, new SerializableColor(Color.BLUE)));
        prepGame();
    }

    private void prepGame() {
        this.game = new Game(player);
        inputHandler.setAWSDPlayer(player);
    }

    @Override
    public void updateGame(Player opponent) {
        game.update(opponent);
    }

    @Override
    public EventHandler<KeyEvent> getKeyInputHandler() {
        return inputHandler.getHandler();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Player getWinner() {
        return game.getWinner();
    }

    @Override
    public boolean gameEnded() {
        return game.gameEnded();
    }
}
