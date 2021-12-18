package de.alshikh.haw.tron.client.views.lobby;

import de.alshikh.haw.tron.client.controllers.lobby.inputhandlers.RoomsMenuInputHandler;
import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.client.views.lobby.overlayes.RoomsMenu;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import javafx.collections.ObservableList;

import java.io.IOException;

public class LobbyView implements ILobbyView {

    private final ITronView view;

    public LobbyView(ITronView view) throws IOException {
        this.view = view;
    }

    @Override
    public void showRoomsMenu(RoomsMenuInputHandler roomsMenuInputHandler, ObservableList<Room> rooms) {
        RoomsMenu roomsMenu = new RoomsMenu("menu.css");
        roomsMenuInputHandler.setListView(roomsMenu.getRoomListView());
        roomsMenu.getRoomListView().setItems(rooms);
        roomsMenu.getRoomListView().setOnMouseClicked(roomsMenuInputHandler);
        view.registerOverlay("rooms", roomsMenu);
        view.init();
        view.showOverlay("rooms");
    }
}
