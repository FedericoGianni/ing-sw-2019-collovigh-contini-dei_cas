package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Marker extends MicroEffect {

    private int playerNum;//some effects can give markers to more than 1 player
    private boolean seeAbleTargetNeeded; //some effects can target unSeeable players or need to see players
    private int markers;//the number of markers given
    private boolean differentPlayer;
    private static List<Marker> markersArray=new ArrayList<>();//this array stataicaly contains all the marker types
    // the player number is 100 you need to target every player in the traget's square( check hellion
    // if player number is 1000 io need can target up to the number /1000

    /**
     * constructor
     * @param a
     * @param b
     * @param c
     * @param dp
     */
    public Marker(int a,int b,boolean c,boolean dp) {
        this.markers=a;
        this.playerNum=b;
        this.seeAbleTargetNeeded=c;
        this.differentPlayer=dp;
    }

    /**
     * get marks
     * @return
     */
    public static List<Marker> getMarkersArray() {
        return markersArray;
    }

    /**
     * directly insert marker
     * @param mA
     */
    public static void insertMarkerType(Marker mA) {
        markersArray.add(mA);
    }



    /**
     * this method uses the semplified JSON to populate the microeffets-MArker Class that are in a known number
     */
    public static void populator()
    {
        List<Marker> markerList = Parser.markerReader();

        for (Marker marker: markerList){

            Marker.insertMarkerType(marker);

        }

    }

    @Override
    public void applyOn(Player player) {

    }

    /**
     * return thi object
     * @return
     */
    @Override
    public Marker copy() {
        return this;
    }

    /**
     * print infos
     */
    @Override
        public void print() {
        /*
            System.out.println("markers: "+markers);
            System.out.println("mseeable Target: "+seeAbleTargetNeeded);
            System.out.println(": "+playerNum);
            System.out.println("different player: "+differentPlayer);

         */
        }


    //i don't need to check distance because if the distance is ok from the damage here is too obv
    /**
     *give the markers to the tragets
     * @param playerList
     * @param w
     * @throws OverKilledPlayerException
     * @throws DeadPlayerException
     * @throws PlayerInSameCellException
     * @throws PlayerInDifferentCellException
     * @throws UncorrectDistanceException
     * @throws SeeAblePlayerException
     * @throws DifferentPlayerNeededException
     * @throws NotCorrectPlayerNumberException
     */
    @Override
    public void microEffectApplicator(List<Player> playerList, Weapon w, Cell c,int n) throws  PlayerInDifferentCellException, SeeAblePlayerException, DifferentPlayerNeededException, NotCorrectPlayerNumberException, PlayerNotSeeableException {
        print();
        if (differentPlayer==true)//only one case
        {

            if(w.getFirstTargets().get(0)!=playerList.get(0))//lock rfle, just 1 target
            {
                if(playerList.get(0).canSee().contains(w.getFirstTargets().get(0))){
                playerList.get(0).addMarks(w.isPossessedBy().getPlayerId(),markers);}else{
                    throw new SeeAblePlayerException();
                }
            }
            else{
                throw new DifferentPlayerNeededException();
            }
        }else if(playerNum == 100)//every other player in the targeted cell
        {
            for(Player item : playerList)//check that everyone is in the same cells
            {
                sameCellCheck(item,playerList);
            }
            if(seeAbleTargetNeeded==true)
            {
                if(!w.isPossessedBy().canSee().contains(playerList.get(0)))//one random palyer beacause everyone is in the same cell
                {
                    throw new SeeAblePlayerException();
                }
            }
            for(Player item:playerList)
            {
                item.addMarks(w.isPossessedBy().getPlayerId(),markers);
            }
        }else if(playerNum >=1000)//up to playerNum/1000 giocatori
        {

            if(playerList.size()>playerNum/1000){
                throw new NotCorrectPlayerNumberException();
            }
            for(Player p:playerList)
            {
                p.addMarks(w.isPossessedBy().getPlayerId(),markers);
            }

        }else{//player num is what is written
            if(playerList.size()!=playerNum){
                throw new NotCorrectPlayerNumberException();
            }
            for(Player item : playerList)
            {
                //System.out.println(w.isPossessedBy().canSee().size());
                if(!w.isPossessedBy().canSee().contains(item))
                {

                    throw new PlayerNotSeeableException();

                }
            }
            for(Player item : playerList)
            {
                item.addMarks(w.isPossessedBy().getPlayerId(),markers);
            }
        }
    }

    /**
     * check if you need to move ebfore
     * @return
     */
    @Override
    public boolean moveBefore() {
        return false;
    }

    /**
     *check if the players are in the same cell
     * @param p
     * @param playerList
     * @throws PlayerInDifferentCellException
     * @throws NotCorrectPlayerNumberException
     */
    private void sameCellCheck(Player p,List<Player> playerList) throws PlayerInDifferentCellException, NotCorrectPlayerNumberException {
        if(!playerList.containsAll(p.getCurrentPosition().getPlayers())){//non all the player are in the number
            throw new NotCorrectPlayerNumberException();
        }
        for(Player item:playerList)
        {
            if(p.getCurrentPosition()!=item.getCurrentPosition())
                throw new PlayerInDifferentCellException();
        }
    }

}
