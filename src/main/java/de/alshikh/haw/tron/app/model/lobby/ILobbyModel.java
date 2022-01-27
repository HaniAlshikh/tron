package de.alshikh.haw.tron.app.model.lobby;

import de.alshikh.haw.tron.app.model.lobby.data.entities.IRoom;
import javafx.collections.ObservableList;

import java.util.UUID;

public interface ILobbyModel {
    void addRoom(IRoom room);

    void removeRoom(UUID uuid);

    ObservableList<IRoom> getRoomsList();
}
