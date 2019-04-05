package it.polimi.ingsw;

import java.util.*;

/**
 * 
 */
public class Player {

    /**
     * Default constructor
     */
    public Player() {
    }

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private List<Weapon> currentWeapons;

    /**
     * 
     */
    private Cell currentPosition;

    /**
     * 
     */
    private List<PowerUp> currentPowerUp;

    /**
     * 
     */
    private List<Integer> dmgTaken;

    /**
     * 
     */
    private boolean adrAct1;

    /**
     * 
     */
    private boolean adrAct2;

    /**
     * 
     */
    private int deaths;

    /**
     * 
     */
    private int[] marks = new int[3];

    /**
     * 
     */
    private int score;

    /**
     * 
     */
    private List<AmmoCube> Ammo;


    /**
     * @return
     */
    public Cell getCurrentPosition() {
        // TODO implement here
        return null;
    }

    /**
     * @param c
     */
    public void setPlayerPos(Cell c) {
        // TODO implement here
    }

    /**
     * @return
     */
    public int getPlayerId() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public String getPlayerName() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public List<Weapon> getWeapon() {
        // TODO implement here
        return null;
    }

    /**
     * @param w
     */
    public void addWeapon(Weapon w) {
        // TODO implement here
    }

    /**
     * @param w
     */
    public void delWeapon(Weapon w) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<PowerUp> getPowerUps() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Boolean hasMaxPowerUp() {
        // TODO implement here
        return null;
    }

    /**
     * @param p
     */
    public void addPowerUp(PowerUp p) {
        // TODO implement here
    }

    /**
     * @param p
     */
    public void delPowerUp(PowerUp p) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<Player> canTarget() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Player> canBeTargetedBy() {
        // TODO implement here
        return null;
    }

    /**
     * @param c
     */
    public void addCube(AmmoCube c) {
        // TODO implement here
    }

    /**
     * @return
     */
    public int blueCubes() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int yellowCubes() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int redCubes() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int numDeaths() {
        // TODO implement here
        return 0;
    }

    /**
     * @param death
     */
    public void incrDeaths( Boolean death) {
        // TODO implement here
    }

    /**
     * @param fromPlayerId 
     * @param value
     */
    public void setDmg(int fromPlayerId, int value) {
        // TODO implement here
    }

    /**
     * @param fromPlayerId 
     * @param value
     */
    public void setMarks(int fromPlayerId, int value) {
        // TODO implement here
    }

    /**
     * @param value
     */
    public void addScore(int value) {
        // TODO implement here
    }

}