package de.alshikh.haw.tron.app.views.game;

import de.alshikh.haw.tron.app.models.game.data.entities.Game;
import de.alshikh.haw.tron.app.views.view_library.Coordinate;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public interface IGameView {

    void showGame(Game game);

    void highlightCell(Coordinate... cells);

    void showStartMenu(StringProperty playerName, String message,
                       EventHandler<ActionEvent> startBtnHandler, EventHandler<ActionEvent> joinBtnHandler);

    void showWaitingMenu(EventHandler<ActionEvent> cancelBtnHandler);

    void reset();

    Scene getScene();

}
