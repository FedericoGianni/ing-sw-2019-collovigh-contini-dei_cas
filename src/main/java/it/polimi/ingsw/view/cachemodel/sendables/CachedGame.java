package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.CurrentGame;
import it.polimi.ingsw.view.updates.Update;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CachedGame extends UpdateClass {

    /**
     * This list contains Pair with x -> killerId, y -> amount
     */
    private final List<Point> killShotTrack = new ArrayList<>();


    public CachedGame(List<Point> killShotTrack) {

        super(UpdateType.GAME, -1);





    }

    /**
     * return a list containing Pairs with x -> killerId, y -> amount
     */
    public List<Point> getKillShotTrack() {
        return killShotTrack;
    }

}
