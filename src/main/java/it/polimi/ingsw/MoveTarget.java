package it.polimi.ingsw;

public class MoveTarget extends MicroEffect {

    private int cellNumber;//the target can be moved before or after shooting with some kind of effects

    public MoveTarget(int n){
        this.cellNumber=n;

    }

    @Override
    public void applyOn(Player player) {

    }

    @Override
    public MicroEffect copy() {
        return null;
    }

    public int getMoves()
    {
        return this.cellNumber;
    }
    public void setMoves(int n)
    {
        this.cellNumber=n;
    }
}
