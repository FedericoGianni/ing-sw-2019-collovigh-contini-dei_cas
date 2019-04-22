package it.polimi.ingsw.model;

import customsexceptions.CardNotPossessedException;

/**
 * 
 */
public class Teleporter extends PowerUp {

    /**
     * Default constructor
     */
    public Teleporter(Color color) {

        super(color);
    }

    /**
     *
     * @param cell cell in which the player will be teleported
     */
    public void use(Cell cell) throws CardNotPossessedException {

        Boolean b = false;
        Player p = null;

        for (Player player: Model.getGame().getPlayers()){

            if (player.getPowerUpBag().hasItem(this)) {
                b = true;
                p=player;
            }

        }

        if(!b){ throw new CardNotPossessedException();}
        else{

            p.setPlayerPos(cell);
        }


    }

}