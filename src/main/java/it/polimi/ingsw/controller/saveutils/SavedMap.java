package it.polimi.ingsw.controller.saveutils;

import it.polimi.ingsw.model.map.AmmoCell;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.SpawnCell;

import java.awt.*;
import java.io.Serializable;

import static it.polimi.ingsw.utils.CellColor.*;

/**
 * This class is used to serialize the Model game Map in a file to be read, allowing game persistence
 */
public class SavedMap implements Serializable {

    private static final int MAP_R = 3;
    private static final int MAP_C = 4;

    private final SavedCell[][] matrix;

    private final int mapType;

    public SavedMap( int mapType ){

        this.mapType = mapType;

        this.matrix = new SavedCell[MAP_R][MAP_C];

        generateCells(mapType);

    }

    public SavedMap(Cell[][] map, int mapType) {

        this.mapType = mapType;

        this.matrix = new SavedCell[MAP_R][MAP_C];

        for (int i = 0; i < MAP_R; i++) {

            for (int j = 0; j < MAP_C; j++) {

                this.matrix[i][j] = (map[i][j] == null) ? null : map[i][j].getSaveVersionOfCell();

            }
        }
    }

    public int getMapType() {
        return mapType;
    }

    public Cell[][] getRealMap(){

        Cell[][] realMap = new Cell[MAP_R][MAP_C];

        for (int i = 0; i < MAP_R; i++) {

            for (int j = 0; j < MAP_C; j++) {

                realMap[i][j] = (matrix[i][j] == null) ? null : matrix[i][j].getRealCell();

            }
        }

        return realMap;
    }

    private void generateCells(int mapType) {  // TODO delete

        switch (mapType){

            case 1:

                (this.matrix[0][0]) = new SavedCell(new AmmoCell());
                (this.matrix[0][1]) = new SavedCell(new AmmoCell());
                (this.matrix[0][2]) = new SavedCell(new SpawnCell());
                (this.matrix[0][3]) = null;

                (this.matrix[1][0]) =  new SavedCell(new SpawnCell());
                (this.matrix[1][1]) = new SavedCell(new AmmoCell());
                (this.matrix[1][2]) = new SavedCell(new AmmoCell());
                (this.matrix[1][3]) = new SavedCell(new AmmoCell());

                (this.matrix[2][0]) = null;
                (this.matrix[2][1]) = new SavedCell(new AmmoCell());
                (this.matrix[2][2]) = new SavedCell(new AmmoCell());
                (this.matrix[2][3]) = new SavedCell(new SpawnCell());

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

                (this.matrix[0][0]) =  new SavedCell(new AmmoCell());
                (this.matrix[0][1]) =  new SavedCell(new AmmoCell());
                (this.matrix[0][2]) = new SavedCell(new SpawnCell());
                (this.matrix[0][3]) =  new SavedCell(new AmmoCell());

                (this.matrix[1][0]) = new SavedCell(new SpawnCell());
                (this.matrix[1][1]) =  new SavedCell(new AmmoCell());
                (this.matrix[1][2]) =  new SavedCell(new AmmoCell());
                (this.matrix[1][3]) =  new SavedCell(new AmmoCell());

                (this.matrix[2][0]) = null;
                (this.matrix[2][1]) =  new SavedCell(new AmmoCell());
                (this.matrix[2][2]) =  new SavedCell(new AmmoCell());
                (this.matrix[2][3]) = new SavedCell(new SpawnCell());

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

                (this.matrix[0][0]) = new SavedCell(new AmmoCell());
                (this.matrix[0][1]) = new SavedCell(new AmmoCell());
                (this.matrix[0][2]) = new SavedCell(new SpawnCell());
                (this.matrix[0][3]) = new SavedCell(new AmmoCell());

                (this.matrix[1][0]) = new SavedCell(new SpawnCell());
                (this.matrix[1][1]) = new SavedCell(new AmmoCell());
                (this.matrix[1][2]) = new SavedCell(new AmmoCell());
                (this.matrix[1][3]) = new SavedCell(new AmmoCell());

                (this.matrix[2][0]) = new SavedCell(new AmmoCell());
                (this.matrix[2][1]) = new SavedCell(new AmmoCell());
                (this.matrix[2][2]) = new SavedCell(new AmmoCell());
                (this.matrix[2][3]) = new SavedCell(new SpawnCell());

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
}
