package de.alshikh.haw.tron.client.controllers.game;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.views.game.IGameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameController implements IGameController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private IGameModel gameModel;
    private IGameView gameView;

    private IGameController opponentController;

    private final Timeline gameLoop;

    private static GameController INSTANCE;
    public static GameController getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GameController();
        }
        return INSTANCE;
    }

    private GameController() {
        this.gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.080), e -> updateGame()));
    }

    @Override
    public void showStartMenu() {
        gameView.showStartMenu(
                e -> startGame(),
                e -> joinGame()
        );
    }

    private void startGame() {
        log.info("Starting a new Game");
        gameModel.createGame();
        gameView.reset();
        gameView.showWaitingMenu();
    }

    private void joinGame() {
        gameModel.joinGame();
        playGame();
        // TODO: hoster should detect the opponent and start the game
        opponentController.playGame();
    }

    @Override
    public void playGame() {
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
        gameView.showWinnerMenu(gameModel.getWinner(), e -> startGame());
    }

    @Override
    public IGameModel getGameModel() {
        return gameModel;
    }

    @Override
    public void setGameModel(IGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public IGameView getGameView() {
        return gameView;
    }

    @Override
    public void setGameView(IGameView gameView) {
        this.gameView = gameView;
    }
}
