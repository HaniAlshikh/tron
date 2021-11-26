package de.alshikh.haw.tron.client;

import de.alshikh.haw.tron.client.controllers.GameController;
import de.alshikh.haw.tron.client.controllers.IGameController;
import de.alshikh.haw.tron.client.models.GameModel;
import de.alshikh.haw.tron.client.models.IGameModel;
import de.alshikh.haw.tron.client.views.GameView;
import de.alshikh.haw.tron.client.views.IGameView;
import javafx.application.Application;
import javafx.stage.Stage;

public class TronGame extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        IGameModel gameModel = new GameModel();
        IGameView gameView = new GameView();
        IGameController gameController = new GameController(gameModel, gameView);
        gameController.showStartMenu();

        // configure and show stage
        stage.setTitle("TRON - Light Cycles");
        stage.setScene(gameView.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
