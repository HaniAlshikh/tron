package de.alshikh.haw.tron.app.models.game;

import de.alshikh.haw.tron.app.models.game.data.entities.IGame;
import de.alshikh.haw.tron.app.models.game.data.entities.IPlayer;
import de.alshikh.haw.tron.app.models.game.data.entities.IPlayerUpdate;
import javafx.beans.Observable;

public interface IGameModel extends Observable {
    void createGame();

    void joinGame();

    void updateGameState(IPlayerUpdate opponentUpdate);

    void createNewPlayerUpdate();

    void publishGameStateUpdate();

    IPlayer getPlayer();

    IGame getGame();

}
