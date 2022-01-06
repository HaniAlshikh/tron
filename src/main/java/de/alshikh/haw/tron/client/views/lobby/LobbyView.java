package de.alshikh.haw.tron.client.views.lobby;

import de.alshikh.haw.tron.client.controllers.lobby.inputhandlers.RoomsMenuInputHandler;
import de.alshikh.haw.tron.client.models.lobby.datatypes.IRoom;
import de.alshikh.haw.tron.client.views.lobby.overlayes.RoomsMenu;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import javafx.collections.ObservableList;

import java.io.IOException;

public class LobbyView implements ILobbyView {

    private final RoomsMenu roomsMenu;

    private final ITronView view;

    public LobbyView(ITronView view) throws IOException {
        this.view = view;

        roomsMenu = new RoomsMenu("menu.css");
        view.registerOverlay("rooms", roomsMenu);

        view.init();
    }

    @Override
    public void showRoomsMenu(RoomsMenuInputHandler roomsMenuInputHandler, ObservableList<IRoom> rooms) {
        roomsMenuInputHandler.setListView(roomsMenu.getRoomListView());
        roomsMenu.getRoomListView().setItems(rooms);
        roomsMenu.getRoomListView().setOnMouseClicked(roomsMenuInputHandler);
        view.init();
        view.showOverlay("rooms");
    }
}
