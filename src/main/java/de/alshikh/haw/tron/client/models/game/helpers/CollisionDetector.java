package de.alshikh.haw.tron.client.models.game.helpers;

import de.alshikh.haw.tron.client.models.game.data.entities.Game;
import de.alshikh.haw.tron.client.models.game.data.entities.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class CollisionDetector {
    private static final Logger log = LoggerFactory.getLogger(CollisionDetector.class.getSimpleName());

    // rules
    private static final Predicate<Player> theWall =
            Player::isDead;

    private static final Predicate<Player> himSelf =
            p -> p.getBike().getTrail().contains(p.getBike().getPosition());

    private static final BiPredicate<Player, Player> bothCollidedWithTheWall =
            (p1, p2) -> theWall.test(p1) && theWall.test(p2);

    private static final BiPredicate<Player, Player> otherPlayer =
            (p1, p2) -> p1.getBike().getTrail().contains(p2.getBike().getPosition());

    private static final BiPredicate<Player, Player> eachOther =
            (p1, p2) -> p1.getBike().getPosition().equals(p2.getBike().getPosition());


    public static void check(Game game) {
        game.setEnded(true);
        if (bothCollidedWithTheWall.or(eachOther).test(game.getPlayer(), game.getOpponent())) {
            log.info(game.getPlayer() + " collided with " + game.getOpponent());
        }
        else if (collidedWith(himSelf.or((p -> otherPlayer.test(p, game.getOpponent()))).or(theWall), game.getPlayer())) {
            game.setWinner(game.getOpponent());
        }
        else if (collidedWith(himSelf.or((p -> otherPlayer.test(p, game.getPlayer()))).or(theWall), game.getOpponent())) {
            game.setWinner(game.getPlayer());
        } else {
            game.setEnded(false);
        }
    }

    private static boolean collidedWith(Predicate<Player> rules, Player p) {
        return rules.test(p);
    }
}
