package de.alshikh.haw.tron.client.controllers.lobby.inputhandlers;

import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class RoomsMenuInputHandler implements EventHandler<MouseEvent> {

    ListView<Room> listView;
    Consumer<Room> listItemConsumer;

    public RoomsMenuInputHandler() {}

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            listItemConsumer.accept(listView.getSelectionModel().getSelectedItem());
        }
    }

    public ListView<Room> getListView() {
        return listView;
    }

    public void setListView(ListView<Room> listView) {
        this.listView = listView;
    }

    public Consumer<Room> getListItemConsumer() {
        return listItemConsumer;
    }

    public void setListItemConsumer(Consumer<Room> listItemConsumer) {
        this.listItemConsumer = listItemConsumer;
    }
}
