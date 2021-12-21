package de.alshikh.haw.tron.client.models.game;

import de.alshikh.haw.tron.client.models.game.data.entities.Game;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;

public interface IGameModel extends Observable {

    // TODO: find a better way (player factory?)
    void createGame(StringProperty playerName);

    void joinGame(StringProperty playerName);

    void updateGameState(PlayerUpdate opponentUpdate);

    void publishUpdate();

    Game getGame();
}
