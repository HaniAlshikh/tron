package de.alshikh.haw.tron.client;

import de.alshikh.haw.tron.client.controllers.game.GameController;
import de.alshikh.haw.tron.client.controllers.game.IGameController;
import de.alshikh.haw.tron.client.models.game.GameModel;
import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.views.game.GameView;
import de.alshikh.haw.tron.client.views.game.IGameView;
import javafx.application.Application;
import javafx.stage.Stage;

public class TronGame extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        IGameModel gameModel = new GameModel();
        IGameView gameView = new GameView();
        IGameController gameController = GameController.getInstance();
        gameController.setGameModel(gameModel);
        gameController.setGameView(gameView);
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
