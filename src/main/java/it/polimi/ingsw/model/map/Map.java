package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;

import java.awt.*;

import static java.lang.Math.abs;

/**
 * Class representing the board game Map. It can be of 3 different types depending on the number of players.
 *
 */
public class Map {

    private static final int MAP_R = 3;
    private static final int MAP_C = 4;


    private Cell[][] matrix;

    private int mapType;

    public Cell[][] getMatrix() {
        return matrix.clone();
    }

    /**
     * Default constructor
     * mapType is set at 2 because this default type is valid for every number of players
     */
    public Map() {
        this.mapType = 1;
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

    /**
     * Generates a new Map from a jsonMap
     * @param jsonMap simplified map which can be read/stored in a .json file
     */
    public Map(JsonMap jsonMap){

        this.mapType = jsonMap.getMapType();
        this.matrix = new Cell[MAP_R][MAP_C];

        //recreating cells by readign type from json
        for (int i = 0; i < MAP_R; i++) {
            for (int j = 0; j < MAP_C; j++) {
                if(jsonMap.getCell(i,j) != null){
                    if(jsonMap.getCell(i,j).getCellType().equals(CellType.AMMO)) {
                        this.matrix[i][j] = new AmmoCell();
                    } else {
                        this.matrix[i][j] = new SpawnCell();
                    }
                } else {
                    this.matrix[i][j] = null;
                }
            }
        }

        setAdjacencesFromJson(jsonMap);
    }

    public void setAdjacencesFromJson(JsonMap jsonMap){

        //setting adjacences from jsonMap to Map
        for (int i = 0; i < MAP_R; i++) {
            for (int j = 0; j < MAP_C; j++) {
                if(this.matrix[i][j] != null){
                    if(jsonMap.getCell(i,j).getAdjNorth() != null)
                        this.matrix[i][j].setAdjNorth(this.getCell(jsonMap.getCell(i,j).getAdjNorth().x, jsonMap.getCell(i,j).getAdjNorth().y));
                    else
                        this.matrix[i][j].setAdjNorth(null);

                    if(jsonMap.getCell(i,j).getAdjSouth() != null)
                        this.matrix[i][j].setAdjSouth(this.getCell(jsonMap.getCell(i, j).getAdjSouth().x, jsonMap.getCell(i, j).getAdjSouth().y));
                    else
                        this.matrix[i][j].setAdjSouth(null);

                    if(jsonMap.getCell(i,j).getAdjEast() != null)
                        this.matrix[i][j].setAdjEast(this.getCell(jsonMap.getCell(i, j).getAdjEast().x, jsonMap.getCell(i, j).getAdjEast().y));
                    else
                        this.matrix[i][j].setAdjEast(null);

                    if(jsonMap.getCell(i,j).getAdjWest() != null)
                        this.matrix[i][j].setAdjWest(this.getCell(jsonMap.getCell(i, j).getAdjWest().x, jsonMap.getCell(i, j).getAdjWest().y));
                    else
                        this.matrix[i][j].setAdjWest(null);

                    this.matrix[i][j].setColor(jsonMap.getCell(i,j).getColor());
                    this.matrix[i][j].setVisited(jsonMap.getCell(i,j).isVisit());
                    this.matrix[i][j].setAmmoCell(jsonMap.getCell(i,j).isAmmoCell());
                }
            }
        }
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

    public void updateObserver(){
        for(Cell[] c : this.matrix){
            for(Cell c2 : c){
                if(c2 != null && c2.isAmmoCell())
                    c2.updateAll(c2);
            }
        }
    }

    public void initializeSpawnCell(){

        for(Cell[] c : this.matrix){
            for(Cell c2 : c){
                if(c2 != null && !c2.isAmmoCell()){

                    SpawnCell cell = (SpawnCell) c2;

                    cell.populateWeapon();
                }


            }
        }

    }

    public void ReplaceAmmoCard(){

        AmmoCell cell;

        for(Cell[] c : this.matrix){

            for(Cell c2 : c){

                if(c2 != null && c2.isAmmoCell())

                    c2.generateAmmoCard();

            }
        }
    }

    /**
     * Method useful for canSee method inside, which records if canSee algorithm has already visited this Cell
     */
    public void setUnvisited() {

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

    /**
     *
     * @param p1
     * @param p2
     * @return
     */
    public static int getDist(Player p1, Player p2)//geometrical dstance, the distance in squares, like tractor beam I effect, we need a real method for distances
    {
        Map map= Model.getMap();
        Point d1, d2;
        d1 = map.cellToCoord(p1.getCurrentPosition());
        d2 = map.cellToCoord(p2.getCurrentPosition());

        return abs(d1.x - d2.x) + abs(d1.y - d2.y);

    }

    /**
     *
     * @param c1
     * @param c2
     * @return
     */
    public static int getDist(Cell c1, Cell c2)//geometrical dstance, the distance in squares, like tractor beam I effect, we need a real method for distances
    {
        Map map= Model.getMap();
        Point d1, d2;
        d1 = map.cellToCoord(c1);
        d2 = map.cellToCoord(c2);

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

    /**
     *
     * @param c spawn cell color
     * @return Spawn cell of the given color
     */
    public Cell getSpawnCell(Color c){

        switch (c){

            case RED:
                return getCell(1, 0);

            case BLUE:
                return getCell(0, 2);

            case YELLOW:
                return getCell(2,3);
        }

        return null;
    }
}