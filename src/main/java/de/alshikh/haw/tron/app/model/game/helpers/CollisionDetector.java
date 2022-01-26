package de.alshikh.haw.tron.app.model.game.helpers;

import de.alshikh.haw.tron.app.model.game.data.entities.IGame;
import de.alshikh.haw.tron.app.model.game.data.entities.IPlayer;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class CollisionDetector {
    // rules
    private static final Predicate<IPlayer> theWall =
            IPlayer::isDead;

    private static final Predicate<IPlayer> himSelf =
            p -> p.getBike().getTrail().contains(p.getBike().getPosition());

    private static final BiPredicate<IPlayer, IPlayer> bothCollidedWithTheWall =
            (p1, p2) -> theWall.test(p1) && theWall.test(p2);

    private static final BiPredicate<IPlayer, IPlayer> otherPlayer =
            (p1, p2) -> p2.getBike().getTrail().contains(p1.getBike().getPosition());

    private static final BiPredicate<IPlayer, IPlayer> eachOther =
            (p1, p2) -> p1.getBike().getPosition().equals(p2.getBike().getPosition());

    public static void check(IGame game) {
        game.setEnded(true);
        if (bothCollidedWithTheWall.or(eachOther).test(game.getPlayer(), game.getOpponent()))
            game.setWinner(null);
        else if (collidedWith(himSelf.or((p -> otherPlayer.test(p, game.getOpponent()))).or(theWall), game.getPlayer()))
            game.setWinner(game.getOpponent());
        else if (collidedWith(himSelf.or((p -> otherPlayer.test(p, game.getPlayer()))).or(theWall), game.getOpponent()))
            game.setWinner(game.getPlayer());
        else
            game.setEnded(false);
    }

    private static boolean collidedWith(Predicate<IPlayer> rules, IPlayer p) {
        return rules.test(p);
    }
}
