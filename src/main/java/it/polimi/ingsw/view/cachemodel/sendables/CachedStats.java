package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;
import java.awt.*;
import java.util.List;

public class CachedStats extends UpdateClass {

    private final int score;
    private final int deaths;
    private final Boolean online;
    private final List<Integer> marks;
    private final List<Integer> dmgTaken;
    private final Point currentPosition;

    public CachedStats( int playerId, int score, int deaths, Boolean online, List<Integer> marks, List<Integer> dmgTaken, Point currentPosition) {
        super(UpdateType.STATS, playerId);
        this.score = score;
        this.deaths = deaths;
        this.online = online;
        this.marks = marks;
        this.dmgTaken = dmgTaken;
        this.currentPosition = currentPosition;
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
        return  currentPosition.x;
    }

    public int getCurrentPosY() {
        return currentPosition.y;
    }

    @Override
    public String toString() {
        return "position : " + currentPosition + ", online: " + online ;
    }
}
