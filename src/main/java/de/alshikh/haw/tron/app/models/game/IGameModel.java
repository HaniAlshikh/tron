package de.alshikh.haw.tron.app.models.game;

import de.alshikh.haw.tron.app.models.game.data.entities.Game;
import de.alshikh.haw.tron.app.models.game.data.entities.Player;
import de.alshikh.haw.tron.app.models.game.data.entities.PlayerUpdate;
import javafx.beans.Observable;

public interface IGameModel extends Observable {
    void createGame();

    void joinGame();

    void updateGameState(PlayerUpdate opponentUpdate);

    void createNewPlayerUpdate();

    void publishGameStateUpdate();

    Player getPlayer();

    Game getGame();

}
