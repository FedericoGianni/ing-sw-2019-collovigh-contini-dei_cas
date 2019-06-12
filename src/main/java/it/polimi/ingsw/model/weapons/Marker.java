package it.polimi.ingsw.model.weapons;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.customsexceptions.DeadPlayerException;
import it.polimi.ingsw.customsexceptions.OverKilledPlayerException;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
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

    public Marker(int a,int b,boolean c,boolean dp) {
        this.markers=a;
        this.playerNum=b;
        this.seeAbleTargetNeeded=c;
        this.differentPlayer=dp;
    }

    public static List<Marker> getMarkersArray() {
        return markersArray;
    }

    public static void insertMarkerType(Marker mA) {
        markersArray.add(mA);
    }

    public boolean isDifferentPlayer() {
        return differentPlayer;
    }

    public void setDifferentPlayer(boolean differenPlayer) {
        this.differentPlayer = differenPlayer;
    }

    public void setMarkers(int m)
    {
        this.markers=m;
    }

    public int getMarkers(){
        return this.markers;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public boolean isSeeAbleTargetNeeded() {
        return seeAbleTargetNeeded;
    }

    public void setSeeAbleTargetNeeded(boolean seeAbleTargetNeeded) {
        this.seeAbleTargetNeeded = seeAbleTargetNeeded;
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

    @Override
    public Marker copy() {
        return this;
    }

    @Override
    public void print() {
        System.out.println("marker");
    }

    //i don't need to check distance because if the distance is ok from the damage here is too obv
    /**
     *
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
    public void microEffectApplicator(List<Player> playerList, Weapon w, Cell c) throws OverKilledPlayerException, DeadPlayerException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, DifferentPlayerNeededException, NotCorrectPlayerNumberException {
        if (differentPlayer==true)//only one case
        {
            if(w.getFirstTarget()!=playerList.get(0))//weapons that requires precedent players always have one target
            {
                if(playerList.get(0).canSee().contains(w.getFirstTarget())){
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
        }else if(playerNum >=1000)//fino a playerNum/1000 giocatori
        {

        }else{
            if(playerList.size()!=playerNum){
                throw new NotCorrectPlayerNumberException();
            }
            for(Player item : playerList)
            {
                if(!w.isPossessedBy().canSee().contains(item))
                {
                    throw new SeeAblePlayerException();
                }
            }
            for(Player item : playerList)
            {
                item.addMarks(w.isPossessedBy().getPlayerId(),markers);
            }
        }
    }

    @Override
    public boolean moveBefore() {
        return false;
    }

    /**
     *
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
