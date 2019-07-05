package it.polimi.ingsw.model.player;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;

import java.util.List;

/**
 * this interface represent a bag of elements of type T
 * @param <T> is the type of element
 *
 * it is implemented by:
 * @see WeaponBag
 * @see AmmoBag
 * @see PowerUpBag
 */
public interface Bag <T> {

     /**
      *
      * @return the list of elements contained in the bag
      */
     List<T> getList();

     /**
      * adds the element to the bag
      * @param item is the element to add
      */
     void addItem(T item);

     /**
      * This method will delete an item from the bag
      * @param item is the item specified
      * @return the only reference to the element
      * @throws CardNotPossessedException if the element is not found
      */
     T getItem( T item) throws CardNotPossessedException;

     /**
      * checks if a given element is contained in the bag
      * @param item is the queried element
      * @return true if contained, false otherwise
      */
     Boolean hasItem( T item);

}
