package de.alshikh.haw.tron.client.views.lobby.handlers;

import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class RoomsMenuInputHandler implements EventHandler<MouseEvent> {

    ListView<Room> listView;
    Consumer<Room> consumer;

    public RoomsMenuInputHandler(ListView<Room> listView, Consumer<Room> consumer) {
        this.listView = listView;
        this.consumer = consumer;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            consumer.accept(listView.getSelectionModel().getSelectedItem());
        }
    }
}
