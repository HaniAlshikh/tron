package de.alshikh.haw.tron.app.model.lobby.data.datatypes;

import de.alshikh.haw.tron.app.controller.game.helpers.IGameUpdater;
import de.alshikh.haw.tron.app.model.game.data.entities.IPlayer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerUpdateChannel implements IPlayerUpdateChannel {

    private final IPlayer player;
    private final IGameUpdater gameUpdater;
    private final Consumer<String> gameStarter;

    public PlayerUpdateChannel(IPlayer player, IGameUpdater gameUpdater, Consumer<String> gameStarter) {
        this.player = player;
        this.gameUpdater = gameUpdater;
        this.gameStarter = gameStarter; // TD08
    }

    @Override
    public void addListener(InvalidationListener opponentUpdateChannel) {
        player.getUpdate().addListener(opponentUpdateChannel);
        gameStarter.accept(((IPlayerUpdateChannel) opponentUpdateChannel).getPlayerName());
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        player.getUpdate().removeListener(listener);
    }

    @Override
    public void invalidated(Observable observable) {
        gameUpdater.invalidated(observable);
    }

    @Override
    public String getPlayerName() {
        return player.getName();
    }

    @Override
    public UUID getPlayerId() {
        return player.getId();
    }
}
