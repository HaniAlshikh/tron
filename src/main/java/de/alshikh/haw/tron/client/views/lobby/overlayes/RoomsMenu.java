package de.alshikh.haw.tron.client.views.lobby.overlayes;

import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.client.views.view_library.ViewUtility;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RoomsMenu extends VBox {

    private final ListView<Room> roomsListView;

    public RoomsMenu(String stylesheet) {
        super(20.0);
        this.getStylesheets().add(stylesheet);
        this.setAlignment(Pos.CENTER);

        final Label labelReady = new Label("Please choose a room!");
        labelReady.setStyle("-fx-text-fill: " + ViewUtility.getHexTriplet(Color.PAPAYAWHIP.brighter()) + ";");

        roomsListView = new ListView<>();
        roomsListView.prefWidthProperty().bind(this.widthProperty().divide(3));
        roomsListView.setMaxWidth(Control.USE_PREF_SIZE);
        roomsListView.prefHeightProperty().bind(this.heightProperty().divide(2));
        roomsListView.setMaxHeight(Control.USE_PREF_SIZE);
        final Label labelPlaceholder = new Label("No rooms available");
        labelPlaceholder.setStyle("-fx-text-fill: #575149;");
        roomsListView.setPlaceholder(labelPlaceholder);


        this.getChildren().add(labelReady);
        this.getChildren().add(roomsListView);
    }

    public ListView<Room> getRoomListView() {
        return roomsListView;
    }
}