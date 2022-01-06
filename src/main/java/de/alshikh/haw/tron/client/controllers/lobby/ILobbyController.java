package de.alshikh.haw.tron.client.controllers.lobby;

import de.alshikh.haw.tron.client.controllers.game.helpers.IUpdateChannel;

import java.util.UUID;

public interface ILobbyController {

    void showRoomsMenu(IUpdateChannel updateChannel);

    void createRoom(IUpdateChannel updateChannel);

    void removeRoom(UUID uuid);
}
