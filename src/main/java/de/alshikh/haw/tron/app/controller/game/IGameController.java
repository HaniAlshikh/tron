package de.alshikh.haw.tron.app.controller.game;

import de.alshikh.haw.tron.app.model.game.IGameModel;
import de.alshikh.haw.tron.app.view.game.IGameView;

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
}
