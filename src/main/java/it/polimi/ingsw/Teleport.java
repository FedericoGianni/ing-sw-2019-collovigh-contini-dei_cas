package it.polimi.ingsw;

import customsexceptions.CardNotPossessedException;

/**
 * 
 */
public class Teleport extends PowerUp {

    /**
     * Default constructor
     */
    public Teleport(Color color) {

        super(color);
    }

    /**
     *
     * @param cell cell in which the player will be teleported
     */
    public void use(Cell cell, Player player) throws CardNotPossessedException {

        if(!player.getPowerUps(player).contains(this)){ throw new CardNotPossessedException();}
        else{

            player.setPlayerPos(cell);
        }


    }

}