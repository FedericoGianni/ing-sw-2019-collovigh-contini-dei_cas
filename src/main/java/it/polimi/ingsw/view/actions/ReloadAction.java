package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.util.ArrayList;
import java.util.List;

/**
 * this class do the reload actions
 */
public class ReloadAction extends JsonAction {
    /**
     * contians the weapons i want to reload
     */
    private final List<String> weapons;
    /**
     * contains the powerUps i want to discard eventually
     */
    private final List<CachedPowerUp> powerUps;

    /**
     * reload weapons and pay in ammos and with powerUps
     * @param weapons thos eare the weapons i want to reload
     * @param powerUps;
     */
    public ReloadAction(List<String> weapons,List<CachedPowerUp> powerUps) {

        super(ActionTypes.RELOAD);

        this.weapons = weapons;
        this.powerUps = powerUps;

    }

    /**
     * @return the weapons
     */
    public List<String> getWeapons() {
        return new ArrayList<>(weapons);
    }

    /**
     * @return the powerups
     */
    public List<CachedPowerUp> getPowerUps() {
        return new ArrayList<>(powerUps);
    }
}
