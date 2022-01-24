package de.alshikh.haw.tron.app.models.lobby.datatypes;

import de.alshikh.haw.tron.app.controllers.game.helpers.IPlayerUpdateChannel;

import java.util.UUID;

public interface IRoom {
    void enter(IPlayerUpdateChannel gustUpdateChannel);

    UUID getUuid();

    String getName();

    IPlayerUpdateChannel getHostUpdateChannel();
}
