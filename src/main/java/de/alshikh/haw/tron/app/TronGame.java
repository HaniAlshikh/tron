package de.alshikh.haw.tron.app;

import de.alshikh.haw.tron.app.controllers.game.GameController;
import de.alshikh.haw.tron.app.controllers.game.IGameController;
import de.alshikh.haw.tron.app.controllers.lobby.ILobbyController;
import de.alshikh.haw.tron.app.controllers.lobby.LobbyController;
import de.alshikh.haw.tron.app.models.game.GameModel;
import de.alshikh.haw.tron.app.models.game.IGameModel;
import de.alshikh.haw.tron.app.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.app.views.game.GameView;
import de.alshikh.haw.tron.app.views.game.IGameView;
import de.alshikh.haw.tron.app.views.lobby.ILobbyView;
import de.alshikh.haw.tron.app.views.lobby.LobbyView;
import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TronGame implements Runnable {

    private final CompletableFuture<UUID> id = new CompletableFuture<>();
    private final ITronView baseView;
    private final ILobbyModel lobbyModel;

    public TronGame(ITronView baseView, ILobbyModel lobbyModel) {
        this.baseView = baseView;
        this.lobbyModel = lobbyModel;
    }

    @Override
    public void run() {
        try {
            ILobbyView lobbyView = new LobbyView(baseView);
            ILobbyController lobbyController = new LobbyController(lobbyModel, lobbyView);

            IGameModel gameModel = new GameModel();
            IGameView gameView = new GameView(baseView);
            IGameController gameController = new GameController(gameModel, gameView, lobbyController);

            id.complete(gameModel.getPlayer().getId());
            gameController.showStartMenu("Ready?");

            Stage stage = new Stage();
            stage.setTitle("TRON - Light Cycles");
            stage.setScene(gameView.getScene());
            stage.show();

            stage.setOnCloseRequest(e -> gameController.closeGame());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ILobbyModel getLobbyModel() {
        return lobbyModel;
    }

    public UUID getId() throws ExecutionException, InterruptedException {
        return id.get();
    }
}
