package de.alshikh.haw.tron.app.controllers.game;

import de.alshikh.haw.tron.app.models.game.IGameModel;
import de.alshikh.haw.tron.app.views.game.IGameView;

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
