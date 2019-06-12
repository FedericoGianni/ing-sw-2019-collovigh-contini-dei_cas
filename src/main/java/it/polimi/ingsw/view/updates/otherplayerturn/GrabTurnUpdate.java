package it.polimi.ingsw.view.updates.otherplayerturn;



public class GrabTurnUpdate extends TurnUpdate{

    private final String weapon;

    public GrabTurnUpdate( int playerId) {
        super(TurnUpdateType.GRAB, playerId);
        weapon = null;
    }

    public GrabTurnUpdate( int playerId, String weapon) {
        super(TurnUpdateType.GRAB, playerId);
        this.weapon = weapon;
    }

    public String getWeapon() {
        return weapon;
    }
}
