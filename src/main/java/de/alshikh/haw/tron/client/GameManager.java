package de.alshikh.haw.tron.client;

import de.alshikh.haw.tron.client.views.manager.overlays.ManagerMenu;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import de.alshikh.haw.tron.client.views.view_library.TronView;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameManager extends Application {

    public final static String MANAGER_CONFIG_FILE = "manager.properties";

    // TODO: limit to only 80% of machine resources
    //ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void start(Stage stage) throws Exception {

        ITronView managerView = new TronView(MANAGER_CONFIG_FILE);

        ManagerMenu managerMenu = new ManagerMenu("menu.css");
        managerMenu.getBtnNew().setOnAction(e -> new TronGame());
        managerView.registerOverlay("new", managerMenu);

        managerView.init();
        managerView.showOverlay("new");

        stage.setTitle("TRON Game Manager");
        stage.setScene(managerView.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
