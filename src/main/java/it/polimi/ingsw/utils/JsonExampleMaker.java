package it.polimi.ingsw.utils;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.PowerUpBag;
import it.polimi.ingsw.model.player.Stats;
import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.powerup.TagbackGrenade;
import it.polimi.ingsw.model.weapons.Damage;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.actions.Move;
import it.polimi.ingsw.view.actions.SkipAction;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;
import it.polimi.ingsw.view.updates.InitialUpdate;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.otherplayerturn.GrabTurnUpdate;
import it.polimi.ingsw.view.updates.otherplayerturn.MoveTurnUpdate;
import it.polimi.ingsw.view.updates.otherplayerturn.TurnUpdate;

import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonExampleMaker {

    public static void main(String[] args) {

        writeStats();
        writeInitialUpdateClass();
        writeInitialUpdate();

        writeUsePowerUp();
        writeMove();
        writeSkip();

        writeMoveTurnUpdate();
        writeGrabTurnUpdate();

        writeDamageList();


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

            CachedStats cachedStats = new CachedStats(stats.getPlayerId(),stats.getScore(),stats.getDeaths(),stats.getOnline(),stats.getMarks(),stats.getDmgTaken(),Model.getMap().cellToCoord(stats.getCurrentPosition()));


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
            List<Directions> moves = new ArrayList<>();

            moves.add(Directions.NORTH);
            moves.add(Directions.NORTH);
            moves.add(Directions.WEST);

            Move move = new Move(moves,finalpos);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/move.json");


            gson.toJson(move, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }
    }


    public static void writeInitialUpdateClass(){

        Gson gson = new Gson();

        try {

            List<String> names = new ArrayList<>();

            names.add("Alex");
            names.add("Bob");

            List<PlayerColor> colors =new ArrayList<>();

            colors.add(PlayerColor.BLUE);
            colors.add(PlayerColor.YELLOW);

            UpdateClass updateClass = new InitialUpdate(names,colors,-1,0);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/initialUpdateClass.json");


            gson.toJson(updateClass, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    public static void writeInitialUpdate(){

        Gson gson = new Gson();

        try {

            List<String> names = new ArrayList<>();

            names.add("Alex");
            names.add("Bob");

            List<PlayerColor> colors =new ArrayList<>();

            colors.add(PlayerColor.BLUE);
            colors.add(PlayerColor.YELLOW);

            InitialUpdate initialUpdate = new InitialUpdate(names,colors,-1,0);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/initialUpdate.json");


            gson.toJson(initialUpdate, writer);

            writer.write('\n');

            writer.write("Ciao");

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }
    }


    public static void writeUsePowerUp(){

        Gson gson = new Gson();

        try {

            JsonAction jsonAction = new NewtonAction(Color.BLUE,0,2, Directions.NORTH);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/usePowerUp.json");


            gson.toJson(jsonAction, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    public static void writeSkip(){

        Gson gson = new Gson();

        try {

            JsonAction jsonAction = new SkipAction();

            FileWriter writer = new FileWriter("resources/json/jsonComunication/skip.json");


            gson.toJson(jsonAction, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }

    }

    public static void writeMoveTurnUpdate(){

        Gson gson = new Gson();

        try {

            TurnUpdate turnUpdate = new MoveTurnUpdate(0);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/MoveTurnUpdate.json");


            gson.toJson(turnUpdate, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }

    }

    public static void writeGrabTurnUpdate(){

        Gson gson = new Gson();

        try {

            TurnUpdate turnUpdate = new GrabTurnUpdate(0,"Gun");

            FileWriter writer = new FileWriter("resources/json/jsonComunication/GrabTurnUpdate.json");


            gson.toJson(turnUpdate, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }

    }

    public static void writeDamageList(){

        Gson gson = new Gson();

        // int a, int b, boolean c, boolean d, boolean diff,int dm,boolean at

        List<Damage> damageList = new ArrayList<>();

        try{

            Damage damage = new Damage(2,3,true,false,false,5,true);

            damageList.add(damage);

            FileWriter writer = new FileWriter("resources/json/weapons/damageList.json");

            gson.toJson(damageList, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }
    }

}
