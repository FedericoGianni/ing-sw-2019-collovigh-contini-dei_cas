package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VortexCannon extends Weapon{

    private static final String NAME = "VORTEX CANNON";

    private final List<AmmoCube> costBaseEffect;

    private final List<AmmoCube> costSecondEffect;

    private boolean loaded = true;

    public VortexCannon() {

        List<AmmoCube> cost = new ArrayList<>();

        cost.add(new AmmoCube(Color.RED));
        cost.add(new AmmoCube(Color.BLUE));

        costBaseEffect = new ArrayList<>(cost);

        cost = new ArrayList<>();

        cost.add(new AmmoCube(Color.RED));

        costSecondEffect = new ArrayList<>(cost);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reload() throws NotAbleToReloadException {

        // checks if the player who owns the weapon can pay the reload

        if (!this.isPossessedBy().canPay(costBaseEffect)) throw new NotAbleToReloadException();

        else {

            try {

                // the player pays

                this.isPossessedBy().pay(costBaseEffect);

                // the weapon is set to loaded

                loaded = true;

            }catch (CardNotPossessedException e){

                throw new NotAbleToReloadException();
            }
        }
    }

    /**
     * This method will check if the player can shoot
     *
     * @param targetLists is a list of list of target for each effect of the weapon
     * @param effects is a list containing int correspondents to which effect will be used
     * @param cells is a list of cell
     * @return true if the shoot can be preformed, false otherwise
     * @throws WeaponNotLoadedException
     * @throws PlayerInSameCellException
     * @throws PlayerInDifferentCellException
     * @throws UncorrectDistanceException
     * @throws SeeAblePlayerException
     * @throws UncorrectEffectsException
     * @throws NotCorrectPlayerNumberException
     * @throws PlayerNotSeeableException
     * @throws NotEnoughAmmoException
     */
    public Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, UncorrectDistanceException, UncorrectEffectsException, NotCorrectPlayerNumberException, NotEnoughAmmoException {

        // if the player shoot with effect 2 but no effect 1 throw exception

        if (effects.contains(1) && !effects.contains(0)) throw new UncorrectEffectsException();

        if (!this.loaded) throw new WeaponNotLoadedException();

        List<Player> targets = new ArrayList<>();

        targets.addAll(targetLists.get(0));
        targets.addAll((targetLists.size() <= 1) ? new ArrayList<>() : targetLists.get(1));

        Set<Player> set = new HashSet<>(targets);

        // if player are duplicates throw exception

        if (set.size() < targets.size()) throw new NotCorrectPlayerNumberException();

        // check if first effect is doable

        checkBaseEffect(targetLists.get(0).get(0), cells.get(0));

        // check if second effect is doable

        if (effects.contains(1)) checkSecondEffect(targetLists.get(1),cells.get(0));

        return true;
    }


    /**
     * This method check if parameters given are good for base effect
     * @param target is the player targeted
     * @param vortex is the cell containing the vortex
     * @throws NotCorrectPlayerNumberException if there is no target specified
     * @throws UncorrectDistanceException if the target is not in the vortex radius
     */
    private void checkBaseEffect(Player target, Cell vortex)throws  NotCorrectPlayerNumberException, UncorrectDistanceException{

        if (target == null) throw new NotCorrectPlayerNumberException();

        if ((target.getCurrentPosition() == null ) || (!genVortexSurrounding(vortex).contains(target.getCurrentPosition()))) throw new UncorrectDistanceException();

    }

    /**
     * This method check if parameters given are good for second effect
     * @param targets are the player targeted
     * @param vortex is the cell containing the vortex
     * @throws NotCorrectPlayerNumberException if targets are not 1 nor 2
     * @throws UncorrectDistanceException if the targets are not in the vortex radius
     * @throws NotEnoughAmmoException if the player can not pay the effect
     */
    private void checkSecondEffect(List<Player> targets, Cell vortex)throws  NotCorrectPlayerNumberException, UncorrectDistanceException, NotEnoughAmmoException{

        if (!isPossessedBy().canPay(costSecondEffect)) throw new NotEnoughAmmoException();

        if (targets == null) throw new NotCorrectPlayerNumberException();

        if ( (targets.size()!= 1) && (targets.size() != 2) ) throw new NotCorrectPlayerNumberException();

        for (Player target : targets){

            if ((target.getCurrentPosition() == null ) || (!genVortexSurrounding(vortex).contains(target.getCurrentPosition()))) throw new UncorrectDistanceException();

        }

    }

    /**
     *
     * @param vortex is the vortex center
     * @return a list of cell that are in the vortex radius
     */
    private List<Cell> genVortexSurrounding(Cell vortex){

        List<Cell> vortexSurrounding = new ArrayList<>();

        vortexSurrounding.add(vortex);

        vortexSurrounding.add(vortex.getNorth());
        vortexSurrounding.add(vortex.getEast());
        vortexSurrounding.add(vortex.getWest());
        vortexSurrounding.add(vortex.getSouth());

        return vortexSurrounding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException,NotEnoughAmmoException {

        preShoot(targetLists,effects,cells);

        // do the basic shot

        baseEffect(targetLists.get(0).get(0), cells.get(0));

        if (effects.contains(1)) secondEffect(targetLists.get(1),cells.get(0));

        this.loaded = false;
    }

    /**
     * This method will perform the base effect shoot
     * @param target  is the player targeted
     * @param vortex is the cell containing the vortex
     */
    private void baseEffect(Player target, Cell vortex){

        if (!target.getCurrentPosition().equals(vortex)){

            // if the player is not in the vortex cell set so

            target.setPlayerPos(vortex);

        }

        target.addDmg(this.isPossessedBy().getPlayerId(),2);

    }

    /**
     * This method will perform the second effect shoot
     * @param targets  are the player targeted
     * @param vortex is the cell containing the vortex
     */
    private void secondEffect(List<Player> targets, Cell vortex) {

        for (Player target : targets){

            if (!target.getCurrentPosition().equals(vortex)){

                // if the player is not in the vortex cell set so

                target.setPlayerPos(vortex);

            }

            target.addDmg(this.isPossessedBy().getPlayerId(),1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void print() {

        throw new UnsupportedOperationException();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getCost() {
        return costBaseEffect.subList(1,costBaseEffect.size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AmmoCube> getReloadCost() {
        return costBaseEffect;
    }
}
