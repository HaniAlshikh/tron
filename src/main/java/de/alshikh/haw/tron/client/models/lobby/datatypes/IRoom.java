package de.alshikh.haw.tron.client.models.lobby.datatypes;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;

import java.util.UUID;

public interface IRoom {
    void enter(IUpdateChannel gustUpdateChannel);

    UUID getUuid();

    String getName();

    IUpdateChannel getHostUpdateChannel();
}
