package it.polimi.ingsw.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Marker extends MicroEffect {

    private int playerNum;//some effects can give markers to more than 1 player
    private boolean seeAbleTargetNeeded; //some effects can target unSeeable players or need to see players
    private int markers;//the number of markers given
    private boolean differenPlayer;
    private static ArrayList markersArray=new ArrayList<>();//this array stataicaly contains all the marker types
    // the player number is 100 you need to trget every player in the traget's square( check hellion
    // if player number is 1000 io need can target up to the number /1000


    public Marker(int a,int b,boolean c,boolean dp) {
        this.markers=a;
        this.playerNum=b;
        this.seeAbleTargetNeeded=c;
        this.differenPlayer=dp;
        this.isMarker();
    }

    public static ArrayList<Marker> getMarkersArray() {
        return markersArray;
    }

    public static void insertMarkerType(Marker mA) {
        markersArray.add(mA);
    }

    public boolean isDifferenPlayer() {
        return differenPlayer;
    }

    public void setDifferenPlayer(boolean differenPlayer) {
        this.differenPlayer = differenPlayer;
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
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String path = new File("src/main/java/it/polimi/ingsw/model/MarkerTypes").getAbsolutePath();
        try (FileReader reader = new FileReader(path))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray markerTypes = (JSONArray) obj;
            //System.out.println(damageTypes);


            for (int i = 0; i < markerTypes.size(); i++) {
                  parseMarkerObject((JSONObject)markerTypes.get(i));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * create a Marker and adds it to the static list
     * @param markers
     */
    private static void parseMarkerObject(JSONObject markers)
    {
        //Get employee object within list
        JSONObject mObject = (JSONObject) markers.get("Marker");//Choose the class  to read--not really, something similar

        //get the damage amount
        String t = (String) mObject.get("markers");
        //System.out.println(t);

        //Get playerNum
        String d = (String) mObject.get("playerNum");
        //System.out.println(d);

        //Get seeAble
        String stn = (String) mObject.get("seeAbleTargetNeeded");
        //System.out.println(stn);

        //Get seeAble
        String dp = (String) mObject.get("differentPlayer");
        //System.out.println(stn);

        Marker dd=new Marker(Integer.parseInt(t),Integer.parseInt(d),Boolean.parseBoolean(stn),Boolean.parseBoolean(dp));
        Marker.insertMarkerType(dd);

    }




    @Override
    public void applyOn(Player player) {

    }

    @Override
    public MicroEffect copy() {
        return null;
    }
}
