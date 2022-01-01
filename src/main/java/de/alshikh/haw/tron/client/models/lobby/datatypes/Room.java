package de.alshikh.haw.tron.client.models.lobby.datatypes;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;

import java.util.UUID;

public class Room {

    // TODO: generalize to have multiple gusts
    private IUpdateChannel gustUpdateChannel;

    private final UUID uuid;
    private final String name;
    private final IUpdateChannel hostUpdateChannel;

    public Room(IUpdateChannel hostUpdateChannel) {
        this.uuid = hostUpdateChannel.getId();
        this.name = hostUpdateChannel.getName();
        this.hostUpdateChannel = hostUpdateChannel;
    }

    public void enter(IUpdateChannel gustUpdateChannel) {
        this.gustUpdateChannel = gustUpdateChannel;
        // TODO: state pattern (onFull exchange channels)
        forwardChannel();
    }

    private void forwardChannel() {
        hostUpdateChannel.addListener(gustUpdateChannel);
        gustUpdateChannel.addListener(hostUpdateChannel);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public IUpdateChannel getHostUpdateChannel() {
        return hostUpdateChannel;
    }

    @Override
    public String toString() {
        return name;
    }
}
