
package it.polimi.ingsw.model.map;

import com.google.gson.Gson;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;

import java.awt.*;
import java.io.*;

import static it.polimi.ingsw.model.map.CellColor.*;

public class JsonMap {

    public static final int MAP_R = 3;
    public static final int MAP_C = 4;

    private JsonCell[][] matrix = new JsonCell[MAP_R][MAP_C];
    private int mapType;

    public JsonCell[][] getMatrix() {
        return matrix;
    }

    public JsonCell getCell(int r, int c){
        return matrix[r][c];
    }

    public int getMapType() {
        return mapType;
    }

    /**
     * Default contructor
     */
    public JsonMap(){

    }

    /**
     * Constructor used to generate a JsonMap from a Map
     * @param map
     */
    public JsonMap(Map map){

        this.mapType = map.getMapType();
        //this.matrix = new JsonCell[MAP_R][MAP_C];

        for(int i = 0; i < MAP_R; i++){
            for (int j = 0; j < MAP_C; j++) {
                if(map.getCell(i,j) != null){

                    this.matrix[i][j] = new JsonCell();

                    this.getCell(i,j).setColor(map.getCell(i,j).getColor());
                    this.getCell(i,j).setAmmoCell(map.getCell(i,j).isAmmoCell());

                    if(map.getCell(i,j).isAmmoCell()) {
                        this.getCell(i,j).setCellType(CellType.AMMO);
                    } else{
                        this.getCell(i,j).setCellType(CellType.SPAWN);
                    }

                    this.matrix[i][j].setVisit(false);

                    if(map.getCell(i,j).getNorth() != null){
                        this.getCell(i,j).setAdjNorth(new Point(i-1, j));
                    } else {
                        this.getCell(i,j).setAdjNorth(null);
                    }

                    if(map.getCell(i,j).getSouth() != null){
                        this.getCell(i,j).setAdjSouth(new Point(i+1, j));
                    } else {
                        this.getCell(i,j).setAdjSouth(null);
                    }

                    if(map.getCell(i,j).getEast() != null){
                        this.getCell(i,j).setAdjEast(new Point(i, j+1));
                    } else {
                        this.getCell(i,j).setAdjEast(null);
                    }

                    if(map.getCell(i,j).getWest() != null){
                        this.getCell(i,j).setAdjWest(new Point(i, j-1));
                    } else {
                        this.getCell(i,j).setAdjWest(null);
                    }


                } else {
                    //this.matrix[i][j] = new JsonCell();
                    this.matrix[i][j] = null;
                }
            }
        }
    }

    /**
     * Generate a JsonMap from a json file
     * @param mapType integer representing the type of map
     * @return a JsonMap with attributes read from json
     */
    public static JsonMap genJsonMap(int mapType){

        String path = null;
        Gson gson = new Gson();

        switch (mapType){
            case 1:
                path = new File("resources/json/map/map1.json").getAbsolutePath();
                break;

            case 2:
                path = new File("resources/json/map/map2.json").getAbsolutePath();
                break;

            case 3:
                path = new File("resources/json/map/map3.json").getAbsolutePath();
                break;

            default:
                System.out.println("Invalid mapType!");
        }

        try{
            return gson.fromJson(new FileReader(path), JsonMap.class);
        } catch (FileNotFoundException e){
            e.getMessage();
        }

        return null;
    }


    private void genJson(){

        Gson gson = new Gson();
        try{
            gson.toJson(this, new FileWriter("resources/json/map2.json"));
        } catch (IOException e){
            e.getMessage();
        }
    }

