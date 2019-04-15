package it.polimi.ingsw.model;

import java.util.*;

/**
 * This class is an interface that will be implemented by AmmoDeck and PowerUpDeck
 */
public interface Deck<T> {

    /**
     * this method is meant to draw a card from the deck casually, meaning that the decks will never be shuffled
     */
    public T getRandomCard();

}