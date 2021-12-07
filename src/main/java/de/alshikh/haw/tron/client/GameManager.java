package de.alshikh.haw.tron.client;

import de.alshikh.haw.tron.client.views.view_library.ITronView;
import de.alshikh.haw.tron.client.views.view_library.TronView;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameManager extends Application {

    public final static String MANAGER_CONFIG_FILE = "manager.properties";

    @Override
    public void start(Stage stage) throws Exception {

        ITronView ManagerView = new TronView(MANAGER_CONFIG_FILE);
        
        // configure and show stage
        stage.setTitle("TRON Game Manager");
        stage.setScene(ManagerView.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
