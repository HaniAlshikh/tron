package de.alshikh.haw.tron.app.view.lobby;

import de.alshikh.haw.tron.app.controller.lobby.inputhandlers.IRoomsMenuInputHandler;
import de.alshikh.haw.tron.app.model.lobby.datatypes.IRoom;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface ILobbyView {
    void showRoomsMenu(IRoomsMenuInputHandler roomsMenuInputHandler, ObservableList<IRoom> rooms, EventHandler<ActionEvent> cancelBtnHandler);
}
