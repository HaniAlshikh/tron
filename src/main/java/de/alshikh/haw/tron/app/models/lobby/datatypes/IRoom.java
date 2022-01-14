package de.alshikh.haw.tron.app.models.lobby.datatypes;

import de.alshikh.haw.tron.app.controllers.game.helpers.IUpdateChannel;

import java.util.UUID;

public interface IRoom {
    void enter(IUpdateChannel gustUpdateChannel);

    UUID getUuid();

    String getName();

    IUpdateChannel getHostUpdateChannel();
}
