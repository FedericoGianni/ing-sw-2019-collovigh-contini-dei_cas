package it.polimi.ingsw.view.cachemodel.sendables;




import it.polimi.ingsw.view.UiHelpers;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.util.List;

/**
 * Simplified version of the WeaponBag model class to store the weapons possesed by players
 */
public class CachedWeaponBag extends UpdateClass {

    /**
     * List of weapon's name of the player who has this weapon bag
     */
    private final List<String> weapons;

    /**
     * List of boolean which are true if the corrisponding weapon in the weapons list is loaded or not
     */
    private final List<Boolean> loaded;

    public CachedWeaponBag(List<String> weapons, List<Boolean> loaded, int playerId) {

        super(UpdateType.WEAPON_BAG,playerId);

        this.weapons = weapons;

        this.loaded = loaded;
    }

    public List<String> getWeapons() {
        return weapons;
    }

    public List<Boolean> getLoaded() { return loaded; }

    @Override
    public String toString(){
        String s = "";

        if(weapons != null){
            for (int i = 0; i < weapons.size(); i++) {
                s = s.concat(i + " :" + UiHelpers.weaponTranslator(weapons.get(i)));
                if(loaded.get(i).equals(true)) {
                    s = s.concat(" carica.\n");
                } else {
                    s = s.concat(" scarica.\n");
                }
            }
        }

        return s;
    }
}
