package it.polimi.ingsw.model;

import it.polimi.ingsw.customsexceptions.FrenzyActivatedException;
import it.polimi.ingsw.model.map.JsonMap;
import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CurrentGameTest {

    @Test
    void generateInstance() {

        List<Player> players = new ArrayList<>();
        Player p1 = new Player("test1", 0, PlayerColor.YELLOW);
        Player p2 = new Player("test2", 1, PlayerColor.BLUE);
        Player p3 = new Player("test3", 2, PlayerColor.PURPLE);
        players.add(p1);
        players.add(p2);
        players.add(p3);

        //Map m = Map.genMap(1);
        Map m = new Map(JsonMap.genJsonMap(1));

        CurrentGame c = new CurrentGame(players, m);

        assert(c.getPlayers().equals(players));
    }

    @Test
    void drawPowerUp() {
        List<Player> players = new ArrayList<>();
        Player p1 = new Player("test1", 0, PlayerColor.YELLOW);
        Player p2 = new Player("test2", 1, PlayerColor.BLUE);
        Player p3 = new Player("test3", 2, PlayerColor.PURPLE);
        players.add(p1);
        players.add(p2);
        players.add(p3);

        //Map m = Map.genMap(1);
        Map m = new Map(JsonMap.genJsonMap(1));

        CurrentGame c = new CurrentGame(players, m);
        PowerUp teleport = new Teleporter(Color.YELLOW);
        PowerUp venom = new TagbackGrenade(Color.YELLOW);
        PowerUp viewF = new TargetingScope(Color.YELLOW);
        PowerUp kinetic = new Newton(Color.YELLOW);
        c.discardPowerUp(teleport);

        for (int i = 0; i < 24; i++) {
            PowerUp test = c.drawPowerUp();

            assertTrue((test.getClass().equals(teleport.getClass())) ||
                    (test.getClass().equals(venom.getClass())) ||
                    (test.getClass().equals(kinetic.getClass())) ||
                    (test.getClass().equals(viewF.getClass())));
        }

        PowerUp test = c.drawPowerUp();
        assertTrue(teleport.getClass().equals(test.getClass()));
    }

    @Test
    void drawWeapon() {
        //TODO need to test it after NormalWeapon implementation
    }

    @Test
    void idToPlayer() {
        List<Player> players = new ArrayList<>();
        Player p1 = new Player("test1", 0, PlayerColor.YELLOW);
        Player p2 = new Player("test2", 1, PlayerColor.BLUE);
        Player p3 = new Player("test3", 2, PlayerColor.PURPLE);
        players.add(p1);
        players.add(p2);
        players.add(p3);

        //Map m = Map.genMap(1);
        Map m = new Map(JsonMap.genJsonMap(1));

        CurrentGame c = new CurrentGame(players, m);
        for (int id = 0; id < 3; id++) {
            assertEquals(players.get(id), c.idToPlayer(id));
        }

    }

    @Test
    void playerToId() {
        List<Player> players = new ArrayList<>();
        Player p1 = new Player("test1", 0, PlayerColor.YELLOW);
        Player p2 = new Player("test2", 1, PlayerColor.BLUE);
        Player p3 = new Player("test3", 2, PlayerColor.PURPLE);
        players.add(p3);
        players.add(p2);
        players.add(p1);

        //Map m = Map.genMap(1);
        Map m = new Map(JsonMap.genJsonMap(1));

        CurrentGame c = new CurrentGame(players, m);
        assertEquals(p1.getPlayerId(), c.playerToId(p1));
    }

    @Test
    void getPlayers() {

        List<Player> players = new ArrayList<>();
        Player p1 = new Player("test1", 0, PlayerColor.YELLOW);
        Player p2 = new Player("test2", 1, PlayerColor.BLUE);
        Player p3 = new Player("test3", 2, PlayerColor.PURPLE);
        players.add(p1);
        players.add(p2);
        players.add(p3);

        //Map m = Map.genMap(1);
        Map m = new Map(JsonMap.genJsonMap(1));

        CurrentGame c = new CurrentGame(players, m);
        assertEquals(c.getPlayers() , players);
    }

    @Test
    void addDeath() {

        List<Player> players = new ArrayList<>();
        players.add(new Player("Agent",0,PlayerColor.BLUE));
        players.add(new Player("Agent47",1, PlayerColor.YELLOW));

        CurrentGame currentGame = new CurrentGame(players,null);

        assertEquals(0,currentGame.getKillShotTrack().size());

        try {

            currentGame.addkills(0, true);

        }catch (FrenzyActivatedException e){
            e.printStackTrace();
        }

        assertEquals(0,currentGame.getKillShotTrack().get(0).getKillerId());
        assertEquals(2,currentGame.getKillShotTrack().get(0).getAmount());



    }

    @Test
    void shouldThrowFrenzyActivatedException(){

        List<Player> players = new ArrayList<>();
        players.add(new Player("Agent",0,PlayerColor.BLUE));
        players.add(new Player("Agent47",1, PlayerColor.YELLOW));

        CurrentGame currentGame = new CurrentGame(players,null);

        List<Boolean> list = new ArrayList<>();

        try{



            for (int i = 0; i < 10; i++) {

                list.add(false);

            }

            currentGame.addkills(0, list);



        }catch (FrenzyActivatedException e){

            assertEquals(0,e.getPlayerId());

            assertEquals(list.size(),currentGame.getKillShotTrack().size());
        }


    }
}