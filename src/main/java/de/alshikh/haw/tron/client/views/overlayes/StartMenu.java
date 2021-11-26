package de.alshikh.haw.tron.client.views.overlayes;

import de.alshikh.haw.tron.client.views.view_library.ViewUtility;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StartMenu extends VBox {
    private final Label labelReady;
    private final Button btnStart;

    public StartMenu(String stylesheet) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        labelReady = new Label("Ready?");
        labelReady.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        btnStart = new Button("Start Game");

        this.getChildren().add(labelReady);
        this.getChildren().add(btnStart);
    }

    public Button getBtnStart() {
        return btnStart;
    }
}
