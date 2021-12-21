package de.alshikh.haw.tron.client.views.game.overlayes;

import de.alshikh.haw.tron.client.views.view_library.ViewUtility;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StartMenu extends VBox {
    private final TextField txtPlayerName;
    private final Button btnStart;
    private final Button btnJoin;

    public StartMenu(String stylesheet) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        VBox userInput = new VBox();
        userInput.prefWidthProperty().bind(this.widthProperty().divide(3));
        userInput.setMaxWidth(Control.USE_PREF_SIZE);

        Label lblName = new Label("Name:");
        lblName.setStyle("-fx-font: bold 14px \"Sans\";");
        txtPlayerName = new TextField();

        userInput.getChildren().add(lblName);
        userInput.getChildren().add(txtPlayerName);

        Label lblMessage = new Label("Ready?");
        lblMessage.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        btnStart = new Button("Start Game");
        btnJoin = new Button("Join Game");

        this.getChildren().add(userInput);
        this.getChildren().add(lblMessage);
        this.getChildren().add(btnStart);
        this.getChildren().add(btnJoin);
    }

    public Button getBtnStart() {
        return btnStart;
    }

    public Button getBtnJoin() {
        return btnJoin;
    }

    public TextField getTxtPlayerName() {
        return txtPlayerName;
    }
}
