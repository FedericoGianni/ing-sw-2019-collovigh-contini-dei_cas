package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Stats;
import it.polimi.ingsw.view.updates.Update;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class CachedStats implements Serializable, Update {

    private final int score;
    private final int deaths;
    private final Boolean online;
    private final List<Integer> marks;
    private final List<Integer> dmgTaken;
    private final Point currentPosition;

    public CachedStats(Stats stats) {

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
}
