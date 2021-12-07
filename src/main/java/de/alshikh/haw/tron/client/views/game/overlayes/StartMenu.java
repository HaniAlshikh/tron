package de.alshikh.haw.tron.client.views.game.overlayes;

import de.alshikh.haw.tron.client.views.view_library.ViewUtility;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StartMenu extends VBox {
    // TODO: use startMenu as winnerMenu with dynamic label
    private final Label labelReady;
    private final Button btnStart;
    private final Button btnJoin;

    public StartMenu(String stylesheet) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        labelReady = new Label("Ready?");
        labelReady.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        btnStart = new Button("Start Game");
        btnJoin = new Button("Join Game");

        this.getChildren().add(labelReady);
        this.getChildren().add(btnStart);
        this.getChildren().add(btnJoin);
    }

    public Button getBtnStart() {
        return btnStart;
    }

    public Button getBtnJoin() {
        return btnJoin;
    }
}
