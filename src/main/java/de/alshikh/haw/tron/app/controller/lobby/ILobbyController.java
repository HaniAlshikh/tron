package de.alshikh.haw.tron.app.controller.lobby;

import de.alshikh.haw.tron.app.model.lobby.data.datatypes.IPlayerUpdateChannel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.UUID;

public interface ILobbyController {
    void showRoomsMenu(IPlayerUpdateChannel updateChannel, EventHandler<ActionEvent> cancelBtnHandler);

    void createRoom(IPlayerUpdateChannel updateChannel);

    void removeRoom(UUID roomId);
}
