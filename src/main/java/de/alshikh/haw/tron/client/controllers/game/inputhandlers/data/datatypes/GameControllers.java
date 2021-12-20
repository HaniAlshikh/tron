package de.alshikh.haw.tron.client.controllers.game.inputhandlers.data.datatypes;

import de.alshikh.haw.tron.client.models.game.data.entities.Player;

public enum GameControllers {
    AWSD, JIKL;

    Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
