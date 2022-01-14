package de.alshikh.haw.tron.app.controllers.lobby;

import de.alshikh.haw.tron.app.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.app.controllers.lobby.inputhandlers.RoomsMenuInputHandler;
import de.alshikh.haw.tron.app.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.app.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.app.views.lobby.ILobbyView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class LobbyController implements ILobbyController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final ILobbyModel lobbyModel;
    private final ILobbyView lobbyView;

    public LobbyController(ILobbyModel lobbyModel, ILobbyView lobbyView) {
        this.lobbyView = lobbyView;
        this.lobbyModel = lobbyModel;
    }

    @Override
    public void showRoomsMenu(IUpdateChannel guestUpdateChannel, EventHandler<ActionEvent> cancelBtnHandler) {
        RoomsMenuInputHandler roomsMenuInputHandler = new RoomsMenuInputHandler();
        roomsMenuInputHandler.setListItemConsumer(room -> {
            // TODO: what is the best practise to create subs
            //  when sharing a remote room should i create room || roomManager || roomController || lobbyModel stubs?
            //  for example the room stub will get the guestUpdateChannel instance from which a guestUpdateChannelClient
            //  will be shared (room stub will do the conversion) on the other end
            //  the guestUpdateChannelClient addListener method will be called for the hostUpdateChannel
            //  which will create the client.
            //  -> the plan is to make the directory server a remote rooms list which will be bounded
            //  the the roomsList on each instance
            room.enter(guestUpdateChannel);
            // TODO: state pattern
            removeRoom(room.getUuid());
        });
        lobbyView.showRoomsMenu(roomsMenuInputHandler, lobbyModel.getRoomsList(), cancelBtnHandler);
    }

    @Override
    public void createRoom(IUpdateChannel hostUpdateChannel) {
        lobbyModel.addRoom(new Room(hostUpdateChannel));
    }

    @Override
    public void removeRoom(UUID uuid) {
        lobbyModel.removeRoom(uuid);
    }
}
