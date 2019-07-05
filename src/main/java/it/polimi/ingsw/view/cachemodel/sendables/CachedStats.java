package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.awt.*;
import java.util.List;
import java.util.Optional;

/**
 * Simplifed verision of the Model Player's stats, which contains informations about dmg, marks, positions, deaths
 */
public class CachedStats extends UpdateClass {

    /**
     * actual score of the player, updated when killing someone or at game end
     */
    private final int score;

    /**
     * number of deaths of the player who has this cached stats
     */
    private final int deaths;

    /**
     * True if the player is online, false otherwise
     */
    private final Boolean online;

    /**
     * Marks taken by the player. note that the marks taken is the list size, it is a list of integer in which every int represents
     * 1 single mark, in this way you can determine from which player you have taken marks
     */
    private final List<Integer> marks;

    /**
     * Damage taken by the player. note that the marks taken is the list size, it is a list of integer in which every int represents
     * 1 single damage, in this way you can determine from which player you have taken damage
     */
    private final List<Integer> dmgTaken;

    /**
     * Current position of the player, represented by a point in which x is the row and y is the col in the matrix map
     */
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
        return Optional.of(currentPosition.x).orElse(null);
    }

    public int getCurrentPosY() {
        return Optional.of(currentPosition.y).orElse(null);
    }

    @Override
    public String toString() {
        return "position : " + currentPosition + ", online: " + online ;
    }

    public String onlineToString() {
        if (getOnline()){
            return "si";
        } else {
            return "no";
        }
    }
}
