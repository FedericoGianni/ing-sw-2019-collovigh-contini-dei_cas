package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.JsonMap;
import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.utils.CellColor;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static it.polimi.ingsw.model.map.JsonMap.MAP_C;
import static it.polimi.ingsw.model.map.JsonMap.MAP_R;

class JsonMapTest {

    @Test
    void jsonMapRead(){
        JsonMap jsonMap = new JsonMap();
        jsonMap = JsonMap.genJsonMap(1);
        System.out.println("cella 0, 0: " + jsonMap.getCell(0,0));
        assert(jsonMap.getCell(0,0).getColor() == CellColor.BLUE);
        assert(jsonMap.getCell(1,1).getAdjEast().equals(new Point(1,2)));
        assert (jsonMap.getCell(0,0).getAdjEast().equals(new Point(0,1)));

        jsonMap = JsonMap.genJsonMap(2);
        assert (jsonMap.getCell(0,0).getAdjEast().equals(new Point(0,1)));

    }

    @Test
    void createMapfromJsonMap(){

        //TO generate json from jsonMap class
        /*
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        JsonMap jsonMap = new JsonMap();
        jsonMap.generateCells(3);
        jsonMap.populateJson();
        gson.toJson(jsonMap);
        System.out.println(gson.toJson(jsonMap));
        */


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

    }

    @Test
    void genJsonMapFromRealMap(){
        Map m = new Map(JsonMap.genJsonMap(2));

        assert (m.getCell(0,0).getEast().equals(m.getCell(0,1)));

        JsonMap jsonMap = new JsonMap(m);

        assert(jsonMap.getMapType() == 2);
        assert (jsonMap.getCell(0,0).getCellType().equals(CellType.AMMO));
        assert (jsonMap.getCell(0,1).getColor().equals(CellColor.BLUE));

        assert (jsonMap.getCell(0,0).getAdjNorth() == null);
        assert (jsonMap.getCell(0,0).getAdjSouth().equals(new Point(1,0)));
        assert (jsonMap.getCell(0,0).getAdjWest() == null);

        assert (jsonMap.getCell(2,0) == null);

        assert (jsonMap.getCell(0,0).getAdjEast().equals(new Point(0,1)));
    }
}

