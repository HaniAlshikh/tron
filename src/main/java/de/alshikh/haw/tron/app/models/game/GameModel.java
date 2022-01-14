package de.alshikh.haw.tron.app.models.game;

import de.alshikh.haw.tron.app.models.game.data.datatypes.BikeStartingPosition;
import de.alshikh.haw.tron.app.models.game.data.entities.Bike;
import de.alshikh.haw.tron.app.models.game.data.entities.Game;
import de.alshikh.haw.tron.app.models.game.data.entities.Player;
import de.alshikh.haw.tron.app.models.game.data.entities.PlayerUpdate;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GameModel implements IGameModel {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final List<InvalidationListener> listeners = new ArrayList<>();

    private final Player player;
    private Game game;

    public GameModel() {
        this.player = new Player(); // multiple games one player (id)
    }

    // TODO: find a better way (player factory?)
    @Override
    public void createGame(StringProperty playerName) {
        player.reset(playerName, new Bike(BikeStartingPosition.LEFT, Color.ORANGE));
        player.resetUpdate(); // to initial position
        game = new Game();
        game.setPlayer(player);
        game.setOpponent(new Player(new SimpleStringProperty(), new Bike(BikeStartingPosition.RIGHT, Color.BLUE)));
    }

    @Override
    public void joinGame(StringProperty playerName) {
        player.reset(playerName, new Bike(BikeStartingPosition.RIGHT, Color.BLUE));
        player.resetUpdate(); // to initial position
        game = new Game();
        game.setPlayer(player);
        game.setOpponent(new Player(new SimpleStringProperty(), new Bike(BikeStartingPosition.LEFT, Color.ORANGE)));
    }

    @Override
    public void updateGameState(PlayerUpdate opponentUpdate) {
        game.applyOpponentUpdate(opponentUpdate);
        game.movePlayers();
        game.checkForCollision();
        game.checkForBreak();
        publishGameStateUpdate();
    }

    @Override
    public void createNewPlayerUpdate() {
        player.createUpdate();
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
    public Player getPlayer() {
        return player;
    }

    @Override
    public Game getGame() {
        return game;
    }
}
