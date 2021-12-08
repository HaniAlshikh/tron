package de.alshikh.haw.tron.client.controllers.lobby;

import de.alshikh.haw.tron.client.controllers.game.IGameController;
import de.alshikh.haw.tron.client.models.lobby.ILobbyModel;
import de.alshikh.haw.tron.client.models.lobby.LobbyModel;
import de.alshikh.haw.tron.client.models.lobby.datatypes.Room;
import de.alshikh.haw.tron.client.views.lobby.ILobbyView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LobbyController implements ILobbyController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ILobbyModel lobbyModel = LobbyModel.getInstance();
    private final ILobbyView lobbyView;

    public LobbyController(ILobbyView lobbyView) {
        this.lobbyView = lobbyView;
    }

    @Override
    public void showRoomsMenu(IGameController playerController) {
        lobbyView.showRoomsMenu(
                lobbyModel.getRooms(),
                room -> startGame(playerController, room.getHostController())
        );
    }

    @Override
    public void createRoom(String label, IGameController hostController) {
        lobbyModel.addRoom(new Room(label, hostController));
    }

    private void startGame(IGameController playerController, IGameController hostController) {
        playerController.joinGame(hostController);
        hostController.admit(playerController);

        playerController.startGame();
        hostController.startGame();
    }
}
