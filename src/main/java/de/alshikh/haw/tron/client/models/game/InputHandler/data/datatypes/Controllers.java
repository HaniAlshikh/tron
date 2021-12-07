package de.alshikh.haw.tron.client.models.game.InputHandler.data.datatypes;

import de.alshikh.haw.tron.client.common.data.entites.Player;

public enum Controllers {
    AWSD, JIKL;

    Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
