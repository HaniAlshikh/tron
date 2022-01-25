package de.alshikh.haw.tron.app.models.lobby;

import de.alshikh.haw.tron.app.models.lobby.datatypes.IRoom;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.UUID;

public class LobbyModel implements ILobbyModel {
    // maps are unordered and therefore not supported in javafx as view
    ObservableMap<UUID, IRoom> rooms = FXCollections.observableHashMap();
    ObservableList<IRoom> roomsList = FXCollections.observableArrayList();

    public LobbyModel() {
        rooms.addListener((MapChangeListener<UUID, IRoom>) change -> {
            roomsList.remove(change.getValueRemoved());
            if (change.wasAdded()) {
                roomsList.add(change.getValueAdded());
            }
        });
    }

    @Override
    public void addRoom(IRoom room) {
        rooms.put(room.getId(), room);
    }

    @Override
    public void removeRoom(UUID uuid) {
        rooms.remove(uuid);
    }

    @Override
    public ObservableMap<UUID, IRoom> getRooms() {
        return rooms;
    }

    @Override
    public ObservableList<IRoom> getRoomsList() {
        return roomsList;
    }
}
