package it.polimi.ingsw;

public class Damage extends MicroEffect {

    private int playerNum;//some effects can deal damage to more than 1 player
    private boolean seeAbleTargetNeeded; //some effects can target unSeeable players
    private boolean meelee;//some weapons can deal damage to players only in your current cell

    public Damage() {
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public boolean isSeeAbleTargetNeeded() {
        return seeAbleTargetNeeded;
    }

    public void setSeeAbleTargetNeeded(boolean seeAbleTargetNeeded) {
        this.seeAbleTargetNeeded = seeAbleTargetNeeded;
    }

    public boolean isMeelee() {
        return meelee;
    }

    public void setMeelee(boolean meelee) {
        this.meelee = meelee;
    }



    @Override
    public void applyOn(Player player) {

    }

    @Override
    public MicroEffect copy() {
        return null;
    }

}
