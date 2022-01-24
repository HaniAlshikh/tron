package de.alshikh.haw.tron.app.controllers.lobby;

import de.alshikh.haw.tron.app.controllers.game.helpers.IPlayerUpdateChannel;
import de.alshikh.haw.tron.app.controllers.lobby.inputhandlers.RoomsMenuInputHandler;
import de.alshikh.haw.tron.app.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.app.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.app.views.lobby.ILobbyView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.UUID;

public final class LobbyController implements ILobbyController {

    private final ILobbyModel lobbyModel;
    private final ILobbyView lobbyView;

    public LobbyController(ILobbyModel lobbyModel, ILobbyView lobbyView) {
        this.lobbyView = lobbyView;
        this.lobbyModel = lobbyModel;
    }

    @Override
    public void showRoomsMenu(IPlayerUpdateChannel guestUpdateChannel, EventHandler<ActionEvent> cancelBtnHandler) {
        RoomsMenuInputHandler roomsMenuInputHandler = new RoomsMenuInputHandler();
        roomsMenuInputHandler.setListItemConsumer(room -> {
            room.enter(guestUpdateChannel);
            removeRoom(room.getUuid());
        });
        lobbyView.showRoomsMenu(roomsMenuInputHandler, lobbyModel.getRoomsList(), cancelBtnHandler);
    }

    @Override
    public void createRoom(IPlayerUpdateChannel hostUpdateChannel) {
        lobbyModel.addRoom(new Room(hostUpdateChannel));
    }

    @Override
    public void removeRoom(UUID uuid) {
        lobbyModel.removeRoom(uuid);
    }
}
