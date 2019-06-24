package it.polimi.ingsw.model.player;


import it.polimi.ingsw.customsexceptions.DeadPlayerException;
import it.polimi.ingsw.customsexceptions.OverMaxDmgException;
import it.polimi.ingsw.customsexceptions.OverMaxMarkException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Subject;
import it.polimi.ingsw.model.map.Cell;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Stats extends Subject implements Serializable {

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
    private Point currentPosition;
    private Boolean online;
    private boolean frenzyBoard = false;

    /**
     *
     * default constructor
     */
    public Stats() {
        this.score = 0;
        this.deaths = 0;
        this.currentPosition =  null;
        this.online = true;
    }

    /**
     * Clone constructor
     * @param clone is the class from whom copy all the data
     */
    public Stats(Stats clone) {
        this.score = clone.score;
        this.deaths = clone.deaths;
        this.marks = new ArrayList<>(clone.marks);
        this.dmgTaken = new ArrayList<>(clone.dmgTaken);
        this.currentPosition = clone.currentPosition;
        this.online = clone.online;
        this.frenzyBoard = clone.frenzyBoard;
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
        updateAll(this);
    }

    /**
     *
     * @param score to add
     */
    public void addScore(int score){

        this.score = this.score + score ;
        updateAll(this);
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
        updateAll(this);
    }

    /**
     * this function increase the death count by one,
     * NOTE: it will not add the death to the KillShotTrack bc there is no way to know who shoot
     */
    public void addDeath(){

        this.deaths++;
        updateAll(this);
    }

    public int getPlayerId(){

        return Model.getGame()
                .getPlayers()
                .stream()
                .filter(p -> p.getStats().equals(this))
                .collect(Collectors.toList())
                .get(0)
                .getPlayerId();
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
            updateAll(this);
        }
        else {
            throw new OverMaxMarkException();
        }

    }

    /**
     * USed for markers in shooting tests
     * @param marks
     */
    public void setMarksCopy(List<Integer> marks){
            this.marks = new ArrayList<>(marks);
    }

    /**
     *
     * @param playerId is the id of the player who placed the marks if the marks are already three the function will do nothing
     */
    public void addMarks(int playerId){

        if (this.marks.size()<MAX_MARKS){
            this.marks.add(playerId);
            updateAll(this);
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
            updateAll(this);
        }
        else throw new OverMaxDmgException();

    }

    /**
     * Used for the copyes for shoot controls
     * @param dmgTaken
     */
    public void setDmgTakenCopy(List<Integer> dmgTaken){

            this.dmgTaken = new ArrayList<>(dmgTaken);

    }

    /**
     *
     * @param dmg is the number of damage signals to add
     * @param playerId is the id of the player who gave them
     * @throws DeadPlayerException if player died
     */
    public void addDmgTaken(int dmg, int playerId){


        if (marks.indexOf(playerId) != -1) {

            dmg = dmg + this.marks
                    .stream()
                    .filter(x -> x == playerId)
                    .collect(Collectors.toList())
                    .size();


            marks.removeAll(Collections.singleton(playerId));
            updateAll(this);
        }

        if (this.dmgTaken.size() < MAX_DMG ){

            for (int i = 0; i < dmg; i++) {

                if (dmgTaken.size()< MAX_DMG){

                    this.dmgTaken.add(playerId);
                }
            }

            updateAll(this);
        }

        if ((dmgTaken.size() >= MAX_DMG - 1)){  // if player has more than MAX_DMG -1 (simply dead)

            this.addDeath();

        }
    }

    public void resetDmg(){

        this.dmgTaken = new ArrayList<>();
        updateAll(this);
    }

    /**
     *
     * @return current position of the player
     */
    public Cell getCurrentPosition() {
        return (currentPosition == null) ? null : Model.getMap().getCell(currentPosition.x,currentPosition.y);
    }

    public Cell getCurrentPositionCopy()
    {
        return Model.getMap().getCell(currentPosition.x,currentPosition.y);
    }

    /**
     *
     * @param currentPosition of the player
     */
    public void setCurrentPosition(Cell currentPosition) {

        // remove the player from the previous cell

        if(this.currentPosition != null) this.getCurrentPosition().removePlayerFromHere(Model.getPlayer(getPlayerId()));

        // sets the new position in the stats

        this.currentPosition = (currentPosition == null) ? null : Model.getMap().cellToCoord(currentPosition);

        // sets the player in the cell if not null

        if (this.currentPosition != null) currentPosition.addPlayerHere(Model.getPlayer(getPlayerId()));

        // updates the observers

        updateAll(this);
    }

    /**
     * Used for the copyes for preshoot tests
     * @param currentPosition
     */
    public void setCurrentPositionCopy(Cell currentPosition){
        this.currentPosition =  Model.getMap().cellToCoord(currentPosition);

    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
        updateAll(this);

        getAllMyData();
    }

    private void getAllMyData(){

        int playerId = getPlayerId();

        for (Player player : Model.getGame().getPlayers()){

            // sends player stats

            player.getStats().updateSingle(player.getStats(), playerId);

            // sends WeaponBag

            player.getCurrentWeapons().updateSingle(player.getCurrentWeapons(),playerId);

            // sends AmmoBag

            player.getAmmoBag().updateSingle(player.getAmmoBag(),playerId);
        }

        // PowerUps can be seen only by possessor

        Model.getPlayer(playerId).getPowerUpBag().updateAll(Model.getPlayer(playerId).getPowerUpBag());

        // sends Cells

        for(Cell[] c : Model.getMap().getMatrix()){
            for(Cell c2 : c){

                if(c2 != null ) c2.updateSingle(c2,playerId);

            }
        }

        // sends Game

        Model.getGame().updateSingle(Model.getGame(),playerId);
    }

    public boolean isFrenzyBoard() {
        return frenzyBoard;
    }

    public void setFrenzyBoard(boolean frenzyBoard) {
        this.frenzyBoard = frenzyBoard;
    }
}
