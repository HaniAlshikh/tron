package de.alshikh.haw.tron.client.controllers;

import de.alshikh.haw.tron.client.models.IGameModel;
import de.alshikh.haw.tron.client.models.data.datatypes.GameMode;
import de.alshikh.haw.tron.client.views.IGameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameController implements IGameController {

    private IGameModel gameModel;
    private IGameView gameView;

    Timeline gameLoop;

    public GameController(IGameModel gameModel, IGameView gameView) {
        this.gameView = gameView;
        this.gameModel = gameModel;
        this.gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.080), e -> updateGame()));
    }

    @Override
    public void showStartMenu() {
        gameView.showStartMenu(e -> startGame(GameMode.VS));
    }

    private void startGame(GameMode gameMode) {
        gameModel.createGame(gameMode);
        gameView.reset();
        gameView.getScene().setOnKeyPressed(gameModel.getKeyInputHandler());
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void updateGame() {
        gameModel.updateGame();
        gameView.showGame(gameModel.getPlayers());
        if (gameModel.getWinner() != null || gameModel.getPlayers().isEmpty())
            endGame();
    }

    private void endGame() {
        gameLoop.stop();
        gameView.showWinnerMenu(gameModel.getWinner(), e -> startGame(GameMode.VS));
    }
}
