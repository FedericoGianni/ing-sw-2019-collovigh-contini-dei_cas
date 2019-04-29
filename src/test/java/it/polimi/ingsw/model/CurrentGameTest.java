package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CurrentGameTest {

    @Test
    void generateInstance() {

        List<Player> players = new ArrayList<>();
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        Map m = Map.genMap(1);

        CurrentGame c = new CurrentGame(players, m);

        assert(c.getPlayers().equals(players));
    }

    @Test
    void drawPowerUp() {
        List<Player> players = new ArrayList<>();
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        Map m = Map.genMap(1);

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
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        Map m = Map.genMap(1);

        CurrentGame c = new CurrentGame(players, m);
        for (int id = 0; id < 3; id++) {
            assertEquals(players.get(id), c.idToPlayer(id));
        }

    }

    @Test
    void playerToId() {
        List<Player> players = new ArrayList<>();
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        Map m = Map.genMap(1);

        CurrentGame c = new CurrentGame(players, m);
        assertEquals(players.indexOf(p1), c.playerToId(p1));
    }

    @Test
    void getPlayers() {

        List<Player> players = new ArrayList<>();
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        Map m = Map.genMap(1);

        CurrentGame c = new CurrentGame(players, m);
        assertEquals(c.getPlayers() , players);
    }
}