package de.alshikh.haw.tron.client.stubs.remoteroomsfactory.service.data.datatypes;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;
import de.alshikh.haw.tron.client.models.lobby.datatypes.IRoom;
import de.alshikh.haw.tron.client.stubs.PlayerUpdateChannelClient;

import java.util.UUID;
import java.util.function.Function;

public class RemoteRoom implements IRoom {
    // TODO: generalize to have multiple gusts
    private IUpdateChannel gustUpdateChannel;

    private final UUID uuid;
    private final String name;
    private final IUpdateChannel hostUpdateChannel;

    Function<IUpdateChannel, PlayerUpdateChannelClient> createRpcClient;

    public RemoteRoom(IUpdateChannel hostUpdateChannel, Function<IUpdateChannel, PlayerUpdateChannelClient> createRpcClient) {
        this.uuid = hostUpdateChannel.getId();
        this.name = hostUpdateChannel.getName();
        this.hostUpdateChannel = hostUpdateChannel;
        this.createRpcClient = createRpcClient;
    }

    @Override
    public void enter(IUpdateChannel gustUpdateChannel) {
        this.gustUpdateChannel = gustUpdateChannel;
        // TODO: state pattern (onFull exchange channels)
        forwardChannel();
    }

    private void forwardChannel() {
        // host is always remote
        PlayerUpdateChannelClient c = createRpcClient.apply(gustUpdateChannel);
        hostUpdateChannel.addListener(c);
        gustUpdateChannel.addListener(hostUpdateChannel);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IUpdateChannel getHostUpdateChannel() {
        return hostUpdateChannel;
    }

    @Override
    public String toString() {
        return name;
    }
}
