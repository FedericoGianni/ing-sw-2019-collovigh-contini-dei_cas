package it.polimi.ingsw.model;

import java.awt.*;

import static it.polimi.ingsw.model.CellColor.*;
import static java.lang.Math.abs;

/**
 * Class representing the board game Map. It can be of 3 different types depending on the number of players.
 *
 */
public class Map {

    private static final int MAP_R = 3;
    private static final int MAP_C = 4;

    /**
     * Default constructor
     * mapType is set at 2 because this default type is valid for every number of players
     */
    public Map() {
        this.mapType = 2;
        this.matrix = new Cell[MAP_R][MAP_C];
    }

    /**
     * This constructor is used to create a c
     * @param clone
     */
    public Map(Map clone){
        this.mapType = clone.mapType;
        this.matrix = clone.matrix;
    }

    private final Cell[][] matrix;

    private int mapType;

    public Cell[][] getMatrix() {
        return matrix.clone();
    }

    /**
     *
     * @param row int between 0 and 2 representing index of row in the matrix
     * @param col int between 0 and 3 representing index of column in the matrix
     * @return the Cell placed in row and col inside the matrix representing the Map
     */
    public Cell getCell(int row, int col){
        return this.matrix[row][col];
    }

    /**
     *
     * @param c Cell
     * @return a pair of int representing coordinates of the Cell c inside the matrix
     */
    public Point cellToCoord(Cell c){

        if(c == null)
            return null;

        for (int i = 0; i < MAP_R; i++)
            for (int j = 0; j < MAP_C; j++) {
                if (c == matrix[i][j])
                    return new Point(i, j);
            }

        return null;
    }

    /**
     *
     * @return a copy of the actual map
     */
    public Map getMapClone(){
        return new Map(this);
    }



    /**
     *
     * @return actual type of Map: 1, 2, 3
     */
    public int getMapType() {
        return mapType;
    }

    /**
     *
     * @param mapType integer representing the map type which can be chosen at the creation of the Map
     * @return a Map created based on the current playing map type
     */
    public static Map genMap(int mapType) {

        Map m = null;

        switch(mapType) {

            case 1:
                m = new Map();
                m.mapType = 1;
                m.generateCells(1);

                break;

            case 2:
                m = new Map();
                m.mapType = 2;
                m.generateCells(2);

                break;

            case 3:
                m = new Map();
                m.mapType = 3;
                m.generateCells(3);

                break;

            default:

                break;

        }

        return m;

    }

