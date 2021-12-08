package de.alshikh.haw.tron.client.controllers.lobby;

import de.alshikh.haw.tron.client.controllers.game.IGameController;

public interface ILobbyController {

    void showRoomsMenu(IGameController playerController);

    void createRoom(String label, IGameController hostController);
}
