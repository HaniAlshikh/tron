package de.alshikh.haw.tron.app.view.game;

import de.alshikh.haw.tron.manager.Config;
import de.alshikh.haw.tron.app.model.game.data.entities.IGame;
import de.alshikh.haw.tron.app.model.game.data.entities.IPlayer;
import de.alshikh.haw.tron.app.view.game.overlayes.StartMenu;
import de.alshikh.haw.tron.app.view.game.overlayes.WatingMenu;
import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Arrays;

public class GameView implements IGameView {

    private final StartMenu startMenu;
    private final WatingMenu watingMenu;

    private final ITronView view;

    public GameView(ITronView view) throws IOException {
        this.view = view;

        startMenu = new StartMenu(Config.MENU_CSS);
        view.registerOverlay("start", startMenu);

        watingMenu = new WatingMenu(Config.MENU_CSS);
        view.registerOverlay("wait", watingMenu);

        view.init();
    }

    @Override
    public void showGame(IGame game) {
        for (IPlayer p : Arrays.asList(game.getPlayer(), game.getOpponent())) {
            try {
                view.draw(p.getBike().getCoordinates(), p.getBike().getColor());
            } catch (IllegalArgumentException e) { // went out of bound
                p.die();
            }
        }
    }

    @Override
    public void highlightCell(Coordinate... cells) {
        Arrays.stream(cells).forEach(view::highlightCell);
    }

    @Override
    public void showStartMenu(StringProperty playerName, String message,
                              EventHandler<ActionEvent> startBtnHandler, EventHandler<ActionEvent> joinBtnHandler) {
        startMenu.getTxtPlayerName().textProperty().bindBidirectional(playerName);
        startMenu.getLblMessage().textProperty().set(message);
        startMenu.getBtnStart().setOnAction(startBtnHandler);
        startMenu.getBtnJoin().setOnAction(joinBtnHandler);
        view.showOverlay("start");
    }

    @Override
    public void showWaitingMenu(EventHandler<ActionEvent> cancelBtnHandler) {
        watingMenu.getBtnCancel().setOnAction(cancelBtnHandler);
        view.init();
        view.showOverlay("wait");
    }

    @Override
    public void reset() {
        view.init();
    }

    @Override
    public Scene getScene() {
        return view.getScene();
    }
}
