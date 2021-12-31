package de.alshikh.haw.tron.client.models.lobby;

import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.UUID;

public class LobbyModel implements ILobbyModel {

    // maps are unordered and therefore not supported in javafx as view
    ObservableMap<UUID, Room> rooms = FXCollections.observableHashMap();
    ObservableList<Room> roomsList = FXCollections.observableArrayList();


    private static final LobbyModel instance = new LobbyModel();
    public static LobbyModel getInstance() {
        return instance;
    }
    private LobbyModel() {
        rooms.addListener((MapChangeListener<UUID, Room>) change -> {
            roomsList.remove(change.getValueRemoved());
            if (change.wasAdded()) {
                roomsList.add(change.getValueAdded());
            }
        });
    }

    @Override
    public void addRoom(Room room) {
        rooms.put(room.getUuid(), room);
    }

    @Override
    public void removeRoom(UUID uuid) {
        rooms.remove(uuid);
    }

    @Override
    public ObservableMap<UUID, Room> getRooms() {
        return rooms;
    }

    @Override
    public ObservableList<Room> getRoomsList() {
        return roomsList;
    }
}
