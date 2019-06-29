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

    private static final int DMG_FIRST_EFFECT = 1;

    private static final int DMG_SECOND_EFFECT_FIRST_CELL = 2;

    private static final int DMG_SECOND_EFFECT_SECOND_CELL = 1;

    private final List<AmmoCube> costBaseEffect;

    private final List<AmmoCube> costSecondEffect;

    public PowerGlove() {

        super(POWER_GLOVE_NAME);

        this.costBaseEffect = Arrays.asList(new AmmoCube(Color.YELLOW), new AmmoCube(Color.BLUE));
        this.costSecondEffect = Arrays.asList(new AmmoCube(Color.BLUE));

    }

    @Override
    public Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws DifferentPlayerNeededException, WeaponNotLoadedException, PlayerAlreadyDeadException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CellNonExistentException {
        return null;
    }

    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerAlreadyDeadException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CardNotPossessedException, DifferentPlayerNeededException, CellNonExistentException, PrecedentPlayerNeededException {

    }

    @Override
    public List<AmmoCube> getReloadCost() {
        return costBaseEffect;
    }
}
