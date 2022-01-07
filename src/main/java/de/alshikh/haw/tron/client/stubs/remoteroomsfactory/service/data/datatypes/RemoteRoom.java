package de.alshikh.haw.tron.client.stubs.remoteroomsfactory.service.data.datatypes;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.client.stubs.PlayerUpdateChannelClient;

import java.util.function.Function;

public class RemoteRoom extends Room {
    private final Function<IUpdateChannel, PlayerUpdateChannelClient> createRpcClientHook;

    public RemoteRoom(IUpdateChannel hostUpdateChannel, Function<IUpdateChannel, PlayerUpdateChannelClient> createRpcClientHook) {
        super(hostUpdateChannel);
        this.createRpcClientHook = createRpcClientHook;
    }

    protected void forwardChannel() {
        hostUpdateChannel.addListener(createRpcClientHook.apply(gustUpdateChannel));
        gustUpdateChannel.addListener(hostUpdateChannel);
    }
}
