package de.alshikh.haw.tron.app.views.manager.overlays;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

public class LoadingMenu extends VBox {
    private final Label progressTxt;

    public LoadingMenu(String stylesheet) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressTxt = new Label();

        this.getChildren().add(progressIndicator);
        this.getChildren().add(progressTxt);
    }

    public Label getProgressTxt() {
        return progressTxt;
    }
}
