package de.alshikh.haw.tron.app.model.lobby.datatypes;

import de.alshikh.haw.tron.app.controller.game.helpers.IPlayerUpdateChannel;

import java.util.UUID;

public interface IRoom {
    void enter(IPlayerUpdateChannel gustUpdateChannel);

    UUID getId();

    String getName();

    IPlayerUpdateChannel getHostUpdateChannel();
}
