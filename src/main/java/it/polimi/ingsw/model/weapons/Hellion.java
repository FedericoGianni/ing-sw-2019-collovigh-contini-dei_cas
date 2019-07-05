package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represent the hellion Special weapon
 * @see SpecialWeapons
 */
public class Hellion extends SpecialWeapons {

    /**
     * name of the weapon
     */
    private static final String HELLION_NAME = "HELLION";

    /**
     * base dmg
     */
    private static final Integer DMG = 1;
    /**
     * marks given to the targets in base mode
     */
    private static final Integer BASIC_MODE_MARKS = 1;
    /**
     * marks given to the targets in second effect mode
     */
    private static final Integer FIRST_MODE_MARKS = 2;

    /**
     * reload cost
     */
    private List<AmmoCube> baseCost;
    /**
     * second effect cost
     */
    private List<AmmoCube> secondCost;

    /**
     * Constructor
     */
    public Hellion() {

        super(HELLION_NAME);

        baseCost = Arrays.asList(new AmmoCube(Color.RED),new AmmoCube(Color.YELLOW));

        secondCost = Arrays.asList(new AmmoCube(Color.RED));
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws PlayerAlreadyDeadException, WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CellNonExistentException {

        checkPlayerAlreadyDead(targetLists);

        if ( (effects == null ) || (effects.size() != 1) || (!Arrays.asList(0,1).containsAll(effects)) ) throw new UncorrectEffectsException();

        if ( ! this.isLoaded() ) throw new WeaponNotLoadedException();

        if ( (targetLists == null) || (targetLists.stream().flatMap(List::stream).collect(Collectors.toList()).isEmpty()) ) throw new NotCorrectPlayerNumberException();

        if ( isPossessedBy().getCurrentPosition().equals(targetLists.get(0).get(0).getCurrentPosition()) ) throw new PlayerInSameCellException();

        if ( effects.contains(1) && ( ! isPossessedBy().canPay(secondCost)) ) throw new NotEnoughAmmoException();

        if (! isPossessedBy().canSee().contains(targetLists.get(0).get(0))) throw new PlayerNotSeeableException();

        return true;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws PlayerAlreadyDeadException, WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CardNotPossessedException, DifferentPlayerNeededException, CellNonExistentException {

        if (preShoot(targetLists,effects,cells)){

            if ( effects.contains(0) ) baseEffect( targetLists.get(0).get(0) );

            else if ( effects.contains(1) ) secondEffect(targetLists.get(0).get(0));

            this.setLoaded(false);
        }
    }

    /**
     * This method will perform the first effect action
     * @param mainTarget is the target on which will be given the damage
     */
    private void baseEffect( Player mainTarget ){

        mainTarget.addDmg(isPossessedBy().getPlayerId(),DMG);

        SpecialWeapons.toAllPlayerInCell(mainTarget.getCurrentPosition(),0,BASIC_MODE_MARKS,isPossessedBy().getPlayerId());

    }

    /**
     * This method will perform the second effect action
     * @param mainTarget is the target on which will be given the damage
     */
    private void secondEffect( Player mainTarget ){

        mainTarget.addDmg(isPossessedBy().getPlayerId(),DMG);

        SpecialWeapons.toAllPlayerInCell(mainTarget.getCurrentPosition(),0,FIRST_MODE_MARKS,isPossessedBy().getPlayerId());

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getReloadCost() {
        return baseCost;
    }
}
