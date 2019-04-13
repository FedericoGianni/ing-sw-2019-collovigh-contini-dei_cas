package it.polimi.ingsw;

import java.util.*;

/**
 * 
 */
public class WeaponDeck implements Deck <Weapon> {

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
        this.weaponDeck = new ArrayList<>();
        for(Weapon w : clone.weaponDeck){
            this.weaponDeck.add(w);
        }
    }

    /**
     * 
     */
    private List<Weapon> weaponDeck;



    /**
     * 
     */
    public Weapon getRandomCard() {
        // TODO implement here

        return null;
    }

}