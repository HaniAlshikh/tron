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

public class TronGame {

    public final static String VIEW_CONFIG_FILE = "view.properties";

    public TronGame() {
        try {
            ITronView view = new TronView(VIEW_CONFIG_FILE);

            ILobbyView lobbyView = new LobbyView(view);
            ILobbyController lobbyController = new LobbyController(lobbyView);

            IGameModel gameModel = new GameModel();
            IGameView gameView = new GameView(view);
            IGameController gameController = new GameController(gameModel, gameView, lobbyController);


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
