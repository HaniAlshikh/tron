package de.alshikh.haw.tron.app.controller.game;

import de.alshikh.haw.tron.app.controller.game.helpers.GameUpdater;
import de.alshikh.haw.tron.app.controller.game.helpers.IGameUpdater;
import de.alshikh.haw.tron.app.controller.game.helpers.IPlayerUpdateChannel;
import de.alshikh.haw.tron.app.controller.game.helpers.PlayerUpdateChannel;
import de.alshikh.haw.tron.app.controller.game.inputhandlers.GameInputHandler;
import de.alshikh.haw.tron.app.controller.lobby.ILobbyController;
import de.alshikh.haw.tron.app.model.game.IGameModel;
import de.alshikh.haw.tron.app.view.game.IGameView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameController implements IGameController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final IGameModel gameModel;
    private final IGameView gameView;
    private final ILobbyController lobbyController;

    private final IGameUpdater gameUpdater;
    private final IPlayerUpdateChannel playerUpdateChannel;

    public GameController(IGameModel gameModel, IGameView gameView, ILobbyController lobbyController) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.lobbyController = lobbyController;

        this.gameUpdater = new GameUpdater(this);
        this.playerUpdateChannel = new PlayerUpdateChannel(gameModel.getPlayer(), gameUpdater, this::startGame);
        this.gameModel.addListener(gameUpdater); // on model update update the view
    }

    @Override
    public void showStartMenu(String message) {
        gameView.showStartMenu(
                gameModel.getPlayer().nameProperty(),
                message,
                e -> createGame(),
                e -> joinGame()
        );
    }

    @Override
    public void createGame() {
        logger.info("Starting a new Game as host");
        gameModel.createGame();
        lobbyController.createRoom(playerUpdateChannel);
        gameView.showWaitingMenu(e -> cancelGame());
    }

    @Override
    public void joinGame() {
        logger.info("Starting a new Game as player");
        gameModel.joinGame();
        lobbyController.showRoomsMenu(playerUpdateChannel, e -> cancelGame());
    }

    @Override
    public void cancelGame() {
        lobbyController.removeRoom(gameModel.getPlayer().getId());
        gameView.reset();
        showStartMenu("Ready?");
    }

    @Override
    public void startGame(String opponentName) {
        lobbyController.removeRoom(gameModel.getPlayer().getId());
        gameModel.getGame().getOpponent().nameProperty().setValue(opponentName);
        gameView.reset();
        gameView.getScene().setOnKeyPressed(new GameInputHandler(gameModel.getPlayer()));
        gameUpdater.start();
    }

    @Override
    public void endGame() {
        endGame(gameModel.getGame().getWinner() == null ? "It's a tie" : gameModel.getGame().getWinner() + " won");
    }

    @Override
    public void endGame(String message) {
        gameUpdater.stop();
        gameView.highlightCell(
                gameModel.getGame().getPlayer().getBike().getPosition(),
                gameModel.getGame().getOpponent().getBike().getPosition());
        showStartMenu(message);
    }

    @Override
    public void closeGame() {
        gameUpdater.stop();
        lobbyController.removeRoom(gameModel.getPlayer().getId());
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
