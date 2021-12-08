package de.alshikh.haw.tron.client.views.lobby;

import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import javafx.collections.ObservableList;

import java.util.function.Consumer;

public interface ILobbyView {
    void showRoomsMenu(ObservableList<Room> rooms, Consumer<Room> roomConsumer);
}
