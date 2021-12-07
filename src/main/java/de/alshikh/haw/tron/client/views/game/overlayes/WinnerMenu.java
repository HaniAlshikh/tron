package de.alshikh.haw.tron.client.views.game.overlayes;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import de.alshikh.haw.tron.client.views.view_library.ViewUtility;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class WinnerMenu extends VBox {
    private final Label labelWinner;
    private final Button btnStart;
    private final Button btnExit;

    public WinnerMenu(String stylesheet, Player winner) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        labelWinner = new Label(winner == null ? "It's a tie" : winner + " won");
        labelWinner.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        btnStart = new Button("Play again");
        btnExit = new Button("Exit");
        btnExit.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

        this.getChildren().add(labelWinner);
        HBox hBox = new HBox(20);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(btnStart, btnExit);
        this.getChildren().add(hBox);
    }

    public Button getBtnStart() {
        return btnStart;
    }
}
