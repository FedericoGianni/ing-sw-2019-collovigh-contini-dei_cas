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
    private Observers observers;


    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.game = (CurrentGame) object;

        // translates the killShotTrack into a list of Point

        List<Point> killShotTrack = extractKillShotTrack(game);

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedGame(killShotTrack);

        // send the update to all Virtual Views

        for (VirtualView virtualView : observers.getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }
    }

    @Override
    public void updateSinge(int playerId, Object object) {

        // cast the Object in its dynamic type

        this.game = (CurrentGame) object;

        // translates the killShotTrack into a list of Point

        List<Point> killShotTrack = extractKillShotTrack(game);

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

    public void setObservers(Observers observers) {
        this.observers = observers;
    }
}
