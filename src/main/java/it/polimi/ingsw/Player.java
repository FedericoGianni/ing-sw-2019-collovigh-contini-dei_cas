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
    private List<PowerUp> currentPowerUps;

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

        return this.currentPosition;
    }

    /**
     * @param c
     */
    public void setPlayerPos(Cell c) {
        this.currentPosition=c;
    }

    /**
     * @return
     */
    public int getPlayerId(Player p) {
        return p.id;
    }

    /**
     * @return
     */
    public String getPlayerName(Player p) {
        return p.name;
    }

    /**
     * @return
     */
    public List<Weapon> getWeapon(Player p) {
        return p.currentWeapons;
    }

    /**
     * @param w
     */
    public void addWeapon(Weapon w) {
        // the controller package chacks that a player mustn't have more than 3 weapons
        this.currentWeapons.add(w);
    }

    /**
     * @param w
     */
    public void delWeapon(Weapon w) {
        this.currentWeapons.remove(w);
    }

    /**
     * @return
     */
    public List<PowerUp> getPowerUps(Player p) {

        return p.currentPowerUps;
    }

    /**
     * @return
     */
    public Boolean hasMaxPowerUp(Player p) {
        if(p.currentPowerUps.size()==3)
            return true;
        return false;
    }

    public Boolean hasMaxWeapons(Player p) {
        if(p.currentWeapons.size()==3)
            return true;
        return false;
    }

    /**
     * @param p
     */
    public void addPowerUp(PowerUp p) {
        this.currentPowerUps.add(p);
    }

    /**
     * @param p
     */
    public void delPowerUp(PowerUp p) {
        this.currentPowerUps.remove(p);
    }

    /**
     * @return
     */
    public List<Player> canTarget() {
        return null;
    }//requires weapon implemntation
    //occhio a cambi stanza e a cambio numero di giocatori
    public List<Player> canSee() {//necessaries other players, otherwise you don't know where tehey are
                Cell c=this.getCurrentPosition();
                List<Player> visibili;
                visibili=c.getPlayers();
                visibili.remove(this);//with these instructions i'm sure to take the players that are in the current cell

                if(c.getNorth() !=null)
                {
                   visibili.addAll(c.getPlayers());
                   visibili=runner(visibili,c.getNorth());
                }
            if(c.getEast() !=null)
            {
                visibili.addAll(c.getPlayers());
                visibili=runner(visibili,c.getEast());
            }
            if(c.getWest() !=null)
            {
                visibili.addAll(c.getPlayers());
                visibili=runner(visibili,c.getWest());
            }
            if(c.getSouth() !=null)
            {
                visibili.addAll(c.getPlayers());
                visibili=runner(visibili,c.getSouth());
            }


                return visibili;//handle a nullPointerExcpetion if you can't see any other player
    }
    /**
     * @return
     */

    public List<Player> runner(List<Player> visibili,Cell c)
    {
        if(c.getNorth() !=null && c.getNorth().getColor()==c.getColor())//if the color is different you change the room, so you can't see other players
        {
            visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c.getNorth());
        }
        if(c.getEast() !=null && c.getEast().getColor()==c.getColor())
        {
            visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c.getEast());
        }
        if(c.getWest() !=null && c.getWest().getColor()==c.getColor())
        {
            visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c.getWest());
        }
        if(c.getSouth() !=null && c.getSouth().getColor()==c.getColor())
        {
            visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c.getSouth());
        }
        return visibili;
    }
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
     * @param
     */
    public void setDeath()
    {
        this.deaths=deaths++;
    }
    public void incrDeaths( Boolean death) {//---- senses?
        if(death==true)
        {
            setDeath();
        }
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