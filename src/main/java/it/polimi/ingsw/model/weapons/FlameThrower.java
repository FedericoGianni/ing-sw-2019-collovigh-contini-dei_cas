package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;

import java.util.Arrays;
import java.util.List;

public class FlameThrower extends SpecialWeapons{

    private static final String FLAMETHROWER_NAME = "FLAMETHROWER";

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
    public Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws PlayerAlreadyDeadException, WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CellNonExistentException {

        for (int i = 0; i < effects.size(); i++) {
            for(Player p : targetLists.get(i)){
                if(p.getStats().getDmgTaken().size() > KILL_DMG){
                    throw new PlayerAlreadyDeadException();
                }
            }
        }

        if ( (effects == null ) || (effects.size() != 1) || (!Arrays.asList(0,1).containsAll(effects)) ) throw new UncorrectEffectsException();

        if ( ! this.isLoaded() ) throw new WeaponNotLoadedException();

        if ( (targetLists == null) || (targetLists.stream().flatMap(List::stream).count() > 0 ) ) throw new NotCorrectPlayerNumberException();

        if ( ! playerAreInLine(isPossessedBy(),targetLists.get(0)) ) throw new UncorrectDistanceException();

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CardNotPossessedException, DifferentPlayerNeededException, CellNonExistentException, PrecedentPlayerNeededException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getReloadCost() {
        return costBaseEffect;
    }


}
