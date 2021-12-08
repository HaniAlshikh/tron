package de.alshikh.haw.tron.client.models.lobby;

import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import javafx.collections.ObservableList;

public interface ILobbyModel {
    void addRoom(Room room);

    void removeRoom(Room room);

    ObservableList<Room> getRooms();
}
