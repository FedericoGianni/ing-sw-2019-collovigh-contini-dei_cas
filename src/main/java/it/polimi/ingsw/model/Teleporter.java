package it.polimi.ingsw.model;

import customsexceptions.CardNotPossessedException;
import customsexceptions.CellNonExistentException;

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
    public void use(Cell cell) throws CardNotPossessedException, CellNonExistentException {

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

            if (cell == null) {throw new CellNonExistentException();}

            else p.setPlayerPos(cell);
        }


    }

}