package de.alshikh.haw.tron.client.models.game;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.models.game.data.datatypes.BikeStartingPosition;
import de.alshikh.haw.tron.client.models.game.data.entities.Bike;
import de.alshikh.haw.tron.client.models.game.data.entities.Game;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import javafx.beans.InvalidationListener;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GameModel implements IGameModel {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final List<InvalidationListener> listeners = new ArrayList<>();

    private Game game;

    public GameModel() {}

    // TODO: find a better way (player factory?)
    @Override
    public void createGame() {
        prepGame(new Player("P1", new Bike(BikeStartingPosition.LEFT, Color.RED)),
                new Player("P2", new Bike(BikeStartingPosition.RIGHT, Color.BLUE)));
    }

    @Override
    public void joinGame() {
        prepGame(new Player("P2", new Bike(BikeStartingPosition.RIGHT, Color.BLUE)),
                new Player("P1", new Bike(BikeStartingPosition.LEFT, Color.RED)));
    }

    @Override
    public void applyOpponentUpdate(PlayerUpdate opponentPlayerUpdate) {
        game.getOpponent().applyUpdate(opponentPlayerUpdate);
    }

    @Override
    public int comparePlayerVersions() {
        // TODO: maybe allow version catch up
        logger.debug("Player version: " + game.getPlayer().getVersion() + " " + game.getOpponent().getVersion() + " :Opponent version");
        // TODO: a version mismatch of 1 point is expected as one of the players will call the updateGame method first
        //  this should be solved by threading
        return game.getPlayer().getVersion() == game.getOpponent().getVersion() ||
                game.getPlayer().getVersion() - 1 == game.getOpponent().getVersion() ||
                game.getPlayer().getVersion() == game.getOpponent().getVersion() - 1 ? 0 : -1;
    }

    @Override
    public void updateGame() {
        game.update();
        listeners.forEach(l -> l.invalidated(this));
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

    private void prepGame(Player player, Player opponent) {
        this.game = new Game();
        this.game.setPlayer(player);
        this.game.setOpponent(opponent);
    }
}