    /**
     *
     * @param mapType an integer representing the type of Map, passed as a parameter to populate the matrix of Cells in different modes
     */
    public void generateCells(int mapType) {

        switch (mapType){

            case 1:

                (this.matrix[0][0]) = new AmmoCell();
                (this.matrix[0][1]) = new AmmoCell();
                (this.matrix[0][2]) = new SpawnCell();
                (this.matrix[0][3]) = null;

                (this.matrix[1][0]) = new SpawnCell();
                (this.matrix[1][1]) = new AmmoCell();
                (this.matrix[1][2]) = new AmmoCell();
                (this.matrix[1][3]) = new AmmoCell();

                (this.matrix[2][0]) = null;
                (this.matrix[2][1]) = new AmmoCell();
                (this.matrix[2][2]) = new AmmoCell();
                (this.matrix[2][3]) = new SpawnCell();

                (this.matrix[0][0]).setAdjNorth(null);
                (this.matrix[0][0]).setAdjSouth(this.matrix[1][0]);
                (this.matrix[0][0]).setAdjEast(this.matrix[0][1]);
                (this.matrix[0][0]).setAdjWest(null);
                (this.matrix[0][0]).setColor(BLUE);

                (this.matrix[0][1]).setAdjNorth(null);
                (this.matrix[0][1]).setAdjSouth(null);
                (this.matrix[0][1]).setAdjEast(this.matrix[0][2]);
                (this.matrix[0][1]).setAdjWest(this.matrix[0][0]);
                (this.matrix[0][1]).setColor(BLUE);

                (this.matrix[0][2]).setAdjNorth(null);
                (this.matrix[0][2]).setAdjSouth(this.matrix[1][2]);
                (this.matrix[0][2]).setAdjEast(null);
                (this.matrix[0][2]).setAdjWest(this.matrix[0][1]);
                (this.matrix[0][2]).setColor(BLUE);

                (this.matrix[1][0]).setAdjNorth(this.matrix[0][0]);
                (this.matrix[1][0]).setAdjSouth(null);
                (this.matrix[1][0]).setAdjEast(this.matrix[1][1]);
                (this.matrix[1][0]).setAdjWest(null);
                (this.matrix[1][0]).setColor(RED);

                (this.matrix[1][1]).setAdjNorth(null);
                (this.matrix[1][1]).setAdjSouth(this.matrix[2][1]);
                (this.matrix[1][1]).setAdjEast(this.matrix[1][2]);
                (this.matrix[1][1]).setAdjWest(this.matrix[1][0]);
                (this.matrix[1][1]).setColor(RED);

                (this.matrix[1][2]).setAdjNorth(this.matrix[0][2]);
                (this.matrix[1][2]).setAdjSouth(null);
                (this.matrix[1][2]).setAdjEast(this.matrix[1][3]);
                (this.matrix[1][2]).setAdjWest(this.matrix[1][1]);
                //this cell is actually purple but in the same room as the Red ones, so we treat it just like a red one
                (this.matrix[1][2]).setColor(RED);

                (this.matrix[1][3]).setAdjNorth(null);
                (this.matrix[1][3]).setAdjSouth(this.matrix[2][3]);
                (this.matrix[1][3]).setAdjEast(null);
                (this.matrix[1][3]).setAdjWest(this.matrix[1][2]);
                (this.matrix[1][3]).setColor(YELLOW);

                (this.matrix[2][0]) = null;

                (this.matrix[2][1]).setAdjNorth(this.matrix[1][1]);
                (this.matrix[2][1]).setAdjSouth(null);
                (this.matrix[2][1]).setAdjEast(this.matrix[2][2]);
                (this.matrix[2][1]).setAdjWest(null);
                (this.matrix[2][1]).setColor(GREY);

                (this.matrix[2][2]).setAdjNorth(null);
                (this.matrix[2][2]).setAdjSouth(null);
                (this.matrix[2][2]).setAdjEast(this.matrix[2][3]);
                (this.matrix[2][2]).setAdjWest(this.matrix[2][1]);
                (this.matrix[2][2]).setColor(GREY);

                (this.matrix[2][3]).setAdjNorth(this.matrix[1][3]);
                (this.matrix[2][3]).setAdjSouth(null);
                (this.matrix[2][3]).setAdjEast(null);
                (this.matrix[2][3]).setAdjWest(this.matrix[2][2]);
                (this.matrix[2][3]).setColor(YELLOW);

                break;

            case 2:

                (this.matrix[0][0]) = new AmmoCell();
                (this.matrix[0][1]) = new AmmoCell();
                (this.matrix[0][2]) = new SpawnCell();
                (this.matrix[0][3]) = new AmmoCell();

                (this.matrix[1][0]) = new SpawnCell();
                (this.matrix[1][1]) = new AmmoCell();
                (this.matrix[1][2]) = new AmmoCell();
                (this.matrix[1][3]) = new AmmoCell();

                (this.matrix[2][0]) = null;
                (this.matrix[2][1]) = new AmmoCell();
                (this.matrix[2][2]) = new AmmoCell();
                (this.matrix[2][3]) = new SpawnCell();

                (this.matrix[0][0]).setAdjNorth(null);
                (this.matrix[0][0]).setAdjSouth(this.matrix[1][0]);
                (this.matrix[0][0]).setAdjEast(this.matrix[0][1]);
                (this.matrix[0][0]).setAdjWest(null);
                (this.matrix[0][0]).setColor(BLUE);

                (this.matrix[0][1]).setAdjNorth(null);
                (this.matrix[0][1]).setAdjSouth(null);
                (this.matrix[0][1]).setAdjEast(this.matrix[0][2]);
                (this.matrix[0][1]).setAdjWest(this.matrix[0][0]);
                (this.matrix[0][1]).setColor(BLUE);

                (this.matrix[0][2]).setAdjNorth(null);
                (this.matrix[0][2]).setAdjSouth(this.matrix[1][2]);
                (this.matrix[0][2]).setAdjEast(this.matrix[0][3]);
                (this.matrix[0][2]).setAdjWest(this.matrix[0][1]);
                (this.matrix[0][2]).setColor(BLUE);

                (this.matrix[0][3]).setAdjNorth(null);
                (this.matrix[0][3]).setAdjSouth(this.matrix[1][3]);
                (this.matrix[0][3]).setAdjEast(null);
                (this.matrix[0][3]).setAdjWest(this.matrix[0][2]);
                (this.matrix[0][3]).setColor(GREEN);

                (this.matrix[1][0]).setAdjNorth(this.matrix[0][0]);
                (this.matrix[1][0]).setAdjSouth(null);
                (this.matrix[1][0]).setAdjEast(this.matrix[1][1]);
                (this.matrix[1][0]).setAdjWest(null);
                (this.matrix[1][0]).setColor(RED);

                (this.matrix[1][1]).setAdjNorth(null);
                (this.matrix[1][1]).setAdjSouth(this.matrix[2][1]);
                (this.matrix[1][1]).setAdjEast(null);
                (this.matrix[1][1]).setAdjWest(this.matrix[1][0]);
                (this.matrix[1][1]).setColor(RED);

                (this.matrix[1][2]).setAdjNorth(this.matrix[0][2]);
                (this.matrix[1][2]).setAdjSouth(this.matrix[2][2]);
                (this.matrix[1][2]).setAdjEast(this.matrix[1][3]);
                (this.matrix[1][2]).setAdjWest(null);
                (this.matrix[1][2]).setColor(YELLOW);

                (this.matrix[1][3]).setAdjNorth(this.matrix[0][3]);
                (this.matrix[1][3]).setAdjSouth(this.matrix[2][3]);
                (this.matrix[1][3]).setAdjEast(null);
                (this.matrix[1][3]).setAdjWest(this.matrix[1][2]);
                (this.matrix[1][3]).setColor(YELLOW);

                (this.matrix[2][1]).setAdjNorth(this.matrix[1][1]);
                (this.matrix[2][1]).setAdjSouth(null);
                (this.matrix[2][1]).setAdjEast(this.matrix[2][2]);
                (this.matrix[2][1]).setAdjWest(null);
                (this.matrix[2][1]).setColor(GREY);

                (this.matrix[2][2]).setAdjNorth(this.matrix[1][2]);
                (this.matrix[2][2]).setAdjSouth(null);
                (this.matrix[2][2]).setAdjEast(this.matrix[2][3]);
                (this.matrix[2][2]).setAdjWest(this.matrix[2][1]);
                (this.matrix[2][2]).setColor(YELLOW);

                (this.matrix[2][3]).setAdjNorth(this.matrix[1][3]);
                (this.matrix[2][3]).setAdjSouth(null);
                (this.matrix[2][3]).setAdjEast(null);
                (this.matrix[2][3]).setAdjWest(this.matrix[2][2]);
                (this.matrix[2][3]).setColor(YELLOW);

                break;

            case 3:

                (this.matrix[0][0]) = new AmmoCell();
                (this.matrix[0][1]) = new AmmoCell();
                (this.matrix[0][2]) = new SpawnCell();
                (this.matrix[0][3]) = new AmmoCell();

                (this.matrix[1][0]) = new SpawnCell();
                (this.matrix[1][1]) = new AmmoCell();
                (this.matrix[1][2]) = new AmmoCell();
                (this.matrix[1][3]) = new AmmoCell();

                (this.matrix[2][0]) = new AmmoCell();
                (this.matrix[2][1]) = new AmmoCell();
                (this.matrix[2][2]) = new AmmoCell();
                (this.matrix[2][3]) = new SpawnCell();

                (this.matrix[0][0]).setAdjNorth(null);
                (this.matrix[0][0]).setAdjSouth(this.matrix[1][0]);
                (this.matrix[0][0]).setAdjEast(this.matrix[0][1]);
                (this.matrix[0][0]).setAdjWest(null);
                (this.matrix[0][0]).setColor(RED);

                (this.matrix[0][1]).setAdjNorth(null);
                (this.matrix[0][1]).setAdjSouth(this.matrix[1][1]);
                (this.matrix[0][1]).setAdjEast(this.matrix[0][2]);
                (this.matrix[0][1]).setAdjWest(this.matrix[0][0]);
                (this.matrix[0][1]).setColor(BLUE);

                (this.matrix[0][2]).setAdjNorth(null);
                (this.matrix[0][2]).setAdjSouth(this.matrix[1][2]);
                (this.matrix[0][2]).setAdjEast(this.matrix[0][3]);
                (this.matrix[0][2]).setAdjWest(this.matrix[0][1]);
                (this.matrix[0][2]).setColor(BLUE);

                (this.matrix[0][3]).setAdjNorth(null);
                (this.matrix[0][3]).setAdjSouth(this.matrix[1][3]);
                (this.matrix[0][3]).setAdjEast(null);
                (this.matrix[0][3]).setAdjWest(this.matrix[0][2]);
                (this.matrix[0][3]).setColor(GREEN);

                (this.matrix[1][0]).setAdjNorth(this.matrix[0][0]);
                (this.matrix[1][0]).setAdjSouth(this.matrix[2][0]);
                (this.matrix[1][0]).setAdjEast(null);
                (this.matrix[1][0]).setAdjWest(null);
                (this.matrix[1][0]).setColor(RED);

                (this.matrix[1][1]).setAdjNorth(this.matrix[0][1]);
                (this.matrix[1][1]).setAdjSouth(this.matrix[2][1]);
                (this.matrix[1][1]).setAdjEast(null);
                (this.matrix[1][1]).setAdjWest(null);
                (this.matrix[1][1]).setColor(PURPLE);

                (this.matrix[1][2]).setAdjNorth(this.matrix[0][2]);
                (this.matrix[1][2]).setAdjSouth(this.matrix[2][2]);
                (this.matrix[1][2]).setAdjEast(this.matrix[1][3]);
                (this.matrix[1][2]).setAdjWest(null);
                (this.matrix[1][2]).setColor(YELLOW);

                (this.matrix[1][3]).setAdjNorth(this.matrix[0][3]);
                (this.matrix[1][3]).setAdjSouth(this.matrix[2][3]);
                (this.matrix[1][3]).setAdjEast(null);
                (this.matrix[1][3]).setAdjWest(this.matrix[1][2]);
                (this.matrix[1][3]).setColor(YELLOW);

                (this.matrix[2][0]).setAdjNorth(this.matrix[1][0]);
                (this.matrix[2][0]).setAdjSouth(null);
                (this.matrix[2][0]).setAdjEast(this.matrix[2][1]);
                (this.matrix[2][0]).setAdjWest(null);
                (this.matrix[2][0]).setColor(GREY);

                (this.matrix[2][1]).setAdjNorth(this.matrix[1][1]);
                (this.matrix[2][1]).setAdjSouth(null);
                (this.matrix[2][1]).setAdjEast(this.matrix[2][2]);
                (this.matrix[2][1]).setAdjWest(this.matrix[2][0]);
                (this.matrix[2][1]).setColor(GREY);

                (this.matrix[2][2]).setAdjNorth(this.matrix[1][2]);
                (this.matrix[2][2]).setAdjSouth(null);
                (this.matrix[2][2]).setAdjEast(this.matrix[2][3]);
                (this.matrix[2][2]).setAdjWest(this.matrix[2][1]);
                (this.matrix[2][2]).setColor(YELLOW);

                (this.matrix[2][3]).setAdjNorth(this.matrix[1][3]);
                (this.matrix[2][3]).setAdjSouth(null);
                (this.matrix[2][3]).setAdjEast(null);
                (this.matrix[2][3]).setAdjWest(this.matrix[2][2]);
                (this.matrix[2][3]).setColor(YELLOW);

                break;

            default:

                break;
        }
    }

    /**
     * Method useful for canSee method inside, which records if canSee algorithm has already visited this Cell
     */
    void setUnvisited() {

    //always call after a search for set the cells unvisited
        int cont = 0;
        int r = 0;
        int c = 0;
        for(r = 0; r < MAP_R; r++) {
            for(c = 0; c < MAP_C; c++) {
                if(getCell(r,c) != null)
                    getCell(r,c).unvisit();
            }
        }
    }




    public static int getDist(Player p1,Player p2)
    {
        Map map=Model.getMap();
        Point d1, d2;
        d1 = map.cellToCoord(p1.getCurrentPosition());
        d2 = map.cellToCoord(p2.getCurrentPosition());

        return abs(d1.x - d2.x) + abs(d1.y - d2.y);

    }

    /**
     *
     * @param cell is the cell we want to see if exist
     * @return null if asked cell is null or if the cell is not in the matrix
     */
    public boolean hasCell(Cell cell){

        if (cell == null){ return false;}

        for (int i = 0; i < MAP_R; i++) {
            for (int j = 0; j < MAP_C; j++) {

                if (cell == matrix[i][j])
                    return true;
                
            }
            
        }

        return false;
    }

}