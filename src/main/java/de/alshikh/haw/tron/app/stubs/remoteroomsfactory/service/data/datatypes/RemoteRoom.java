package de.alshikh.haw.tron.app.stubs.remoteroomsfactory.service.data.datatypes;

import de.alshikh.haw.tron.app.controllers.game.helpers.IPlayerUpdateChannel;
import de.alshikh.haw.tron.app.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.app.stubs.PlayerUpdateChannelClient;

import java.util.function.Function;

public class RemoteRoom extends Room {
    private final Function<IPlayerUpdateChannel, PlayerUpdateChannelClient> createRpcClientHook;

    public RemoteRoom(IPlayerUpdateChannel hostUpdateChannel, Function<IPlayerUpdateChannel, PlayerUpdateChannelClient> createRpcClientHook) {
        super(hostUpdateChannel);
        this.createRpcClientHook = createRpcClientHook;
    }

    protected void close() {
        hostUpdateChannel.addListener(createRpcClientHook.apply(gustUpdateChannel));
        gustUpdateChannel.addListener(hostUpdateChannel);
    }
}
