package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.CurrentGame;
import it.polimi.ingsw.view.cachemodel.updates.Update;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CachedGame implements Serializable, Update {

    /**
     * This list contains Pair with x -> killerId, y -> amount
     */
    private final List<Point> killShotTrack = new ArrayList<>();


    public CachedGame(CurrentGame game) {

        for (int i = 0; i < game.getKillShotTrack().size(); i++) {

            this.killShotTrack.add(new Point(
                    game.getKillShotTrack().get(i).getKillerId(),
                    game.getKillShotTrack().get(i).getAmount()
                    ));
            
        }



    }

    /**
     * return a list containing Pairs with x -> killerId, y -> amount
     */
    public List<Point> getKillShotTrack() {
        return killShotTrack;
    }

}
