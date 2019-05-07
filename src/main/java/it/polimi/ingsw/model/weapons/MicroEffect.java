package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.customsexceptions.DeadPlayerException;
import it.polimi.ingsw.customsexceptions.FrenzyActivatedException;
import it.polimi.ingsw.customsexceptions.OverKilledPlayerException;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

/**
 * //TODO
 */
public abstract class MicroEffect {
    /**
     *
     */
    public MicroEffect() {

    }

    /**
     *
     * This method is set abstract because it will have different implementation in each subclass, but we wanted to make sure that each of them has it
     *
     * @param player on which the effect will be applied
     */
    public abstract void applyOn(Player player);

    /**
     * This method is set abstract because it will have different implementation in each subclass, but we wanted to make sure that each of them has their
     *
     * @return a clone of this object (but sonar does not let me use the clone name)
     */
    public abstract MicroEffect copy();

    public abstract void print();
    /**
     * @param playerList
     * apply the microEffect consequences
     */
    public abstract void microEffectApplicator(ArrayList<Player> playerList,Weapon w) throws OverKilledPlayerException, DeadPlayerException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectTargetDistance, SeeAblePlayerException, FrenzyActivatedException, DifferentPlayerNeededException, NotCorrectPlayerNumberException;
    public abstract boolean moveBefore();
}
