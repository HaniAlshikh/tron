package de.alshikh.haw.tron.client;

import de.alshikh.haw.tron.client.views.manager.overlays.ManagerMenu;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import de.alshikh.haw.tron.client.views.view_library.TronView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameManager extends Application {

    public final static String MANAGER_CONFIG_FILE = "manager.properties";
    public final static double RESOURCES_LIMIT = 0.8;

    // TODO: limit to only 80% of machine resources
    //  is this enough?
    //  what to do if all threads are busy?
    //  - don't allow new games
    //  - allow new games and accept the lag
    //      (for example displaying the current game state in the 7th game need to wait for the other 6 games)
    ExecutorService es = Executors.newFixedThreadPool((int)
            (Runtime.getRuntime().availableProcessors() * RESOURCES_LIMIT));

    @Override
    public void start(Stage stage) throws Exception {
        ITronView managerView = new TronView(MANAGER_CONFIG_FILE);

        ManagerMenu managerMenu = new ManagerMenu("menu.css");
        managerMenu.getBtnNew().setOnAction(e -> Platform.runLater(new TronGame(es)));
        managerView.registerOverlay("managerMenu", managerMenu);

        managerView.init();
        managerView.showOverlay("managerMenu");

        stage.setTitle("TRON Game Manager");
        stage.setScene(managerView.getScene());
        stage.setAlwaysOnTop(true);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.show();
        stage.setX((screenBounds.getWidth() - managerView.getScene().getWidth()) / 2);
        stage.setY(0);
        stage.setOnCloseRequest(e -> { Platform.exit(); System.exit(0); });
    }

    @Override
    public void stop() {
        es.shutdownNow();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
