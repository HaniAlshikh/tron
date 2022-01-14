package de.alshikh.haw.tron.app.models.lobby.datatypes;

import de.alshikh.haw.tron.app.controllers.game.helpers.IUpdateChannel;

import java.util.UUID;

public class Room implements IRoom {
    protected IUpdateChannel gustUpdateChannel;

    private final UUID uuid;
    private final String name;
    protected final IUpdateChannel hostUpdateChannel;

    public Room(IUpdateChannel hostUpdateChannel) {
        this.uuid = hostUpdateChannel.getId();
        this.name = hostUpdateChannel.getName();
        this.hostUpdateChannel = hostUpdateChannel;
    }

    @Override
    public void enter(IUpdateChannel gustUpdateChannel) {
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
    public IUpdateChannel getHostUpdateChannel() {
        return hostUpdateChannel;
    }

    @Override
    public String toString() {
        return name;
    }
}
