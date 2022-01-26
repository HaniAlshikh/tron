package de.alshikh.haw.tron.app.stub.helpers.remoteroomsfactory.data.datatypes;

import de.alshikh.haw.tron.app.controller.game.helpers.IPlayerUpdateChannel;
import de.alshikh.haw.tron.app.model.lobby.datatypes.Room;
import de.alshikh.haw.tron.app.stub.PlayerUpdateChannelCaller;

import java.util.function.Function;

public class RemoteRoom extends Room {

    private final Function<IPlayerUpdateChannel, PlayerUpdateChannelCaller> playerUpdateChannelAppStubFactory;

    public RemoteRoom(IPlayerUpdateChannel hostUpdateChannel,
                      Function<IPlayerUpdateChannel, PlayerUpdateChannelCaller> playerUpdateChannelAppStubFactory) {
        super(hostUpdateChannel);
        this.playerUpdateChannelAppStubFactory = playerUpdateChannelAppStubFactory;
    }

    protected void close() {
        hostUpdateChannel.addListener(playerUpdateChannelAppStubFactory.apply(gustUpdateChannel));
        gustUpdateChannel.addListener(hostUpdateChannel);
    }
}
