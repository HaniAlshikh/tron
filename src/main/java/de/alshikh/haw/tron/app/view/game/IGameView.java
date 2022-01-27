package de.alshikh.haw.tron.app.view.game;

import de.alshikh.haw.tron.app.model.game.data.entities.IGame;
import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public interface IGameView {

    void showGame(IGame game);

    void highlightCell(Coordinate... cells);

    void showStartMenu(StringProperty playerName, String message,
                       EventHandler<ActionEvent> startBtnHandler, EventHandler<ActionEvent> joinBtnHandler);

    void showWaitingMenu(EventHandler<ActionEvent> cancelBtnHandler);

    void reset();

    Scene getScene();

}
