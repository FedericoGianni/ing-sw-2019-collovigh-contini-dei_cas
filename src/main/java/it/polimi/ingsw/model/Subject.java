package it.polimi.ingsw.model;

import it.polimi.ingsw.view.virtualView.observers.Observer;
import it.polimi.ingsw.view.virtualView.observers.Observers;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for the Observer pattern: every class that extends this will be Observable
 */
public abstract class Subject {

    /**
     * observer list
     */
    private List<Observer> observers = new ArrayList<>();

    /**
     *
     * @return the observer list
     */
    public List<Observer> getObservers() {
        return observers;
    }

    /**
     *
     * @param observer is the observer to add to the class
     */
    public void addObserver(Observer observer){

        if (Observers.isInitialized()) {

            this.observers.add(observer);

        }
    }

    /**
     *
     * @param observer is the observer to remove
     */
    public void removeObserver(Observer observer){

        this.observers.remove(observer);
    }

    /**
     *
     * @param object is the object to notify the update on
     */
    public void updateAll(Object object){

        if (Observers.isInitialized()) {


            for (Observer observer : observers) {

                observer.update(object);
            }

        }
    }

    /**
     *
     * @param object is the object updated
     * @param playerId is the id of the player to notify
     */
    public void updateSingle(Object object, int playerId){

        if (Observers.isInitialized()){

            for (Observer observer : observers) {

                observer.updateSinge(playerId,object);
            }
        }
    }




}
