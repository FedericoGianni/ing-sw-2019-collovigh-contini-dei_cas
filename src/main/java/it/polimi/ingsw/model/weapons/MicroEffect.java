package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

/**
 * the micro effects, the atomic axctions of damages, marks and moving
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
     * @throws PlayerInSameCellException
     * @throws PlayerInDifferentCellException
     * @throws UncorrectDistanceException
     * @throws SeeAblePlayerException
     * @throws DifferentPlayerNeededException
     * @throws NotCorrectPlayerNumberException
     */
    public abstract void microEffectApplicator(List<Player> playerList, Weapon w, Cell c,int n) throws PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, DifferentPlayerNeededException, NotCorrectPlayerNumberException, PlayerNotSeeableException, PrecedentPlayerNeededException;

    //devo fare una copia di tutte le stats e cosi da usare qui!!!
   // public abstract void microEffectApplicatorFake(List<Player> playerList, Weapon w, Cell c) throws PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, DifferentPlayerNeededException, NotCorrectPlayerNumberException, PlayerNotSeeableException;

    public abstract boolean moveBefore();
}
