package it.polimi.ingsw.view.updates.otherplayerturn;


import it.polimi.ingsw.model.Color;

import java.util.ArrayList;
import java.util.List;

public class GrabTurnUpdate extends TurnUpdate{

    private final String weapon;
    private final List<Color> ammo = new ArrayList<>();

    public GrabTurnUpdate( int playerId, List<Color> ammo) {
        super(TurnUpdateType.GRAB, playerId);
        weapon = null;
        this.ammo.addAll(ammo);
    }

    public GrabTurnUpdate( int playerId, String weapon) {
        super(TurnUpdateType.GRAB, playerId);
        this.weapon = weapon;
    }

    public String getWeapon() {
        return weapon;
    }

    public List<Color> getAmmo() {

        return ammo;
    }
}
