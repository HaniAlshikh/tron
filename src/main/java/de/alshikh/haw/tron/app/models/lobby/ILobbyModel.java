package de.alshikh.haw.tron.app.models.lobby;

import de.alshikh.haw.tron.app.models.lobby.datatypes.IRoom;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.UUID;

public interface ILobbyModel {
    void addRoom(IRoom room);

    void removeRoom(UUID uuid);

    ObservableMap<UUID, IRoom> getRooms();

    ObservableList<IRoom> getRoomsList();
}
