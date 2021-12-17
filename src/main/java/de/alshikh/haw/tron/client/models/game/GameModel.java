package de.alshikh.haw.tron.client.models.game;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.models.game.data.datatypes.BikeStartingPosition;
import de.alshikh.haw.tron.client.models.game.data.entities.Bike;
import de.alshikh.haw.tron.client.models.game.data.entities.Game;
import de.alshikh.haw.tron.client.models.game.inputhandler.IInputHandler;
import de.alshikh.haw.tron.client.models.game.inputhandler.InputHandler;
import javafx.beans.InvalidationListener;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameModel implements IGameModel {
    private final List<InvalidationListener> listeners = new ArrayList<>();

    private Game game;

    IInputHandler inputHandler;

    public GameModel() {
        this.inputHandler = new InputHandler();
    }

    @Override
    public void createGame() {
        prepGame("P1", new Bike(BikeStartingPosition.LEFT, Color.RED));
    }

    @Override
    public void joinGame() {
        prepGame("P2", new Bike(BikeStartingPosition.RIGHT, Color.BLUE));
    }

    @Override
    public void updateGame() {
        game.update();
        listeners.forEach(l -> l.invalidated(this));
    }

    @Override
    public EventHandler<KeyEvent> getKeyInputHandler() {
        return inputHandler.getHandler();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Game getGame() {
        return game;
    }

    private void prepGame(String name, Bike bike) {
        Player player = new Player(name, bike);
        this.game = new Game();
        this.game.setPlayer(player);
        inputHandler.setAWSDPlayer(player);
    }
}
