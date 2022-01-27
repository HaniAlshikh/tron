package de.alshikh.haw.tron.app.controller.lobby.inputhandlers;

import de.alshikh.haw.tron.app.model.lobby.data.entities.IRoom;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public interface IRoomsMenuInputHandler extends EventHandler<MouseEvent> {
    void setListView(ListView<IRoom> listView);

    void setListItemConsumer(Consumer<IRoom> listItemConsumer);
}
