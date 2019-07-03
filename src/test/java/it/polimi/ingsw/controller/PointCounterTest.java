package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.CurrentGame;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointCounterTest {

    @Test
    void calculatePointsFirstDeath() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        Player player01 = Model.getPlayer(0);
        Player player02 = Model.getPlayer(1);
        Player player03 = Model.getPlayer(2);

        // player 0 gets killed by player 1 and 2

        player01.addDmg(1,5);
        player01.addDmg(2,6);

        PointCounter pointCounter = new PointCounter(null);

        pointCounter.calculateTurnPoints();

        //System.out.println(player02.getStats().getScore());

        //System.out.println(player03.getStats().getScore());

        assertEquals(8,player03.getStats().getScore());

        assertEquals(6 + 1,player02.getStats().getScore());

        assertEquals(0,player01.getStats().getScore());


    }

    @Test
    void calculatePointsEighthDeath() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        Player player01 = Model.getPlayer(0);
        Player player02 = Model.getPlayer(1);
        Player player03 = Model.getPlayer(2);

        player01.setDeath(7);

        // player 0 gets killed by player 1 and 2

        player01.addDmg(1,5);
        player01.addDmg(2,6);

        PointCounter pointCounter = new PointCounter(null);

        pointCounter.calculateTurnPoints();

        //System.out.println(player02.getStats().getScore());

        //System.out.println(player03.getStats().getScore());

        assertEquals(0 ,player03.getStats().getScore());

        assertEquals(0 + 1,player02.getStats().getScore());

        assertEquals(0,player01.getStats().getScore());


    }


    @Test
    void calcGamePointsTie() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        Player player01 = Model.getPlayer(0);
        Player player02 = Model.getPlayer(1);
        Player player03 = Model.getPlayer(2);

        CurrentGame game = Model.getGame();

        game.addkills(0,true); // player 01 -> overkill (2)
        game.addkills(1,false); // player 02 -> kill (1)
        game.addkills(1,true); // player 02 -> overkill (2)
        game.addkills(0,false); // player 01 -> kill (1)

        PointCounter pointCounter = new PointCounter(null);

        pointCounter.calcGamePoints();

        assertEquals(8,player01.getStats().getScore());

        assertEquals(6,player02.getStats().getScore());

        assertEquals(0,player03.getStats().getScore());
    }

    @Test
    void calcGamePoints() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        Player player01 = Model.getPlayer(0);
        Player player02 = Model.getPlayer(1);
        Player player03 = Model.getPlayer(2);

        CurrentGame game = Model.getGame();

        game.addkills(0,true); // player 01 -> overkill (2)
        game.addkills(1,false); // player 02 -> kill (1)
        game.addkills(1,true); // player 02 -> overkill (2)
        game.addkills(0,true); // player 01 -> overkill (2)

        PointCounter pointCounter = new PointCounter(null);

        pointCounter.calcGamePoints();

        //System.out.println(" points: " + Model.getGame().getPlayers().stream().map( x -> x.getStats().getScore()).collect(Collectors.toList()));

        assertEquals(8,player01.getStats().getScore());

        assertEquals(6,player02.getStats().getScore());

        assertEquals(0,player03.getStats().getScore());
    }
}