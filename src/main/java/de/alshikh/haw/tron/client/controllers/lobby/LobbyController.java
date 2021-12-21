package de.alshikh.haw.tron.client.controllers.lobby;

import de.alshikh.haw.tron.client.controllers.game.IGameController;
import de.alshikh.haw.tron.client.controllers.lobby.inputhandlers.RoomsMenuInputHandler;
import de.alshikh.haw.tron.client.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.client.models.lobby.LobbyModel;
import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.client.views.lobby.ILobbyView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class LobbyController implements ILobbyController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ILobbyModel lobbyModel = LobbyModel.getInstance();
    private final ILobbyView lobbyView;

    public LobbyController(ILobbyView lobbyView) {
        this.lobbyView = lobbyView;
    }

    @Override
    public void showRoomsMenu(IGameController playerController) {
        RoomsMenuInputHandler roomsMenuInputHandler = new RoomsMenuInputHandler();
        roomsMenuInputHandler.setListItemConsumer(room -> startGame(playerController, room.getHostController()));
        lobbyView.showRoomsMenu(roomsMenuInputHandler, lobbyModel.getRoomsList());
    }

    @Override
    public void createRoom(UUID uuid, String label, IGameController hostController) {
        lobbyModel.addRoom(uuid, new Room(label, hostController));
    }

    @Override
    public void removeRoom(UUID uuid) {
        lobbyModel.removeRoom(uuid);
    }

    private void startGame(IGameController playerController, IGameController hostController) {
        hostController.admit(playerController);
        playerController.admit(hostController);

        playerController.startGame();
        hostController.startGame();
    }
}
