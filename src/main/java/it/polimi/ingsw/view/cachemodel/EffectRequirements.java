package it.polimi.ingsw.view.cachemodel;

import java.io.Serializable;
import java.util.List;

public class EffectRequirements implements Serializable {

    private final List<Integer> numberOfTargets;

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
