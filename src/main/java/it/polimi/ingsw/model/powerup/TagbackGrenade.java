package it.polimi.ingsw.model.powerup;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.actions.usepowerup.GrenadeAction;

/**
 * This class represent the TagBackGrenade powerUp
 *
 * this card will give the target one mark, can be used only on a player that has just shot you
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
     * This method apply the card to a selected target
     *
     * ( all the controls will be done in the controller)
     * @see it.polimi.ingsw.controller.PowerUpPhase#useGrenade(GrenadeAction)
     *
     * @param target is the player at which the grenade will be applied
     * @param sourcePId is the id of the player who used it
     */
    public void applyOn(Player target, int sourcePId) {

        target.addMarks( sourcePId ,1);

    }

}