package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;

import java.util.Arrays;
import java.util.List;

public class PowerGlove extends SpecialWeapons {

    private static final String POWER_GLOVE_NAME = "POWER GLOVE";

    private static final int DMG_BASE_EFFECT = 1;

    private static final int MARK_BASE_EFFECT = 2;

    private static final int DMG_SECOND_EFFECT = 2;

    private final List<AmmoCube> costBaseEffect;

    private final List<AmmoCube> costSecondEffect;

    public PowerGlove() {

        super(POWER_GLOVE_NAME);

        this.costBaseEffect = Arrays.asList(new AmmoCube(Color.YELLOW), new AmmoCube(Color.BLUE));
        this.costSecondEffect = Arrays.asList(new AmmoCube(Color.BLUE));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws DifferentPlayerNeededException, WeaponNotLoadedException, PlayerAlreadyDeadException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CellNonExistentException {

        if ( (effects == null ) || (effects.size() != 1) || (!Arrays.asList(0,1).containsAll(effects)) ) throw new UncorrectEffectsException();

        if ( ! this.isLoaded() ) throw new WeaponNotLoadedException();

        if ( targetLists == null ) throw new NotCorrectPlayerNumberException();

        checkPlayerAlreadyDead(targetLists);

        if (effects.contains(0)) return checkBaseEffect(targetLists.get(0));

        if (effects.contains(1)) return checkSecondEffect(targetLists.get(0));

        return true;
    }

    /**
     * This method will check if the parameters for the base shot are valid
     * @param targets is the list of targets
     * @return true if the parameters are validated
     * @throws NotCorrectPlayerNumberException if the targets are more than one
     * @throws UncorrectDistanceException if the target is not in a cell adj to the shooter's one
     */
    private Boolean checkBaseEffect(List<Player> targets) throws  NotCorrectPlayerNumberException, UncorrectDistanceException {

        if (targets.size() != 1 ) throw new NotCorrectPlayerNumberException();

        if ( isPossessedBy().getCurrentPosition().getAdjacencienceDirection(targets.get(0).getCurrentPosition()) == null ) throw new UncorrectDistanceException();

        return true;
    }

    /**
     * This method will check if the parameters for the second effect shot are valid
     * @param targets is the list of targets
     * @return true if the parameters are validated
     * @throws NotEnoughAmmoException if the player could not pay for the second effect
     * @throws NotCorrectPlayerNumberException if the specified targets are not 1 nor 2
     * @throws UncorrectDistanceException if the targets are not in aline of adj cells
     */
    private Boolean checkSecondEffect(List<Player> targets) throws NotEnoughAmmoException, NotCorrectPlayerNumberException, UncorrectDistanceException {

        if (! isPossessedBy().canPay(costSecondEffect) ) throw new NotEnoughAmmoException();

        if ( targets.isEmpty() || targets.size() > 2 ) throw new NotCorrectPlayerNumberException();

        if ( ! playerAreInLine(isPossessedBy(),targets) ) throw new UncorrectDistanceException();

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerAlreadyDeadException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CardNotPossessedException, DifferentPlayerNeededException, CellNonExistentException, PrecedentPlayerNeededException {

        if (preShoot(targetLists, effects, cells)){

            if (effects.contains(0)) shootBase(targetLists.get(0));

            if (effects.contains(1)) shootSecond(targetLists.get(0));
        }
    }

    /**
     * This method perform the base shot
     * @param targets is the list of targets
     */
    private void shootBase(List<Player> targets){

        isPossessedBy().setPlayerPos(targets.get(0).getCurrentPosition());

        targets.get(0).addDmg(isPossessedBy().getPlayerId(),DMG_BASE_EFFECT);
        targets.get(0).addMarks(isPossessedBy().getPlayerId(),MARK_BASE_EFFECT);
    }

    /**
     * This method perform the second effect shot
     * @param targets is the list of targets
     */
    private void shootSecond(List<Player> targets){

        for (Player target : targets){

            isPossessedBy().setPlayerPos(target.getCurrentPosition());

            targets.get(0).addDmg(isPossessedBy().getPlayerId(),DMG_SECOND_EFFECT);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getReloadCost() {
        return costBaseEffect;
    }
}
