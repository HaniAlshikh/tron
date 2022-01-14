package de.alshikh.haw.tron.app.views.manager.overlays;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ManagerMenu extends VBox {
    private final Button btnNew;

    public ManagerMenu(String stylesheet) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        btnNew = new Button("New Game");
        Button btnExit = new Button("Exit");
        btnExit.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

        this.getChildren().add(btnNew);
        this.getChildren().add(btnExit);
    }

    public Button getBtnNew() {
        return btnNew;
    }
}
