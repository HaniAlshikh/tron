package de.alshikh.haw.tron.app.views.lobby;

import de.alshikh.haw.tron.Config;
import de.alshikh.haw.tron.app.controllers.lobby.inputhandlers.IRoomsMenuInputHandler;
import de.alshikh.haw.tron.app.models.lobby.datatypes.IRoom;
import de.alshikh.haw.tron.app.views.lobby.overlayes.RoomsMenu;
import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;

public class LobbyView implements ILobbyView {

    private final RoomsMenu roomsMenu;

    private final ITronView view;

    public LobbyView(ITronView view) throws IOException {
        this.view = view;

        roomsMenu = new RoomsMenu(Config.MENU_CSS);
        view.registerOverlay("rooms", roomsMenu);

        view.init();
    }

    @Override
    public void showRoomsMenu(IRoomsMenuInputHandler roomsMenuInputHandler,
                              ObservableList<IRoom> rooms,
                              EventHandler<ActionEvent> cancelBtnHandler) {
        roomsMenuInputHandler.setListView(roomsMenu.getRoomListView());
        roomsMenu.getRoomListView().setItems(rooms);
        roomsMenu.getRoomListView().setOnMouseClicked(roomsMenuInputHandler);
        roomsMenu.getBtnCancel().setOnAction(cancelBtnHandler);
        view.init();
        view.showOverlay("rooms");
    }
}
