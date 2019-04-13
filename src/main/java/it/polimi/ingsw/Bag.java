package it.polimi.ingsw;

import java.util.List;

public interface Bag <T> {

     List<T> getList();

     void addItem(T item);

     T getItem( T item);

     Boolean hasItem( T item);


}
