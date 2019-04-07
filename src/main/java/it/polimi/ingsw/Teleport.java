package it.polimi.ingsw;

import customsexceptions.CardNotPossessedException;

import java.util.*;

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
     * to be implemented
     * @param cell cell in which the player will be teleported
     */
    public void use(Cell cell, Player player) throws CardNotPossessedException {

        if(!player.getPowerUps().contains(this)){ throw new CardNotPossessedException();}
        else{

            player.setPlayerPos(cell);
        }


    }

}