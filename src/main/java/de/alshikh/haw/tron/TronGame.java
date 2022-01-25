package de.alshikh.haw.tron;

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
import edu.cads.bai5.vsp.tron.view.TronView;
import javafx.stage.Stage;

import java.io.IOException;

public class TronGame implements Runnable {

    private final ILobbyModel lobbyModel;

    public TronGame(ILobbyModel lobbyModel) {
        this.lobbyModel = lobbyModel;
    }

    @Override
    public void run() {
        try {
            ITronView baseView = new TronView(Config.VIEW_PROP);

            ILobbyView lobbyView = new LobbyView(baseView);
            ILobbyController lobbyController = new LobbyController(lobbyModel, lobbyView);

            IGameModel gameModel = new GameModel();
            IGameView gameView = new GameView(baseView);
            IGameController gameController = new GameController(gameModel, gameView, lobbyController);

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
}
