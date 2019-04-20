package it.polimi.ingsw.model;

import customsexceptions.CardNotPossessedException;

import java.util.List;

/**
 * 
 */
public class TagbackGrenade extends PowerUp {

    /**
     * Default constructor
     */
    public TagbackGrenade(Color color) {

        super(color);
    }

    /**
     *
     * @param p is the player at which the grenade will be applied
     */
    public void applyOn(Player p) throws CardNotPossessedException{

        List<Player> playerList =  Model.getGame().getPlayers();
        Boolean possessed = false;

        for (Player player: playerList){

            if (player.getPowerUpBag().hasItem(this)){

                p.addMarks(Model.getGame().playerToId(player),1);

                possessed = true;
            }
        }

        if (!possessed) throw new CardNotPossessedException();

    }

}