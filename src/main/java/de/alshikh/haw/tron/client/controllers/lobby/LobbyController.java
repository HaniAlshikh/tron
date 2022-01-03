package de.alshikh.haw.tron.client.controllers.lobby;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.client.controllers.lobby.inputhandlers.RoomsMenuInputHandler;
import de.alshikh.haw.tron.client.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.client.models.lobby.LobbyModel;
import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.client.views.lobby.ILobbyView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class LobbyController implements ILobbyController {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final ILobbyModel lobbyModel = LobbyModel.getInstance();
    private final ILobbyView lobbyView;

    public LobbyController(ILobbyView lobbyView) {
        this.lobbyView = lobbyView;
    }

    @Override
    public void showRoomsMenu(IUpdateChannel guestUpdateChannel) {
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
            lobbyModel.enterRoom(room, guestUpdateChannel);
            // TODO: state pattern
            removeRoom(room.getUuid());
        });
        lobbyView.showRoomsMenu(roomsMenuInputHandler, lobbyModel.getRoomsList());
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
