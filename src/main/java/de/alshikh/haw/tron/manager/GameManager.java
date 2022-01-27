package de.alshikh.haw.tron.manager;

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

    ILobbyModel singletonLobbyModel = new LobbyModel(); // TD10

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

    public static void main(String[] args) {
        launch(args);
    }
}
