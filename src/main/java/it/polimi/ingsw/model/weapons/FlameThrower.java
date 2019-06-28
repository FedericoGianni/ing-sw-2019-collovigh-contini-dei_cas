package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlameThrower extends SpecialWeapons{

    private static final String FLAMETHROWER_NAME = "FLAMETHROWER";

    private static final int DMG_FIRST_EFFECT = 1;

    private static final int DMG_SECOND_EFFECT_FIRST_CELL = 2;

    private static final int DMG_SECOND_EFFECT_SECOND_CELL = 1;

    private final List<AmmoCube> costBaseEffect;

    private final List<AmmoCube> costSecondEffect;

    public FlameThrower() {

        super(FLAMETHROWER_NAME);

        this.costBaseEffect = Arrays.asList(new AmmoCube(Color.RED));
        this.costSecondEffect = Arrays.asList(new AmmoCube(Color.YELLOW),new AmmoCube(Color.YELLOW));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws DifferentPlayerNeededException,PlayerAlreadyDeadException, WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CellNonExistentException {

        if ( (effects == null ) || (effects.size() != 1) || (!Arrays.asList(0,1).containsAll(effects)) ) throw new UncorrectEffectsException();

        if ( ! this.isLoaded() ) throw new WeaponNotLoadedException();

        if ( (targetLists == null) || ( (targetLists.stream().flatMap(List::stream).count() != 1) && (targetLists.stream().flatMap(List::stream).count() != 2) ) ) throw new NotCorrectPlayerNumberException();

        if ( targetLists.stream().flatMap(List::stream).collect(Collectors.toList()).get(0).equals(targetLists.stream().flatMap(List::stream).collect(Collectors.toList()).get(1)) ) throw new DifferentPlayerNeededException();

        if ( ! playerAreInLine(isPossessedBy(),targetLists.get(0)) ) throw new UncorrectDistanceException();

        if (effects.contains(1) && !isPossessedBy().canPay(costSecondEffect)) throw new NotEnoughAmmoException();

        checkPlayerAlreadyDead(targetLists);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws PlayerAlreadyDeadException, WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CardNotPossessedException, DifferentPlayerNeededException, CellNonExistentException, PrecedentPlayerNeededException {

        if (preShoot(targetLists, effects, cells)){

            if (effects.contains(0)) baseEffect(targetLists.get(0));

            else if (effects.contains(1)) firstEffect(targetLists.get(0));
        }
    }

    /**
     * This method will perform the base effect
     * @param targets is a list of 1 or 2 player that will be targeted
     */
    private void baseEffect(List<Player> targets){

        targets.get(0).addDmg(isPossessedBy().getPlayerId(),DMG_FIRST_EFFECT);

        if (targets.size() > 1) {

            targets.get(1).addDmg(isPossessedBy().getPlayerId(), DMG_FIRST_EFFECT);

        }
    }

    /**
     * This method will perform the second effect
     * @param targets is a list of 1 or 2 player that will be targeted
     */
    private void firstEffect(List<Player> targets){

        toAllPlayerInCell(targets.get(0).getCurrentPosition(),DMG_SECOND_EFFECT_FIRST_CELL,0,isPossessedBy().getPlayerId());

        if (targets.size() > 1){

            toAllPlayerInCell(targets.get(1).getCurrentPosition(),DMG_SECOND_EFFECT_SECOND_CELL,0,isPossessedBy().getPlayerId());

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
