package it.polimi.ingsw.model;

import it.polimi.ingsw.view.virtualView.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer){

        this.observers.add(observer);
    }

    public void removeObserver(Observer observer){

        this.observers.add(observer);
    }

    public void updateAll(Object object){

        for (Observer observer: observers){

            observer.update(object);
        }
    }



}
