package de.alshikh.haw.tron.client.controllers.lobby.inputhandlers;

import de.alshikh.haw.tron.client.models.lobby.datatypes.IRoom;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class RoomsMenuInputHandler implements EventHandler<MouseEvent> {

    ListView<IRoom> listView;
    Consumer<IRoom> listItemConsumer;

    public RoomsMenuInputHandler() {}

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            listItemConsumer.accept(listView.getSelectionModel().getSelectedItem());
        }
    }

    public ListView<IRoom> getListView() {
        return listView;
    }

    public void setListView(ListView<IRoom> listView) {
        this.listView = listView;
    }

    public Consumer<IRoom> getListItemConsumer() {
        return listItemConsumer;
    }

    public void setListItemConsumer(Consumer<IRoom> listItemConsumer) {
        this.listItemConsumer = listItemConsumer;
    }
}
