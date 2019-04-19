package it.polimi.ingsw.model;

public class MoveTarget extends MicroEffect {

    private int cellNumber;//the target can be moved before or after shooting with some kind of effects
    private boolean endYouCanSee;
    //tractor beam moves the target frow everywhere to a cell you can see
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
