package it.polimi.ingsw.view.virtualView.observers;

public interface Observer {

     void update(Object object);

     void updateSinge(int playerId, Object object);
}
