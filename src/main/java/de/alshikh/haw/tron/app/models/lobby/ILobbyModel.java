package de.alshikh.haw.tron.app.models.lobby;

import de.alshikh.haw.tron.app.models.lobby.datatypes.IRoom;
import javafx.collections.ObservableList;

import java.util.UUID;

public interface ILobbyModel {
    void addRoom(IRoom room);

    void removeRoom(UUID uuid);

    ObservableList<IRoom> getRoomsList();
}
