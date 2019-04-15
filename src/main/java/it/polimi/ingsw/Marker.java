package it.polimi.ingsw;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Marker extends MicroEffect {

    private int playerNum;//some effects can give markers to more than 1 player
    private boolean seeAbleTargetNeeded; //some effects can target unSeeable players or need to see players
    private int markers;//the number of markers given
    private static ArrayList markersArray=new ArrayList<>();//this array stataicaly contains all the marker types

    public Marker(int a,int b,boolean c) {
        this.playerNum=a;
        this.playerNum=b;
        this.seeAbleTargetNeeded=c;

    }

    public static ArrayList<Marker> getMarkersArray() {
        return markersArray;
    }

    public static void setMarkersArray(ArrayList mA) {
        markersArray = mA;
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
        ArrayList<Marker> mrks=new ArrayList<>();
        try (FileReader reader = new FileReader("C:\\Users\\bl4ck\\IdeaProjects\\ing-sw-2019-collovigh-contini-dei_cas\\src\\main\\java\\it\\polimi\\ingsw\\MarkerTypes"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray markerTypes = (JSONArray) obj;
            //System.out.println(damageTypes);


            for (int i = 0; i < markerTypes.size(); i++) {
                mrks =  parseMarkerObject((JSONObject)markerTypes.get(i), mrks);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        markersArray=mrks;
    }

    private static ArrayList <Marker> parseMarkerObject(JSONObject markers,ArrayList<Marker> mrks)
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

        Marker dd=new Marker(Integer.parseInt(t),Integer.parseInt(d),Boolean.parseBoolean(stn));
        mrks.add(dd);
        return mrks;

    }




    @Override
    public void applyOn(Player player) {

    }

    @Override
    public MicroEffect copy() {
        return null;
    }
}
