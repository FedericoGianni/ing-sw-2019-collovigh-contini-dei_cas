package it.polimi.ingsw.model.powerup;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Player;

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
     * @throws CardNotPossessedException if the card is not possessed by anyone
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