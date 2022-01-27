package de.alshikh.haw.tron.app.model.game;

import de.alshikh.haw.tron.app.model.game.data.entities.IGame;
import de.alshikh.haw.tron.app.model.game.data.entities.IPlayer;
import de.alshikh.haw.tron.app.model.game.data.entities.IPlayerUpdate;
import javafx.beans.Observable;

public interface IGameModel extends Observable {
    void createGame();

    void joinGame();

    void setOpponentName(String opponentName);

    void updateGameState(IPlayerUpdate opponentUpdate);

    void createNewPlayerUpdate();

    void publishPlayerUpdate();

    void publishGameStateUpdate();

    IPlayer getPlayer();

    IGame getGame();

}
