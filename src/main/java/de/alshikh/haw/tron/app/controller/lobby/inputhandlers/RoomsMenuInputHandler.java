package de.alshikh.haw.tron.app.controller.lobby.inputhandlers;

import de.alshikh.haw.tron.app.model.lobby.datatypes.IRoom;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class RoomsMenuInputHandler implements IRoomsMenuInputHandler {

    ListView<IRoom> listView;
    Consumer<IRoom> listItemConsumer;

    public RoomsMenuInputHandler() {}

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            listItemConsumer.accept(listView.getSelectionModel().getSelectedItem());
        }
    }

    @Override
    public void setListView(ListView<IRoom> listView) {
        this.listView = listView;
    }

    @Override
    public void setListItemConsumer(Consumer<IRoom> listItemConsumer) {
        this.listItemConsumer = listItemConsumer;
    }
}
