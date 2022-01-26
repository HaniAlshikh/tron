package de.alshikh.haw.tron.app.stub.helpers.remoteroomsfactory.data.datatypes;

import de.alshikh.haw.tron.app.controller.game.helpers.IPlayerUpdateChannel;
import de.alshikh.haw.tron.app.model.lobby.datatypes.Room;
import de.alshikh.haw.tron.app.stub.PlayerUpdateChannelCaller;

import java.util.function.Function;

public class RemoteRoom extends Room {
    // needed because the gustUpdateChannel is obtained after the player has chosen the room
    // he want to join therefore this can not be abstracted to upper levels (considering the game as a blackbox)
    private final Function<IPlayerUpdateChannel, PlayerUpdateChannelCaller> rpcClientFactory;

    public RemoteRoom(IPlayerUpdateChannel hostUpdateChannel,
                      Function<IPlayerUpdateChannel, PlayerUpdateChannelCaller> rpcClientFactory) {
        super(hostUpdateChannel);
        this.rpcClientFactory = rpcClientFactory;
    }

    protected void close() {
        hostUpdateChannel.addListener(rpcClientFactory.apply(gustUpdateChannel));
        gustUpdateChannel.addListener(hostUpdateChannel);
    }
}
