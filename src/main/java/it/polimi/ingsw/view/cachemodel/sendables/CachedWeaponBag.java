package it.polimi.ingsw.view.cachemodel.sendables;



import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.util.List;

public class CachedWeaponBag extends UpdateClass {

    private final List<String> weapons;
    private final List<Boolean> loaded;

    public CachedWeaponBag(List<String> weapons, List<Boolean> loaded, int playerId) {

        super(UpdateType.POWERUP_BAG,playerId);

        this.weapons = weapons;

        this.loaded = loaded;
    }

    public List<String> getWeapons() {
        return weapons;
    }

    public List<Boolean> getLoaded() { return loaded; }

    @Override
    public String toString(){

        String s = null;

        if(weapons != null){
            for (int i = 0; i < weapons.size(); i++) {
                s.concat(i + " :" + weapons.get(i));
                if(loaded.get(i).equals(true))
                    s.concat(" carica.\n");
                else
                    s.concat(" scarica.\n");
            }
        }

        return s;
    }
}
