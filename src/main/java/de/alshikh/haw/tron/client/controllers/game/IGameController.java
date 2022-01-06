package de.alshikh.haw.tron.client.controllers.game;

import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.views.game.IGameView;

import java.util.concurrent.ExecutorService;

public interface IGameController {

    void showStartMenu(String message);

    void createGame();

    void joinGame();

    void cancelGame();

    void startGame(String opponentName);

    void endGame();

    void endGame(String message);

    void closeGame();

    IGameModel getGameModel();

    IGameView getGameView();

    ExecutorService getEs();

    int getNumberOfRetries();
}
