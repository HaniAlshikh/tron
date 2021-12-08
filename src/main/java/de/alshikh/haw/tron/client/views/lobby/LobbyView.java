package de.alshikh.haw.tron.client.views.lobby;

import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.client.views.lobby.overlayes.RoomsMenu;
import de.alshikh.haw.tron.client.views.view_library.ITronView;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.function.Consumer;

public class LobbyView implements ILobbyView {

    private final ITronView view;

    public LobbyView(ITronView view) throws IOException {
        this.view = view;
    }

    @Override
    public void showRoomsMenu(ObservableList<Room> rooms, Consumer<Room> roomConsumer) {
        RoomsMenu roomsMenu = new RoomsMenu("menu.css");
        roomsMenu.getRoomListView().setItems(rooms);
        roomsMenu.setRoomConsumer(roomConsumer);
        view.registerOverlay("rooms", roomsMenu);
        view.init();
        view.showOverlay("rooms");
    }
}
