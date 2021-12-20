package de.alshikh.haw.tron.client;

import de.alshikh.haw.tron.client.controllers.game.GameController;
import de.alshikh.haw.tron.client.controllers.game.IGameController;
import de.alshikh.haw.tron.client.controllers.lobby.ILobbyController;
import de.alshikh.haw.tron.client.controllers.lobby.LobbyController;
import de.alshikh.haw.tron.client.models.game.GameModel;
import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.views.game.GameView;
import de.alshikh.haw.tron.client.views.game.IGameView;
import de.alshikh.haw.tron.client.views.lobby.ILobbyView;
import de.alshikh.haw.tron.client.views.lobby.LobbyView;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import de.alshikh.haw.tron.client.views.view_library.TronView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class TronGame implements Runnable {

    public final static String VIEW_CONFIG_FILE = "view.properties";

    private final ExecutorService es;

    public TronGame(ExecutorService es) {
        this.es = es;
    }

    @Override
    public void run() {
        try {
            ITronView baseView = new TronView(VIEW_CONFIG_FILE);

            ILobbyView lobbyView = new LobbyView(baseView);
            ILobbyController lobbyController = new LobbyController(lobbyView);

            IGameModel gameModel = new GameModel();
            IGameView gameView = new GameView(baseView);

            IGameController gameController = new GameController(gameModel, gameView, lobbyController, es);
            gameController.showStartMenu();


            // configure and show stage
            Stage stage = new Stage();
            stage.setTitle("TRON - Light Cycles");
            stage.setScene(gameView.getScene());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
