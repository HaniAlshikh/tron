package de.alshikh.haw.tron.client.controllers.game;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.controllers.lobby.ILobbyController;
import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.views.game.IGameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameController implements IGameController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IGameModel gameModel;
    private final IGameView gameView;
    private final ILobbyController lobbyController;

    private IGameController opponentController;

    private final Timeline gameLoop;

    public GameController(IGameModel gameModel, IGameView gameView, ILobbyController lobbyController) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.lobbyController = lobbyController;
        this.gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.2), e -> updateGame()));
    }

    @Override
    public void showStartMenu() {
        gameView.showStartMenu(
                e -> setupGame(),
                e -> lobbyController.showRoomsMenu(this)
        );
    }

    private void setupGame() {
        logger.info("Starting a new Game");
        gameModel.createGame();
        lobbyController.createRoom(gameModel.getPlayer().getName(), this);
        gameView.reset();
        gameView.showWaitingMenu();
    }

    @Override
    public void admit(IGameController opponentController) {
        this.opponentController = opponentController;
    }

    @Override
    public void joinGame(IGameController opponentController) {
        this.opponentController = opponentController;
        gameModel.joinGame();
    }

    @Override
    public void startGame() {
        gameView.reset();
        gameView.getScene().setOnKeyPressed(gameModel.getKeyInputHandler());
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    @Override
    public Player getUpdate() {
        return gameModel.getPlayer();
    }

    private void updateGame() {
        Player opponent = opponentController.getUpdate();
        gameModel.updateGame(opponent);
        gameView.showGame(gameModel.getPlayer(), opponent);
        if (gameModel.getWinner() != null || gameModel.gameEnded())
            endGame();
    }

    private void endGame() {
        gameLoop.stop();
        gameView.showWinnerMenu(gameModel.getWinner(), e -> setupGame());
    }

    @Override
    public IGameModel getGameModel() {
        return gameModel;
    }

    @Override
    public IGameView getGameView() {
        return gameView;
    }
}
