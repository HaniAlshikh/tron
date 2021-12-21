package de.alshikh.haw.tron.client.controllers.game;

import de.alshikh.haw.tron.client.models.game.IGameModel;
import de.alshikh.haw.tron.client.models.game.data.entities.PlayerUpdate;
import de.alshikh.haw.tron.client.views.game.IGameView;
import javafx.beans.property.StringProperty;

public interface IGameController {

    void showStartMenu();

    void joinGame();

    void admit(IGameController opponentController);

    void startGame();

    PlayerUpdate getPlayerUpdate();

    IGameModel getGameModel();

    IGameView getGameView();

    StringProperty playerNameProperty();
}
