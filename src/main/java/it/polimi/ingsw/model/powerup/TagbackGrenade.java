package it.polimi.ingsw.model.powerup;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;

/**
 * 
 */
public class TagbackGrenade extends PowerUp {

    /**
     * Default constructor
     */
    public TagbackGrenade(Color color) {

        super(color);
        this.setType(PowerUpType.TAG_BACK_GRENADE);
    }

    /**
     *
     * @param p is the player at which the grenade will be applied
     * @throws CardNotPossessedException if the card is not possessed by anyone
     */
    public void applyOn(Player target, int sourcePId) {

        target.addMarks( sourcePId ,1);

    }

}