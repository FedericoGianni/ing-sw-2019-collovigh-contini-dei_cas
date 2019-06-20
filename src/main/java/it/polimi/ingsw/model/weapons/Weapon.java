package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.AmmoBag;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Weapon implements Serializable {

    private Integer firstTarget;//first target of every shot , useful for checking  if we need to retarget players or similar

    /**
     * Abstract class, no constructor
     */
    public Weapon(){}
    /**
     * @return Boolean if the weapon is loaded
     */
    public abstract boolean isLoaded();

    /**
     * reloads the weapon
     */
    public abstract void reload() throws NotAbleToReloadException ;



    public Player getFirstTarget() { return Model.getPlayer(firstTarget); }//useful when you have to target  different target
    public void setFirstTarget(Player firstTarget) {
        this.firstTarget = firstTarget.getPlayerId();
    }

    public final Player isPossessedBy(){

        List<Player> list = Model.getGame().getPlayers().stream()
                .filter(player -> player.getCurrentWeapons().hasItem(this))
                .collect(Collectors.toList());

        return (list.isEmpty()) ? null : list.get(0);
    }

    /**
     * this method checks if the shoot action can be done
     * a list of itnegers who enumbers the effects (0 to 3) for every effects you have a list of targets
     * cells because you may move a target with move microEffects
     * @param targetLists
     * @param effects
     * @param cells
     * @throws WeaponNotLoadedException
     * @throws OverKilledPlayerException
     * @throws DeadPlayerException
     * @throws PlayerInSameCellException
     * @throws PlayerInDifferentCellException
     * @throws UncorrectDistanceException
     * @throws SeeAblePlayerException
     * @throws FrenzyActivatedException
     */
    public abstract Boolean preShoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, DifferentPlayerNeededException, CardNotPossessedException, NotEnoughAmmoException;//may need to be changed


    /**
     * this method is the method that leads all weapons to the shooting action
     * a list of itnegers who enumbers the effects (0 to 3) for every effects you have a list of targets
     * cells because you may move a target with move microEffects
     * @param targetLists
     * @param effects
     * @param cells
     * @throws WeaponNotLoadedException
     * @throws OverKilledPlayerException
     * @throws DeadPlayerException
     * @throws PlayerInSameCellException
     * @throws PlayerInDifferentCellException
     * @throws UncorrectDistanceException
     * @throws SeeAblePlayerException
     * @throws FrenzyActivatedException
     */
    public abstract void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException, NotEnoughAmmoException, CardNotPossessedException, DifferentPlayerNeededException;//may need to be changed

    /**
     * @param cost
     * @param possessed
     * @return true if the player can pay something in ammo
     */
    public final boolean canPay(List<AmmoCube> cost, AmmoBag possessed) {

        List<Color> cash = possessed
                .getList()
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());

        List<Color> required = cost
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());


        return cash.containsAll(required);
    }

    /**
     * print some infos about weapons--useful for client infos screening
     */
    public abstract void print();

    /**
     * name of the ammo
     * @return
     */
    public abstract String getName();

    public abstract List<AmmoCube> getCost();

    public abstract List<AmmoCube> getReloadCost();

}
