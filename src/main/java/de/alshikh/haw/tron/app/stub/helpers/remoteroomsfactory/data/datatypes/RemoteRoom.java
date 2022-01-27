package de.alshikh.haw.tron.app.stub.helpers.remoteroomsfactory.data.datatypes;

import de.alshikh.haw.tron.app.model.lobby.data.datatypes.IPlayerUpdateChannel;
import de.alshikh.haw.tron.app.model.lobby.data.entities.Room;
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
