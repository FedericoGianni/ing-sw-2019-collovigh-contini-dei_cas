package it.polimi.ingsw.view.updates.otherplayerturn;

public class ShootTurnUpdate extends TurnUpdate{

    private final int TargetId;
    private final String weapon;

    public ShootTurnUpdate( int playerId, int targetId, String weapon) {
        super(TurnUpdateType.SHOOT, playerId);
        TargetId = targetId;
        this.weapon = weapon;
    }

    public int getTargetId() {
        return TargetId;
    }

    public String getWeapon() {
        return weapon;
    }
}
