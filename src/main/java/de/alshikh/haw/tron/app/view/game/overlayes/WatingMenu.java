package de.alshikh.haw.tron.app.view.game.overlayes;

import edu.cads.bai5.vsp.tron.view.ViewUtility;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class WatingMenu extends VBox {
    private final Button btnCancel;

    public WatingMenu(String stylesheet) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        Label labelReady = new Label("Please wait until someone joins!");
        labelReady.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        btnCancel = new Button("Cancel Game");

        this.getChildren().add(labelReady);
        this.getChildren().add(btnCancel);
    }

    public Button getBtnCancel() {
        return btnCancel;
    }
}
