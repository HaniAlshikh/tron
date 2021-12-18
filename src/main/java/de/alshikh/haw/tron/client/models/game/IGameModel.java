package de.alshikh.haw.tron.client.models.game;

import de.alshikh.haw.tron.client.models.game.data.entities.Game;
import javafx.beans.Observable;

public interface IGameModel extends Observable {

    void createGame();

    void joinGame();

    void updateGame();

    Game getGame();
}