    /**
     *
     * @param mapType an integer representing the type of Map, passed as a parameter to populate the matrix of Cells in different modes
     */
    private void generateCells(int mapType) {

        switch (mapType){

            case 1:

                (this.matrix[0][0]) = new JsonCell();
                (this.matrix[0][1]) = new JsonCell();
                (this.matrix[0][2]) = new JsonCell();
                (this.matrix[0][3]) = null;

                (this.matrix[1][0]) = new JsonCell();
                (this.matrix[1][1]) = new JsonCell();
                (this.matrix[1][2]) = new JsonCell();
                (this.matrix[1][3]) = new JsonCell();

                (this.matrix[2][0]) = null;
                (this.matrix[2][1]) = new JsonCell();
                (this.matrix[2][2]) = new JsonCell();
                (this.matrix[2][3]) = new JsonCell();

                (this.matrix[0][0]).setAdjNorth(null);
                (this.matrix[0][0]).setAdjSouth(new Point(1,0));
                (this.matrix[0][0]).setAdjEast(new Point(0,1));
                (this.matrix[0][0]).setAdjWest(null);
                (this.matrix[0][0]).setColor(BLUE);

                (this.matrix[0][1]).setAdjNorth(null);
                (this.matrix[0][1]).setAdjSouth(null);
                (this.matrix[0][1]).setAdjEast(new Point(0,2));
                (this.matrix[0][1]).setAdjWest(new Point(0,0));
                (this.matrix[0][1]).setColor(BLUE);

                (this.matrix[0][2]).setAdjNorth(null);
                (this.matrix[0][2]).setAdjSouth(new Point(1,2));
                (this.matrix[0][2]).setAdjEast(null);
                (this.matrix[0][2]).setAdjWest(new Point(0,1));
                (this.matrix[0][2]).setColor(BLUE);

                (this.matrix[1][0]).setAdjNorth(new Point(0,0));
                (this.matrix[1][0]).setAdjSouth(null);
                (this.matrix[1][0]).setAdjEast(new Point(1,1));
                (this.matrix[1][0]).setAdjWest(null);
                (this.matrix[1][0]).setColor(RED);

                (this.matrix[1][1]).setAdjNorth(null);
                (this.matrix[1][1]).setAdjSouth(new Point(2,1));
                (this.matrix[1][1]).setAdjEast(new Point(1,2));
                (this.matrix[1][1]).setAdjWest(new Point(1,0));
                (this.matrix[1][1]).setColor(RED);

                (this.matrix[1][2]).setAdjNorth(new Point(0,2));
                (this.matrix[1][2]).setAdjSouth(null);
                (this.matrix[1][2]).setAdjEast(new Point(1,3));
                (this.matrix[1][2]).setAdjWest(new Point(1,1));
                //this cell is actually purple but in the same room as the Red ones, so we treat it just like a red one
                (this.matrix[1][2]).setColor(RED);

                (this.matrix[1][3]).setAdjNorth(null);
                (this.matrix[1][3]).setAdjSouth(new Point(2,3));
                (this.matrix[1][3]).setAdjEast(null);
                (this.matrix[1][3]).setAdjWest(new Point(1,2));
                (this.matrix[1][3]).setColor(YELLOW);

                (this.matrix[2][0]) = null;

                (this.matrix[2][1]).setAdjNorth(new Point(1,1));
                (this.matrix[2][1]).setAdjSouth(null);
                (this.matrix[2][1]).setAdjEast(new Point(2,2));
                (this.matrix[2][1]).setAdjWest(null);
                (this.matrix[2][1]).setColor(GREY);

                (this.matrix[2][2]).setAdjNorth(null);
                (this.matrix[2][2]).setAdjSouth(null);
                (this.matrix[2][2]).setAdjEast(new Point(2,3));
                (this.matrix[2][2]).setAdjWest(new Point(2,1));
                (this.matrix[2][2]).setColor(GREY);

                (this.matrix[2][3]).setAdjNorth(new Point(1,3));
                (this.matrix[2][3]).setAdjSouth(null);
                (this.matrix[2][3]).setAdjEast(null);
                (this.matrix[2][3]).setAdjWest(new Point(2,2));
                (this.matrix[2][3]).setColor(YELLOW);

                break;


            case 2:

                (this.matrix[0][0]) = new JsonCell();
                (this.matrix[0][1]) = new JsonCell();
                (this.matrix[0][2]) = new JsonCell();
                (this.matrix[0][3]) = new JsonCell();

                (this.matrix[1][0]) = new JsonCell();
                (this.matrix[1][1]) = new JsonCell();
                (this.matrix[1][2]) = new JsonCell();
                (this.matrix[1][3]) = new JsonCell();

                (this.matrix[2][0]) = null;
                (this.matrix[2][1]) = new JsonCell();
                (this.matrix[2][2]) = new JsonCell();
                (this.matrix[2][3]) = new JsonCell();

                (this.matrix[0][0]).setAdjNorth(null);
                (this.matrix[0][0]).setAdjSouth(new Point(1,0));
                (this.matrix[0][0]).setAdjEast(new Point(0,1));
                (this.matrix[0][0]).setAdjWest(null);
                (this.matrix[0][0]).setColor(BLUE);

                (this.matrix[0][1]).setAdjNorth(null);
                (this.matrix[0][1]).setAdjSouth(null);
                (this.matrix[0][1]).setAdjEast(new Point(0,2));
                (this.matrix[0][1]).setAdjWest(new Point(0,0));
                (this.matrix[0][1]).setColor(BLUE);

                (this.matrix[0][2]).setAdjNorth(null);
                (this.matrix[0][2]).setAdjSouth(new Point(1,2));
                (this.matrix[0][2]).setAdjEast(new Point(0,3));
                (this.matrix[0][2]).setAdjWest(new Point(0,1));
                (this.matrix[0][2]).setColor(BLUE);

                (this.matrix[0][3]).setAdjNorth(null);
                (this.matrix[0][3]).setAdjSouth(new Point(1,3));
                (this.matrix[0][3]).setAdjEast(null);
                (this.matrix[0][3]).setAdjWest(new Point(0,2));
                (this.matrix[0][3]).setColor(GREEN);

                (this.matrix[1][0]).setAdjNorth(new Point(0,0));
                (this.matrix[1][0]).setAdjSouth(null);
                (this.matrix[1][0]).setAdjEast(new Point(1,1));
                (this.matrix[1][0]).setAdjWest(null);
                (this.matrix[1][0]).setColor(RED);

                (this.matrix[1][1]).setAdjNorth(null);
                (this.matrix[1][1]).setAdjSouth(new Point(2,1));
                (this.matrix[1][1]).setAdjEast(null);
                (this.matrix[1][1]).setAdjWest(new Point(1,0));
                (this.matrix[1][1]).setColor(RED);

                (this.matrix[1][2]).setAdjNorth(new Point(0,2));
                (this.matrix[1][2]).setAdjSouth(new Point(2,2));
                (this.matrix[1][2]).setAdjEast(new Point(1,3));
                (this.matrix[1][2]).setAdjWest(null);
                (this.matrix[1][2]).setColor(YELLOW);

                (this.matrix[1][3]).setAdjNorth(new Point(0,3));
                (this.matrix[1][3]).setAdjSouth(new Point(2,3));
                (this.matrix[1][3]).setAdjEast(null);
                (this.matrix[1][3]).setAdjWest(new Point(1,2));
                (this.matrix[1][3]).setColor(YELLOW);

                (this.matrix[2][1]).setAdjNorth(new Point(1,1));
                (this.matrix[2][1]).setAdjSouth(null);
                (this.matrix[2][1]).setAdjEast(new Point(2,2));
                (this.matrix[2][1]).setAdjWest(null);
                (this.matrix[2][1]).setColor(GREY);

                (this.matrix[2][2]).setAdjNorth(new Point(1,2));
                (this.matrix[2][2]).setAdjSouth(null);
                (this.matrix[2][2]).setAdjEast(new Point(2,3));
                (this.matrix[2][2]).setAdjWest(new Point(2,1));
                (this.matrix[2][2]).setColor(YELLOW);

                (this.matrix[2][3]).setAdjNorth(new Point(1,3));
                (this.matrix[2][3]).setAdjSouth(null);
                (this.matrix[2][3]).setAdjEast(null);
                (this.matrix[2][3]).setAdjWest(new Point(2,2));
                (this.matrix[2][3]).setColor(YELLOW);

                break;

            case 3:

                (this.matrix[0][0]) = new JsonCell();
                (this.matrix[0][1]) = new JsonCell();
                (this.matrix[0][2]) = new JsonCell();
                (this.matrix[0][3]) = new JsonCell();

                (this.matrix[1][0]) = new JsonCell();
                (this.matrix[1][1]) = new JsonCell();
                (this.matrix[1][2]) = new JsonCell();
                (this.matrix[1][3]) = new JsonCell();

                (this.matrix[2][0]) = new JsonCell();
                (this.matrix[2][1]) = new JsonCell();
                (this.matrix[2][2]) = new JsonCell();
                (this.matrix[2][3]) = new JsonCell();

                (this.matrix[0][0]).setAdjNorth(null);
                (this.matrix[0][0]).setAdjSouth(new Point(1,0));
                (this.matrix[0][0]).setAdjEast(new Point(0,1));
                (this.matrix[0][0]).setAdjWest(null);
                (this.matrix[0][0]).setColor(RED);

                (this.matrix[0][1]).setAdjNorth(null);
                (this.matrix[0][1]).setAdjSouth(new Point(1,1));
                (this.matrix[0][1]).setAdjEast(new Point(0,2));
                (this.matrix[0][1]).setAdjWest(new Point(0,0));
                (this.matrix[0][1]).setColor(BLUE);

                (this.matrix[0][2]).setAdjNorth(null);
                (this.matrix[0][2]).setAdjSouth(new Point(1,2));
                (this.matrix[0][2]).setAdjEast(new Point(0,3));
                (this.matrix[0][2]).setAdjWest(new Point(0,1));
                (this.matrix[0][2]).setColor(BLUE);

                (this.matrix[0][3]).setAdjNorth(null);
                (this.matrix[0][3]).setAdjSouth(new Point(1,3));
                (this.matrix[0][3]).setAdjEast(null);
                (this.matrix[0][3]).setAdjWest(new Point(0,2));
                (this.matrix[0][3]).setColor(GREEN);

                (this.matrix[1][0]).setAdjNorth(new Point(0,0));
                (this.matrix[1][0]).setAdjSouth(new Point(2,0));
                (this.matrix[1][0]).setAdjEast(null);
                (this.matrix[1][0]).setAdjWest(null);
                (this.matrix[1][0]).setColor(RED);

                (this.matrix[1][1]).setAdjNorth(new Point(0,1));
                (this.matrix[1][1]).setAdjSouth(new Point(2,1));
                (this.matrix[1][1]).setAdjEast(null);
                (this.matrix[1][1]).setAdjWest(null);
                (this.matrix[1][1]).setColor(PURPLE);

                (this.matrix[1][2]).setAdjNorth(new Point(0,2));
                (this.matrix[1][2]).setAdjSouth(new Point(2,2));
                (this.matrix[1][2]).setAdjEast(new Point(1,3));
                (this.matrix[1][2]).setAdjWest(null);
                (this.matrix[1][2]).setColor(YELLOW);

                (this.matrix[1][3]).setAdjNorth(new Point(0,3));
                (this.matrix[1][3]).setAdjSouth(new Point(2,3));
                (this.matrix[1][3]).setAdjEast(null);
                (this.matrix[1][3]).setAdjWest(new Point(1,2));
                (this.matrix[1][3]).setColor(YELLOW);

                (this.matrix[2][0]).setAdjNorth(new Point(1,0));
                (this.matrix[2][0]).setAdjSouth(null);
                (this.matrix[2][0]).setAdjEast(new Point(2,1));
                (this.matrix[2][0]).setAdjWest(null);
                (this.matrix[2][0]).setColor(GREY);

                (this.matrix[2][1]).setAdjNorth(new Point(1,1));
                (this.matrix[2][1]).setAdjSouth(null);
                (this.matrix[2][1]).setAdjEast(new Point(2,2));
                (this.matrix[2][1]).setAdjWest(new Point(2,0));
                (this.matrix[2][1]).setColor(GREY);

                (this.matrix[2][2]).setAdjNorth(new Point(1,2));
                (this.matrix[2][2]).setAdjSouth(null);
                (this.matrix[2][2]).setAdjEast(new Point(2,3));
                (this.matrix[2][2]).setAdjWest(new Point(2,1));
                (this.matrix[2][2]).setColor(YELLOW);

                (this.matrix[2][3]).setAdjNorth(new Point(1,3));
                (this.matrix[2][3]).setAdjSouth(null);
                (this.matrix[2][3]).setAdjEast(null);
                (this.matrix[2][3]).setAdjWest(new Point(2,2));
                (this.matrix[2][3]).setColor(YELLOW);

                break;


            default:

                break;
        }
    }


/*
    public static void main(String[] args) {

        //TO generate json from jsonMap class
        /*
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        JsonMap jsonMap = new JsonMap();
        jsonMap.generateCells(3);
        jsonMap.populateJson();
        gson.toJson(jsonMap);
        System.out.println(gson.toJson(jsonMap));


        Gson gson = new Gson();
        JsonMap jsonMap = JsonMap.genJsonMap(1);

        for (int i = 0; i < MAP_R; i++) {
            for (int j = 0; j < MAP_C; j++) {
                if(jsonMap.getCell(i,j) != null) {
                    System.out.println("cella " + i + " , " + j + " : " + jsonMap.getCell(i, j));
                    System.out.println("\ttipo:" + jsonMap.getCell(i,j).getCellType());
                    System.out.println("\tcolore: "+ jsonMap.getCell(i,j).getColor());
                    System.out.println("\tadiacenze: NORD: " + jsonMap.getCell(i, j).getAdjNorth());
                    System.out.println("\tadiacenze: SUD: " + jsonMap.getCell(i, j).getAdjSouth());
                    System.out.println("\tadiacenze: EST: " + jsonMap.getCell(i, j).getAdjEast());
                    System.out.println("\tadiacenze: OVEST: " + jsonMap.getCell(i, j).getAdjWest());
                    System.out.println("\tplayersHere: " + jsonMap.getCell(i,j).getPlayersHere());
                }

            }
        }

        Map m = new Map(jsonMap);

        System.out.println("debug");
        System.out.println("MAPPA: ");

        for(Cell c1[] : m.getMatrix()){
            for (Cell c2 : c1){
                if(c2 != null) {
                    if(c2.getNorth() != null)
                        System.out.println("adj Nord" + c2.getNorth());

                    if(c2.getSouth() != null)
                        System.out.println("adj Sud" + c2.getSouth());

                    if(c2.getEast() != null)
                        System.out.println("adj Est" + c2.getEast());

                    if(c2.getWest() != null)
                        System.out.println("adj Ovest" + c2.getWest());
                }
            }
        }

    }*/
}
