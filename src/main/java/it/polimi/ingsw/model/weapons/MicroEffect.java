package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.customsexceptions.DeadPlayerException;
import it.polimi.ingsw.customsexceptions.FrenzyActivatedException;
import it.polimi.ingsw.customsexceptions.OverKilledPlayerException;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.List;

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
     *
     * @param playerList
     * @param w
     * @param c requires cell where to move in case you move, is null if you use a non mover microeffect
     * @throws OverKilledPlayerException
     * @throws DeadPlayerException
     * @throws PlayerInSameCellException
     * @throws PlayerInDifferentCellException
     * @throws UncorrectDistanceException
     * @throws SeeAblePlayerException
     * @throws FrenzyActivatedException
     * @throws DifferentPlayerNeededException
     * @throws NotCorrectPlayerNumberException
     */
    public abstract void microEffectApplicator(List<Player> playerList, Weapon w, Cell c) throws PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, DifferentPlayerNeededException, NotCorrectPlayerNumberException;
    public abstract boolean moveBefore();
}
