package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.util.List;

import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.*;

public class CachedAmmoBag extends UpdateClass {

    private final List<Color> ammoList;

    public CachedAmmoBag(List<Color> ammoList, int playerId) {

        super(UpdateType.AMMO_BAG,playerId);

        this.ammoList = ammoList;


    }

    public List<Color> getAmmoList() {
        return ammoList;
    }

    @Override
    public String toString(){

        String s = new String();

        if(ammoList != null) {

            for (Color c : ammoList) {

                switch (c){

                    case BLUE:
                        s = s.concat(ANSI_BLUE.escape());
                        break;

                    case RED:
                        s = s.concat(ANSI_RED.escape());
                        break;

                    case YELLOW:
                        s = s.concat(ANSI_YELLOW.escape());
                        break;

                    default:
                        s = s.concat(ANSI_WHITE.escape());
                }

                s = s.concat("°" + ANSI_RESET.escape());
            }

        } else {
            s = s.concat("nessuna");
        }

        return s;
    }
}
