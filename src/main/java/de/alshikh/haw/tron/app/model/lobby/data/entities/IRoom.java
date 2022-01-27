package de.alshikh.haw.tron.app.model.lobby.data.entities;

import de.alshikh.haw.tron.app.model.lobby.data.datatypes.IPlayerUpdateChannel;

import java.util.UUID;

public interface IRoom {
    void enter(IPlayerUpdateChannel gustUpdateChannel);

    UUID getId();

    String getName();

    IPlayerUpdateChannel getHostUpdateChannel();
}
