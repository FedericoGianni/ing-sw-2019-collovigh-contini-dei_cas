package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.CurrentGame;
import it.polimi.ingsw.view.cachemodel.sendables.CachedGame;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.virtualView.VirtualView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CurrentGameObserver implements Observer {

    private CurrentGame game;
    private CurrentGame previous;
    private Observers observers;

    @Override
    public synchronized void update(Object object) {

        // moves the previous saved current game

        previous = game;

        // cast the Object in its dynamic type

        this.game = new CurrentGame((CurrentGame) object);

        // check if frenzy has begun

        checkFrenzy(game);

        // translates the killShotTrack into a list of Point

        List<Point> killShotTrack = extractKillShotTrack((CurrentGame) object);

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedGame(killShotTrack);

        // send the update to all Virtual Views

        for (VirtualView virtualView : observers.getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }
    }

    @Override
    public synchronized void updateSinge(int playerId, Object object) {

        // moves the previous saved current game

        previous = game;

        // cast the Object in its dynamic type

        this.game = new CurrentGame((CurrentGame) object);

        // check if frenzy has begun

        checkFrenzy(game);

        // translates the killShotTrack into a list of Point

        List<Point> killShotTrack = extractKillShotTrack((CurrentGame) object);

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedGame(killShotTrack);

        // send the update to the selected player's virtual View

        observers
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);

    }


    private List<Point> extractKillShotTrack(CurrentGame game){

        List<Point> killShotTrack = new ArrayList<>();

        for (int i = 0; i < game.getKillShotTrack().size(); i++) {

            killShotTrack.add(new Point(
                    game.getKillShotTrack().get(i).getKillerId(),
                    game.getKillShotTrack().get(i).getAmount()
            ));

        }

        return killShotTrack;

    }

    private void checkFrenzy(CurrentGame game){

        // if the killShotTrack goes over the max

        if ((previous.getKillShotTrack().size() < previous.getSkulls()) && (game.getKillShotTrack().size() >= previous.getSkulls())){

            // activate frenzy mode

            observers.getController().activateFrenzy();
        }
    }

    public void setObservers(Observers observers) {
        this.observers = observers;
    }
}
