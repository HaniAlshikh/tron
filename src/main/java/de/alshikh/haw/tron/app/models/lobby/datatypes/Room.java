package de.alshikh.haw.tron.app.models.lobby.datatypes;

import de.alshikh.haw.tron.app.controllers.game.helpers.IPlayerUpdateChannel;

import java.util.UUID;

public class Room implements IRoom {
    protected IPlayerUpdateChannel gustUpdateChannel;

    private final UUID uuid;
    private final String name;
    protected final IPlayerUpdateChannel hostUpdateChannel;

    public Room(IPlayerUpdateChannel hostUpdateChannel) {
        this.uuid = hostUpdateChannel.getPlayerId();
        this.name = hostUpdateChannel.getPlayerName();
        this.hostUpdateChannel = hostUpdateChannel;
    }

    @Override
    public void enter(IPlayerUpdateChannel gustUpdateChannel) {
        this.gustUpdateChannel = gustUpdateChannel;
        // TODO: state pattern (onFull exchange channels)
        forwardChannel();
    }

    protected void forwardChannel() {
        hostUpdateChannel.addListener(gustUpdateChannel);
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
    public IPlayerUpdateChannel getHostUpdateChannel() {
        return hostUpdateChannel;
    }

    @Override
    public String toString() {
        return name;
    }
}
