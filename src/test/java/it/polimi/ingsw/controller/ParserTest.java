package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.saveutils.SavedMap;
import it.polimi.ingsw.model.CurrentGame;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.powerup.Newton;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.network.Config;
import it.polimi.ingsw.network.networkexceptions.GameNonExistentException;
import it.polimi.ingsw.runner.RunClient;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PlayerColor;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.runner.RunServer.CONFIG_PATH;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void getMap1() {

        SavedMap savedMap= Parser.getMap(1);

        assertNotNull(savedMap);

        Cell[][] matrix = savedMap.getRealMap();

        System.out.println( "\nMAP 1 :");

        for (int i = 0; i <3; i++) {

            for (int j = 0; j < 4; j++) {

                System.out.println((matrix[i][j] == null) ? "Cell : " + i + " " + j + " " +"null" : "Cell : " + i + " " + j + " " + matrix[i][j].getColor());

            }
        }
    }

    @Test
    void getMap2() {

        SavedMap savedMap= Parser.getMap(2);

        assertNotNull(savedMap);

        Cell[][] matrix = savedMap.getRealMap();

        System.out.println( "\nMAP 2 :");

        for (int i = 0; i <3; i++) {

            for (int j = 0; j < 4; j++) {

                System.out.println((matrix[i][j] == null) ? "Cell : " + i + " " + j + " " +"null" : "Cell : " + i + " " + j + " " + matrix[i][j].getColor());

            }
        }
    }

    @Test
    void getMap3() {

        SavedMap savedMap= Parser.getMap(3);

        assertNotNull(savedMap);

        Cell[][] matrix = savedMap.getRealMap();

        System.out.println( "\nMAP 3 :");

        for (int i = 0; i <3; i++) {

            for (int j = 0; j < 4; j++) {

                System.out.println((matrix[i][j] == null) ? "Cell : " + i + " " + j + " " +"null" : "Cell : " + i + " " + j + " " + matrix[i][j].getColor());

                if (matrix[i][j].isAmmoCell()) assertNotNull(matrix[i][j].getAmmoPlaced());

            }
        }
    }

    @Test
    void shouldSaveAndRestoreMap(){

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        Cell[][] matrix = Model.getMap().getMatrix();

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(0,2));

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(1,1));

        Model.getPlayer(0).setPlayerPos(Model.getMap().getCell(2,2));

        Parser.addGame();

        Parser.saveMap();

        assertNotNull(Parser.readSavedMap().getRealMap());

        Cell[][] saved = Parser.readSavedMap().getRealMap();

        // check player

        assertEquals(matrix[0][2].getPlayers(), saved[0][2].getPlayers());

        //check Weapons

        assertEquals(matrix[0][2].getWeapons().stream().map(Weapon::getName).collect(Collectors.toList()), saved[0][2].getWeapons().stream().map(Weapon::getName).collect(Collectors.toList()));

        for (int i = 0; i <3; i++) {

            for (int j = 0; j < 4; j++) {

                //System.out.println((matrix[i][j] == null) ? "[SOURCE] Cell : " + i + " " + j + " " +"null" : "Cell : " + i + " " + j + " " + matrix[i][j].getColor());

                //System.out.println((saved[i][j] == null) ? "[SAVE] Cell : " + i + " " + j + " " +"null" : "Cell : " + i + " " + j + " " + saved[i][j].getColor());


                //System.out.println("");

            }

            //System.out.println("\n");
        }

        Parser.clearGames();
    }



    @Test
    void containsGame() {

        Parser.clearGames();

        int gameId = Parser.addGame(); // game 0

        assertTrue(Parser.containsGame(gameId));

        Parser.clearGames();
    }

    @Test
    void addGame() {

        int size = Parser.getGamesSize();

        Parser.addGame();
        Parser.addGame();

        assertEquals(size + 2, Parser.getGamesSize());

        Parser.clearGames();
    }

    @Test
    void closeGame() {

        try {

            Parser.clearGames();

            int size = Parser.getGamesSize();

            int gameId = Parser.addGame();

            assertTrue(Parser.containsGame(gameId));

            Parser.closeGame(gameId);

            assertFalse(Parser.containsGame(gameId));

        }catch (GameNonExistentException e){

            e.printStackTrace();
        }

        Parser.clearGames();

    }

    @Test
    void clearGames() {

        Parser.clearGames();

        assertEquals(0,Parser.getGamesSize());

    }

    @Test
    void testPath() {

        // gets the internal resources

        Gson gson = new Gson();

        // gets input stream

        InputStream inputStream = RunClient.class.getResourceAsStream("/json/maps/map_01.json" );

        // creates a reader for the file

        BufferedReader br = new BufferedReader( new InputStreamReader(inputStream));

        // load the Config File

        SavedMap config = gson.fromJson(br, SavedMap.class);

        System.out.println(config);


        System.out.println("---------------------------");

        inputStream = Parser.class.getResourceAsStream( "/json/maps/map_01.json" );

        try {

            // creates a reader for the file

            br = new BufferedReader( new InputStreamReader(inputStream));

            // load the Config File

            SavedMap savedMap = gson.fromJson(br, SavedMap.class);

            // LOG the load

        }catch (Exception e){

            e.printStackTrace();

            System.out.println( " could not read the specified map ");
        }

    }

    @Test
    void savePlayers() {

        List<String> playerNames = new ArrayList<>();
        playerNames.add("Player_0");
        playerNames.add("Player_1");
        playerNames.add("Player_2");

        List<PlayerColor> playerColors = new ArrayList<>();
        playerColors.add(PlayerColor.BLUE);
        playerColors.add(PlayerColor.PURPLE);
        playerColors.add(PlayerColor.YELLOW);

        //generating model
        Model currentModel = new Model(playerNames, playerColors, 2,8);

        Player p0 = Model.getPlayer(0);
        p0.addCube(new AmmoCube(Color.YELLOW));
        p0.addPowerUp(new Newton(Color.YELLOW));

        Player p1 = Model.getPlayer(1);
        p1.addCube(new AmmoCube(Color.BLUE));
        p1.addWeapon(Parser.getWeaponByName("LOCK RIFLE"));

        Player p2 = Model.getPlayer(1);
        p2.addCube(new AmmoCube(Color.BLUE));
        p2.addWeapon(Parser.getWeaponByName("LOCK RIFLE"));
        p2.addDmg(0, 3);
        p2.addMarks(1, 3);
        p2.addScore(13);


        //Parser.savePlayers();



    }

    @Test
    void readPlayers() {

        List<String> playerNames = new ArrayList<>();
        playerNames.add("Player_0");
        playerNames.add("Player_1");
        playerNames.add("Player_2");

        List<PlayerColor> playerColors = new ArrayList<>();
        playerColors.add(PlayerColor.BLUE);
        playerColors.add(PlayerColor.PURPLE);
        playerColors.add(PlayerColor.YELLOW);

        //generating model
        Model currentModel = new Model(playerNames, playerColors, 2,8);

        Player p0 = Model.getPlayer(0);
        p0.addCube(new AmmoCube(Color.YELLOW));
        p0.addPowerUp(new Newton(Color.YELLOW));
        p0.setPlayerPos(Model.getMap().getCell(0,0));

        Player p1 = Model.getPlayer(1);
        p1.addCube(new AmmoCube(Color.BLUE));
        p1.addWeapon(Parser.getWeaponByName("LOCK RIFLE"));

        Player p2 = Model.getPlayer(1);
        p2.addCube(new AmmoCube(Color.BLUE));
        p2.addWeapon(Parser.getWeaponByName("LOCK RIFLE"));
        p2.addDmg(0, 3);
        p2.addMarks(1, 3);
        p2.addScore(13);

        //Parser.savePlayers();

        //List<Player> loadedPlayers = Parser.readPlayers();

        //System.out.println("p0 from model: " + p0.getStats().getCurrentPosition());
        //System.out.println("p0 read from file: " + loadedPlayers.get(0).getStats().getCurrentPosition());

        //assert(p0.getStats().equals(loadedPlayers.get(0).getStats()));
        //assert(p1.getStats().getMarks().size()==loadedPlayers.get(1).getStats().getMarks().size());
        //assertEquals(Model.getMap().getCell(0,0), p0.getCurrentPosition());

    }

    @Test
    void saveController() {

        List<String> playerNames = new ArrayList<>();
        playerNames.add("Player_0");
        playerNames.add("Player_1");
        playerNames.add("Player_2");

        List<PlayerColor> playerColors = new ArrayList<>();
        playerColors.add(PlayerColor.BLUE);
        playerColors.add(PlayerColor.PURPLE);
        playerColors.add(PlayerColor.YELLOW);

        //Controller controller = new Controller(playerNames, playerColors, -1, 8);

        //Parser.saveController(controller);


    }

    @Test
    void readController() {

        List<String> playerNames = new ArrayList<>();
        playerNames.add("Player_0");
        playerNames.add("Player_1");
        playerNames.add("Player_2");

        List<PlayerColor> playerColors = new ArrayList<>();
        playerColors.add(PlayerColor.BLUE);
        playerColors.add(PlayerColor.PURPLE);
        playerColors.add(PlayerColor.YELLOW);

        //Controller controller = new Controller(playerNames, playerColors, -1, 8);

        //Parser.saveController(controller);

        //Controller controller1 = Parser.readController();

        //assertEquals(controller.getFrenzy(), controller1.getFrenzy());
        //assertEquals(controller.getGameId(), controller1.getGameId());
        //assertEquals(controller.getRoundNumber(), controller1.getRoundNumber());
    }

    @Test
    void saveCurrentGame() {

        List<Player> players = new ArrayList<>();
        players.add(new Player("a", 0, PlayerColor.YELLOW));

        CurrentGame currentGame = new CurrentGame(players, null, 3);

        //Parser.saveCurrentGame(currentGame);
    }

    @Test
    void readCurrentGame() {

        List<Player> players = new ArrayList<>();
        players.add(new Player("a", 0, PlayerColor.YELLOW));

        CurrentGame currentGame = new CurrentGame(players, null, 3);

        //Parser.saveCurrentGame(currentGame);

        //SavedCurrentGame loadedCurrentGame = Parser.readCurrentGame();

        //CurrentGame realCurrentGame = new CurrentGame(loadedCurrentGame, players, null);

        //assertEquals(currentGame.getSkulls(), realCurrentGame.getSkulls());
        //assertEquals(currentGame.getPowerUpDeck().getPowerUpList().get(1).getType(), realCurrentGame.getPowerUpDeck().getPowerUpList().get(1).getType());
        //assertEquals(currentGame.getKillShotTrack(), realCurrentGame.getKillShotTrack());

    }

    @Test
    void readConfigFile(){

        Config config = new Config(22222);

        assertEquals(-1, config.getGame());

        try{

            Gson   gson = new Gson();

            FileWriter writer = new FileWriter(CONFIG_PATH);

            gson.toJson(config, writer);

            writer.flush();
            writer.close();

        }catch (IOException e){

        }

        Config config1 = Parser.readConfigFile();

        assertEquals(config.getGame(), config1.getGame());


    }
}