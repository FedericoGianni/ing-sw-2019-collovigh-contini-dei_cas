package it.polimi.ingsw.view.utils;

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
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.actions.Move;
import it.polimi.ingsw.view.actions.SkipAction;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.actions.usepowerup.PowerUpAction;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;
import it.polimi.ingsw.view.cachemodel.updates.InitialUpdate;
import it.polimi.ingsw.view.cachemodel.updates.Update;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;
import it.polimi.ingsw.view.cachemodel.updates.UpdateType;

import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonExampleMaker {

    public static void main(String[] args) {

        writeUsePowerUp();
        writeMove();
        writeSkip();


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

    public static void writePowerUpBagUpdate(){

        Gson gson = new Gson();

        try {

            PowerUp powerUp1 = new Newton(Color.BLUE);
            PowerUp powerUp2 = new TagbackGrenade(Color.RED);

            PowerUpBag bag = new PowerUpBag();
            bag.addItem(powerUp1);
            bag.addItem(powerUp2);

            Update cachedPowerUpBag = new CachedPowerUpBag(bag);

            UpdateClass updateClass = new UpdateClass(UpdateType.POWERUP_BAG,cachedPowerUpBag,0);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/update.json");


            gson.toJson(updateClass, writer);

            writer.write('\n');

            writer.write("Ciao");

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

            InitialUpdate initialUpdate = new InitialUpdate(names,colors,-1,0);

            UpdateClass updateClass = new UpdateClass(UpdateType.INITIAL,initialUpdate,0);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/initialUpdateClass.json");


            gson.toJson(updateClass, writer);

            writer.write('\n');

            writer.write("Ciao");

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

    public static void writeExample(){

        Gson gson = new Gson();

        try {

            PowerUp powerUp1 = new Newton(Color.BLUE);
            PowerUp powerUp2 = new TagbackGrenade(Color.RED);

            PowerUpBag bag = new PowerUpBag();
            bag.addItem(powerUp1);
            bag.addItem(powerUp2);

            Object updateClass = new CachedPowerUpBag(bag);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/example.json");


            gson.toJson(updateClass, writer);

            writer.flush();
            writer.close();

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    public static void writeCachedPowerUpBag(){

        Gson gson = new Gson();

        try {

            PowerUp powerUp1 = new Newton(Color.BLUE);
            PowerUp powerUp2 = new TagbackGrenade(Color.RED);

            PowerUpBag bag = new PowerUpBag();
            bag.addItem(powerUp1);
            bag.addItem(powerUp2);

            CachedPowerUpBag cbag = new CachedPowerUpBag(bag);

            FileWriter writer = new FileWriter("resources/json/jsonComunication/cachedPowerUpBag.json");


            gson.toJson(cbag, writer);

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

}
