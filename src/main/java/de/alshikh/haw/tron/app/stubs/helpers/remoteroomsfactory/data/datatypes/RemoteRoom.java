package de.alshikh.haw.tron.app.stubs.helpers.remoteroomsfactory.data.datatypes;

import de.alshikh.haw.tron.app.controllers.game.helpers.IPlayerUpdateChannel;
import de.alshikh.haw.tron.app.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.app.stubs.PlayerUpdateChannelClient;

import java.util.function.Function;

public class RemoteRoom extends Room {
    // needed because the gustUpdateChannel is obtained after the player has chosen the room
    // he want to join therefore this can not be abstracted to upper levels (considering the game as a blackbox)
    private final Function<IPlayerUpdateChannel, PlayerUpdateChannelClient> rpcClientFactory;

    public RemoteRoom(IPlayerUpdateChannel hostUpdateChannel,
                      Function<IPlayerUpdateChannel, PlayerUpdateChannelClient> rpcClientFactory) {
        super(hostUpdateChannel);
        this.rpcClientFactory = rpcClientFactory;
    }

    protected void close() {
        hostUpdateChannel.addListener(rpcClientFactory.apply(gustUpdateChannel));
        gustUpdateChannel.addListener(hostUpdateChannel);
    }
}
