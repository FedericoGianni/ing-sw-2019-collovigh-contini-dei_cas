package it.polimi.ingsw.model;

import java.util.*;

/**
 * 
 */
public class WeaponDeck implements Deck <NormalWeapon> {

    /**
     * Default constructor
     */
    public WeaponDeck() {
    }

    /**
     * This constructor is used to generate a copy instance of WeaponDeck
     * @param clone WeaponDeck instance to be copied
     */
    public WeaponDeck(WeaponDeck clone){
        this.normalWeaponDeck = new ArrayList<>();
        for(NormalWeapon w : clone.normalWeaponDeck){
            this.normalWeaponDeck.add(w);
        }
    }

    /**
     * 
     */
    private List<NormalWeapon> normalWeaponDeck;



    /**
     * 
     */
    public NormalWeapon getRandomCard() {
        // TODO implement here

        return null;
    }

}