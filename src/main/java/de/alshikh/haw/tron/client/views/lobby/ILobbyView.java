package de.alshikh.haw.tron.client.views.lobby;

import de.alshikh.haw.tron.client.controllers.lobby.inputhandlers.RoomsMenuInputHandler;
import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import javafx.collections.ObservableList;

public interface ILobbyView {
    void showRoomsMenu(RoomsMenuInputHandler roomsMenuInputHandler, ObservableList<Room> rooms);
}
