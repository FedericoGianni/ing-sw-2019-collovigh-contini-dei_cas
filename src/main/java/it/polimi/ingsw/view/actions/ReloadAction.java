package it.polimi.ingsw.view.actions;

import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.util.ArrayList;
import java.util.List;

public class ReloadAction extends JsonAction {

    private final List<String> weapons;

    private final List<CachedPowerUp> powerUps;

    public ReloadAction(List<String> weapons,List<CachedPowerUp> powerUps) {

        super(ActionTypes.RELOAD);

        this.weapons = weapons;
        this.powerUps = powerUps;

    }

    public List<String> getWeapons() {
        return new ArrayList<>(weapons);
    }

    public List<CachedPowerUp> getPowerUps() {
        return new ArrayList<>(powerUps);
    }
}
