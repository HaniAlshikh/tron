package de.alshikh.haw.tron.client.controllers.game;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.views.game.IGameView;

public interface IGameController {

    void showStartMenu();

    void playGame();

    Player getUpdate();

    IGameModel getGameModel();

    void setGameModel(IGameModel gameModel);

    IGameView getGameView();

    void setGameView(IGameView gameView);
}
