package de.alshikh.haw.tron.client.views.manager.overlays;

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

        this.getChildren().add(btnNew);
    }

    public Button getBtnNew() {
        return btnNew;
    }
}
