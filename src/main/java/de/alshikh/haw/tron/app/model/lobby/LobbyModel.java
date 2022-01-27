package de.alshikh.haw.tron.app.model.lobby;

import de.alshikh.haw.tron.app.model.lobby.data.entities.IRoom;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.UUID;

public class LobbyModel implements ILobbyModel {
    // maps are unordered and therefore not supported in javafx as view
    private final ObservableMap<UUID, IRoom> rooms = FXCollections.observableHashMap();
    private final ObservableList<IRoom> roomsList = FXCollections.observableArrayList();

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
        Platform.runLater(() -> rooms.put(room.getId(), room));
    }

    @Override
    public void removeRoom(UUID uuid) {
        Platform.runLater(() -> rooms.remove(uuid));
    }

    @Override
    public ObservableList<IRoom> getRoomsList() {
        return roomsList;
    }
}
