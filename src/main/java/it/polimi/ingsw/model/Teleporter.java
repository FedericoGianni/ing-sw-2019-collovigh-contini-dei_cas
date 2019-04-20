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
    public void use(Cell cell, Player player) throws CardNotPossessedException {

        if(!player.getPowerUps().contains(this)){ throw new CardNotPossessedException();}
        else{

            player.setPlayerPos(cell);
        }


    }

}