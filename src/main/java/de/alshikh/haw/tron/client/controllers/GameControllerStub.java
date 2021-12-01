package de.alshikh.haw.tron.client.controllers;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.models.IGameModel;
import de.alshikh.haw.tron.client.views.IGameView;

public class GameControllerStub implements IGameController {

    GameController gameController = GameController.getInstance();

    @Override
    public void showStartMenu() {
        gameController.showStartMenu();
    }

    @Override
    public void playGame() {
        gameController.playGame();
    }

    @Override
    public Player getUpdate() {
        return gameController.getUpdate();
    }

    @Override
    public String getTestMessage() {
        return gameController.getTestMessage();
    }

    @Override
    public IGameModel getGameModel() {
        return gameController.getGameModel();
    }

    @Override
    public void setGameModel(IGameModel gameModel) {
        gameController.setGameModel(gameModel);
    }

    @Override
    public IGameView getGameView() {
        return gameController.getGameView();
    }

    @Override
    public void setGameView(IGameView gameView) {
        gameController.setGameView(gameView);
    }
}
