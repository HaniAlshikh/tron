package de.alshikh.haw.tron.client.controllers.game;

import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import javafx.beans.property.StringProperty;

public interface IGameController {

    void close();

    void showStartMenu();

    void joinGame();

    void admit(IGameController opponentController);

    void startGame();

    PlayerUpdate getPlayerUpdate();

    StringProperty playerNameProperty();
}
