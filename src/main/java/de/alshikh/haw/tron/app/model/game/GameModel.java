package de.alshikh.haw.tron.app.model.game;

import de.alshikh.haw.tron.app.model.game.data.datatypes.BikeStartingPosition;
import de.alshikh.haw.tron.app.model.game.data.entities.*;
import de.alshikh.haw.tron.app.model.game.util.RandomNameGenerator;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GameModel implements IGameModel {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final List<InvalidationListener> listeners = new ArrayList<>();

    private final IPlayer player;
    private IGame game;

    public GameModel() {
        this.player = new Player(new SimpleStringProperty(RandomNameGenerator.get()));
    }

    @Override
    public void createGame() {
        setupGame(
                new Bike(BikeStartingPosition.LEFT, Color.ORANGE),
                new Bike(BikeStartingPosition.RIGHT, Color.BLUE)
                );
    }

    @Override
    public void joinGame() {
        setupGame(
                new Bike(BikeStartingPosition.RIGHT, Color.BLUE),
                new Bike(BikeStartingPosition.LEFT, Color.ORANGE)
        );
    }

    private void setupGame(IBike playerBike, IBike opponentBike) {
        player.reset(playerBike);
        player.renewUpdate();
        game = new Game();
        game.setPlayer(player);
        game.setOpponent(new Player(new SimpleStringProperty(), opponentBike));
    }

    @Override
    public void updateGameState(IPlayerUpdate opponentUpdate) {
        game.applyOpponentUpdate(opponentUpdate);
        game.movePlayers();
        game.checkForCollision();
        publishGameStateUpdate();
    }

    @Override
    public void createNewPlayerUpdate() {
        player.createUpdate();
        logger.debug("publishing player update: " + player.getUpdate());
        player.getUpdate().publishUpdate();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
        listener.invalidated(this);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void publishGameStateUpdate() {
        listeners.forEach(l -> l.invalidated(this));
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public IGame getGame() {
        return game;
    }
}
