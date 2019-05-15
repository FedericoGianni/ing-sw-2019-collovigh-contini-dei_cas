package it.polimi.ingsw.view.cachemodel;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.Stats;
import it.polimi.ingsw.view.actions.Move;
import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;

import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonExampleMaker {

    public static void main(String[] args) {

        writeStats();

        writeMove();

    }

    public static void writeStats(){

        Gson gson = new Gson();


        List<String> playerNames = new ArrayList<>();
        playerNames.add("Player_1");
        playerNames.add("Player_2");
        playerNames.add("Player_3");

        List<PlayerColor> playerColors = new ArrayList<>();
        playerColors.add(PlayerColor.BLUE);
        playerColors.add(PlayerColor.PURPLE);
        playerColors.add(PlayerColor.YELLOW);

        //generating model
        Model currentModel = new Model(playerNames, playerColors, 2);

        Cell cell = Model.getMap().getCell(1,1);


        try {

            Stats stats = new Stats(cell);

            stats.setDeaths(2);
            stats.setScore(10);

            stats.addDmgTaken(2,0);
            stats.addDmgTaken(1,1);

            stats.addMarks(0);

            CachedStats cachedStats = new CachedStats(stats);


            FileWriter writer = new FileWriter("resources/json/jsonComunication/stats.json");


            gson.toJson(cachedStats, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    public static void writeMove(){

        Gson gson = new Gson();

        try {

            Point finalpos = new Point(2,2);
            List<String> moves = new ArrayList<>();

            moves.add("n");
            moves.add("n");
            moves.add("w");

            Move move = new Move(finalpos, moves);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/move.json");


            gson.toJson(move, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }
    }

}
