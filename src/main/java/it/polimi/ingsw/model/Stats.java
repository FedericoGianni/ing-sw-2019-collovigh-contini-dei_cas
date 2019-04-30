package it.polimi.ingsw.model;


import customsexceptions.DeadPlayerException;
import customsexceptions.OverMaxDmgException;
import customsexceptions.OverMaxMarkException;
import customsexceptions.OverkilledPlayerException;

import java.util.ArrayList;
import java.util.List;

public class Stats {

    private static final int MAX_DMG = 12;
    private static final int MAX_MARKS = 3;

    private int score;
    private int deaths;
    /**
     * this is an arrayList which contains max MAX_MARKS Integer each one representing the id of the offending player
     */
    private List<Integer> marks = new ArrayList<>();
    /**
     * this is an ArrayList which contains max MAX_DMG Integer (Overkill) each one representing the id of the offending player
     */
    private List<Integer> dmgTaken = new ArrayList<>();
    private Cell currentPosition;

    /**
     *
     * @param currentPosition is the position in which the player starts
     */
    public Stats(Cell currentPosition) {
        this.score = 0;
        this.deaths = 0;
        this.currentPosition = currentPosition;
    }

    /**
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    /**
     *
     * this function is meant to be used only in Game recovery (EG: load a saved game)
     * @param score set the score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     *
     * @param score to add
     */
    public void addScore(int score){

        this.score = this.score + score ;
    }

    /**
     *
     * @return the deaths count of the player
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     *
     * this function is meant to be used only in Game recovery (EG: load a saved game)
     * @param deaths the deaths count of the player
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    /**
     * this function increase the death count by one
     */
    public void addDeath(){
        this.deaths++;
    }

    /**
     *
     * @return a copy of the marks'list
     */
    public List<Integer> getMarks() {
        return new ArrayList<>(this.marks);
    }

    /**
     *
     * this function is meant to be used only in Game recovery (EG: load a saved game)
     * @param marks the array of marks
     */
    public void setMarks(List<Integer> marks) throws OverMaxMarkException{

        if (marks.size() <= MAX_MARKS) {
            this.marks = new ArrayList<>(marks);
        }
        else {
            throw new OverMaxMarkException();
        }

    }

    /**
     *
     * @param playerId is the id of the player who placed the marks if the marks are already three the function will do nothing
     */
    public void addMarks(int playerId){

        if (this.marks.size()<MAX_MARKS){
            this.marks.add(playerId);
        }

    }

    /**
     *
     * @return the damage points taken by the player
     */
    public List<Integer> getDmgTaken() {
        return new ArrayList<>(this.dmgTaken);
    }

    /**
     *
     * this function is meant to be used only in Game recovery (EG: load a saved game)
     * @param dmgTaken sets the damage points taken by the player
     */
    public void setDmgTaken(List<Integer> dmgTaken) throws OverMaxDmgException{

        if (dmgTaken.size() <= MAX_DMG) {
            this.dmgTaken = new ArrayList<>(dmgTaken);
        }
        else throw new OverMaxDmgException();

    }

    /**
     *
     * @param dmg is the number of damage signals to add
     * @param playerId is the id of the player who gave them
     * @throws DeadPlayerException if player died
     */
    public void addDmgTaken(int dmg, int playerId) throws DeadPlayerException, OverkilledPlayerException {

        for (int i = 0; i < MAX_MARKS; i++) {

            if (marks.indexOf(playerId) != -1) {

                dmg++;
                marks.remove(playerId);

            }
        }


        if (this.dmgTaken.size() < MAX_DMG ){

            for (int i = 0; i < dmg; i++) {

                if (dmgTaken.size()< MAX_DMG){

                    this.dmgTaken.add(playerId);
                }
            }
        }



        if (dmgTaken.size()>= MAX_DMG - 1){  // if player has more than MAX_DMG -1 (simply dead)
            this.addDeath();

            if (dmgTaken.size() == MAX_DMG){  // if player gets Overkilled

                throw new OverkilledPlayerException();
            }
            throw new DeadPlayerException();
        }
    }

    /**
     *
     * @return current position of the player
     */
    public Cell getCurrentPosition() {
        return currentPosition;
    }

    /**
     *
     * @param currentPosition of the player
     */
    public void setCurrentPosition(Cell currentPosition) {
        this.currentPosition = currentPosition;
    }
}
