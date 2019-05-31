package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Stats;
import it.polimi.ingsw.view.updates.Update;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class CachedStats extends UpdateClass {

    private final int score;
    private final int deaths;
    private final Boolean online;
    private final List<Integer> marks;
    private final List<Integer> dmgTaken;
    private final Point currentPosition;

    public CachedStats(Stats stats, int playerId) {

        super(UpdateType.STATS,playerId);

        this.score = stats.getScore();
        this.deaths = stats.getDeaths();
        this.online = stats.getOnline();
        this.marks = stats.getMarks();
        this.dmgTaken = stats.getDmgTaken();
        this.currentPosition = Model.getMap().cellToCoord(stats.getCurrentPosition());
    }

    public int getScore() {
        return score;
    }

    public int getDeaths() {
        return deaths;
    }

    public Boolean getOnline(){ return online; }

    public List<Integer> getMarks() {
        return marks;
    }

    public List<Integer> getDmgTaken() {
        return dmgTaken;
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public int getCurrentPosX() {
        return (int) currentPosition.getX();
    }

    public int getCurrentPosY() {
        return (int) currentPosition.getY();
    }
}
