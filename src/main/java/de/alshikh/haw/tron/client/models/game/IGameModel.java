package de.alshikh.haw.tron.client.models.game;

import de.alshikh.haw.tron.client.models.game.data.entities.Game;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import javafx.beans.Observable;

public interface IGameModel extends Observable {

    void createGame();

    void joinGame();

    void applyOpponentUpdate(PlayerUpdate opponentPlayerUpdate);

    void updateGame();

    Game getGame();

    int comparePlayerVersions();
}
