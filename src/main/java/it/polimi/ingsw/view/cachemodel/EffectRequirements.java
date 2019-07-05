package it.polimi.ingsw.view.cachemodel;

import java.io.Serializable;
import java.util.List;

/**
 * This class is useful for CachedFullWeapons effect to determine which requests are needed to be asked to the User Interfaces
 * to shoot with a weapon
 */
public class EffectRequirements implements Serializable {

    /**
     * Number of targets requested, as the size of the list
     */
    private final List<Integer> numberOfTargets;

    /**
     * True if this weapon effect also requires a cell to be specified
     */
    private final Boolean cellRequired;

    public EffectRequirements(List<Integer> numberOfTargets, Boolean cellRequired) {
        this.numberOfTargets = numberOfTargets;
        this.cellRequired = cellRequired;
    }

    public List<Integer> getNumberOfTargets() {
        return numberOfTargets;
    }

    public Boolean getCellRequired() {
        return cellRequired;
    }
}
