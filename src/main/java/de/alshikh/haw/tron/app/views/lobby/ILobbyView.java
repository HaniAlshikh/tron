package de.alshikh.haw.tron.app.views.lobby;

import de.alshikh.haw.tron.app.controllers.lobby.inputhandlers.RoomsMenuInputHandler;
import de.alshikh.haw.tron.app.models.lobby.datatypes.IRoom;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface ILobbyView {
    void showRoomsMenu(RoomsMenuInputHandler roomsMenuInputHandler, ObservableList<IRoom> rooms, EventHandler<ActionEvent> cancelBtnHandler);
}
