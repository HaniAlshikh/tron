package de.alshikh.haw.tron.client.models.lobby;

import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LobbyModel implements ILobbyModel {

    ObservableList<Room> rooms = FXCollections.observableArrayList();

    private static final LobbyModel instance = new LobbyModel();
    public static LobbyModel getInstance() {
        return instance;
    }
    private LobbyModel() {}

    @Override
    public void addRoom(Room room) {
        rooms.add(room);
    }

    @Override
    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    @Override
    public ObservableList<Room> getRooms() {
        return rooms;
    }
}
