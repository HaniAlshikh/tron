package de.alshikh.haw.tron.client.controllers;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.models.IGameModel;
import de.alshikh.haw.tron.client.views.IGameView;

public interface IGameController {

    void showStartMenu();

    void playGame();

    Player getUpdate();

    String getTestMessage();

    IGameModel getGameModel();

    void setGameModel(IGameModel gameModel);

    IGameView getGameView();

    void setGameView(IGameView gameView);
}
