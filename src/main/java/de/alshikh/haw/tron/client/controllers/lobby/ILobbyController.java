package de.alshikh.haw.tron.client.controllers.lobby;

import de.alshikh.haw.tron.client.controllers.game.IGameController;

import java.util.UUID;

public interface ILobbyController {

    void showRoomsMenu(IGameController playerController);

    void createRoom(UUID uuid, String label, IGameController hostController);

    void removeRoom(UUID uuid);
}
