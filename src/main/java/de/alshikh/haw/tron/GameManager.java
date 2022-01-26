package de.alshikh.haw.tron;

import de.alshikh.haw.tron.app.TronGame;
import de.alshikh.haw.tron.app.model.lobby.ILobbyModel;
import de.alshikh.haw.tron.app.model.lobby.LobbyModel;
import de.alshikh.haw.tron.manager.overlays.ManagerMenu;
import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.TronView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class GameManager extends Application {

    // TODO: limit to only 80% of machine resources
    //  is this enough?
    //  what to do if all threads are busy?
    //  - don't allow new games
    //  - allow new games and accept the lag
    //      (for example displaying the current game state in the 7th game need to wait for the other 6 games)
    //  implement ExecutorServiceManager
    //public final static double RESOURCES_LIMIT = 0.8;
    //ExecutorService es = Executors.newFixedThreadPool((int)
    //        (Runtime.getRuntime().availableProcessors() * RESOURCES_LIMIT));

    // in the standalone version the model is shared as a the source of truth for rooms
    // while in the distributed version the directory server is the source of truth
    ILobbyModel singletonLobbyModel = new LobbyModel();

    @Override
    public void start(Stage stage) throws Exception {
        ITronView managerView = new TronView(Config.MANAGER_PROP);

        ManagerMenu managerMenu = new ManagerMenu(Config.MENU_CSS);
        managerMenu.getBtnNew().setOnAction(e -> Platform.runLater(newTronGame()));
        managerView.registerOverlay("managerMenu", managerMenu);

        managerView.init();
        managerView.showOverlay("managerMenu");

        stage.setTitle("TRON Game Manager");
        stage.setScene(managerView.getScene());
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.show();
        stage.setX((screenBounds.getWidth() - managerView.getScene().getWidth()) / 2);
        stage.setY(0);

        stage.setOnCloseRequest(e -> { Platform.exit(); System.exit(0); });
    }

    public Runnable newTronGame()  {
        try {
            ITronView gameView = new TronView(Config.VIEW_PROP);
            return Config.DISTRIBUTED ?
                    new DistributedTronGame(gameView) :
                    new TronGame(gameView, singletonLobbyModel);
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }

    @Override
    public void stop() {
        //es.shutdownNow(); // TODO
    }

    public static void main(String[] args) {
        launch(args);
    }
}
