package de.alshikh.haw.tron.app.controllers.lobby;

import de.alshikh.haw.tron.app.controllers.game.helpers.IUpdateChannel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.UUID;

public interface ILobbyController {

    void showRoomsMenu(IUpdateChannel updateChannel, EventHandler<ActionEvent> cancelBtnHandler);

    void createRoom(IUpdateChannel updateChannel);

    void removeRoom(UUID uuid);
}
