package de.alshikh.haw.tron.client.models.lobby;

import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.UUID;

public interface ILobbyModel {
    void addRoom(UUID uuid, Room room);

    void removeRoom(UUID uuid);

    ObservableMap<UUID, Room> getRooms();

    ObservableList<Room> getRoomsList();
}
