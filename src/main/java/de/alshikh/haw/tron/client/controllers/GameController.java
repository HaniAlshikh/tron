package de.alshikh.haw.tron.client.controllers;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.models.IGameModel;
import de.alshikh.haw.tron.client.views.IGameView;
import de.alshikh.haw.tron.middleware.rpc.client.IRPCClient;
import de.alshikh.haw.tron.middleware.rpc.server.IRPCServer;
import de.alshikh.haw.tron.middleware.rpc.server.RPCServer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class GameController implements IGameController {

    private IGameModel gameModel;
    private IGameView gameView;

    private IGameController enemyController;

    Timeline gameLoop;

    private static GameController INSTANCE;

    private GameController() {
        this.gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.080), e -> updateGame()));
    }

    public static GameController getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GameController();
        }
        return INSTANCE;
    }

    @Override
    public String getTestMessage() {
        return "Test passed";
    }

    @Override
    public void showStartMenu() {
        gameView.showStartMenu(
                e -> startGame(),
                e -> joinGame()
                );
    }

    private void startGame() {
        System.out.println("Starting a new Game");
        new Thread(() -> {
            try {
                IRPCServer rpcServer1 = new RPCServer(8088);
                rpcServer1.register(IGameController.class, GameControllerStub.class);
                rpcServer1.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        this.enemyController = IRPCClient.getServiceStub(IGameController.class,
                new InetSocketAddress("localhost", 8099));

        gameModel.createGame();
        gameView.reset();
        gameView.showWaitingMenu();
    }

    private void joinGame() {
        new Thread(() -> {
            try {
                IRPCServer rpcServer = new RPCServer(8099);
                rpcServer.register(IGameController.class, GameControllerStub.class);
                rpcServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        this.enemyController = IRPCClient.getServiceStub(IGameController.class,
                new InetSocketAddress("localhost", 8088));

        gameModel.joinGame();
        playGame();
        // TODO: hoster should detect the opponent and start the game
        enemyController.playGame();
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
        Player opponent = enemyController.getUpdate();
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
