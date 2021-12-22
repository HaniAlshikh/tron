package de.alshikh.haw.tron.client.controllers.game;

import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import de.alshikh.haw.tron.client.views.game.IGameView;
import javafx.beans.property.StringProperty;

import java.util.concurrent.ExecutorService;

public interface IGameController {

    void endGame();

    void endGame(String message);

    void closeGame();

    void showStartMenu(String message);

    void joinGame();

    void admit(IGameController opponentController);

    void startGame();

    PlayerUpdate getPlayerUpdate();

    StringProperty playerNameProperty();

    IGameModel getGameModel();

    IGameView getGameView();

    ExecutorService getEs();
}
