package de.alshikh.haw.tron.app.model.lobby.datatypes;

import de.alshikh.haw.tron.app.controller.game.helpers.IPlayerUpdateChannel;

import java.util.UUID;

public class Room implements IRoom {
    private final UUID id;
    private final String name;

    protected final IPlayerUpdateChannel hostUpdateChannel;
    protected IPlayerUpdateChannel gustUpdateChannel;

    public Room(IPlayerUpdateChannel hostUpdateChannel) {
        this.id = hostUpdateChannel.getPlayerId();
        this.name = hostUpdateChannel.getPlayerName();
        this.hostUpdateChannel = hostUpdateChannel;
    }

    @Override
    public void enter(IPlayerUpdateChannel gustUpdateChannel) {
        this.gustUpdateChannel = gustUpdateChannel;
        close();
    }

    protected void close() {
        hostUpdateChannel.addListener(gustUpdateChannel);
        gustUpdateChannel.addListener(hostUpdateChannel);
    }

    @Override
    public UUID getId() {
        return id;
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
