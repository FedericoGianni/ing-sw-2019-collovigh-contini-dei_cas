package it.polimi.ingsw.model;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.map.AmmoCell;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.model.player.Stats;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatsTest {

    @Test
    void setMarks() {

        Cell cell = new AmmoCell();
        Stats stats = new Stats();

        stats.setCurrentPosition(cell);

        List<Integer> list = new ArrayList<>();

        list.add(0);
        list.add(1);
        list.add(2);


        try {
            stats.setMarks(list);
        }catch(OverMaxMarkException e){
            e.printStackTrace();
        }

        list.add(4);

        assertThrows(OverMaxMarkException.class,() -> {
            stats.setMarks(list);
        });

        List<Integer> list2= new ArrayList<>(list);
        list2.remove(3);

        assertEquals(list2, stats.getMarks());
    }

    @Test
    void getMarks(){

        Cell cell = new AmmoCell();
        Stats stats = new Stats();

        stats.setCurrentPosition(cell);

        List<Integer> list = stats.getMarks();

        list.add(1);
        list.add(2);
        list.add(3);

        assertNotEquals(list, stats.getMarks());

        list.add(4);
        list.add(5);

        assertNotEquals(list, stats.getMarks());


    }

    @Test
    void addMarks() {

        Cell cell = new AmmoCell();
        Stats stats = new Stats();

        stats.setCurrentPosition(cell);

        for (int i = 0; i < 10; i++) {

            stats.addMarks(i);
            
        }

        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);

        assertEquals(list,stats.getMarks());
    }

    @Test
    void setDmgTaken() {

        Cell cell = new AmmoCell();
        Stats stats = new Stats();

        stats.setCurrentPosition(cell);

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 24; i++) {

            list.add(i);
            
        }

        assertThrows(OverMaxDmgException.class,() ->{
            stats.setDmgTaken(list);
        });

        list.clear();

        list.add(1);
        list.add(3);
        list.add(0);

        try {
            stats.setDmgTaken(list);
        }catch(OverMaxDmgException e ){
            e.printStackTrace();
        }

        assertEquals(list.get(0),stats.getDmgTaken().get(0));
        assertEquals(list.get(1),stats.getDmgTaken().get(1));
        assertEquals(list.get(2),stats.getDmgTaken().get(2));



    }

    @Test
    void getDmgTaken(){

        Cell cell = new AmmoCell();
        Stats stats = new Stats();

        stats.setCurrentPosition(cell);

        List<Integer> list = stats.getDmgTaken();

        for (int i = 0; i < 12; i++) {

            list.add(i);
            
        }

        assertNotEquals(list, stats.getDmgTaken());

        for (int i = 12; i < 24; i++) {

            list.add(i);
            
        }

        assertNotEquals(list, stats.getDmgTaken());


    }

    @Test
    void addDmgTaken() {

        int maxDmg = 12;

        List<String> players = new ArrayList<>();
        players.add("Agent");

        List<PlayerColor> colors = new ArrayList<>();
        colors.add(PlayerColor.BLUE);

        Model model = new Model(players,colors,1, 8);

        Stats stats2 = Model.getPlayer(0).getStats();

        try{

            stats2.addDmgTaken((maxDmg-2)/2 -1,0);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{

            stats2.addDmgTaken((maxDmg-2)/2 +1,1);
        }catch(Exception e){
            e.printStackTrace();
        }


        for (int i = 0; i < ((maxDmg-2)/2 -1); i++) {

            assertEquals(stats2.getDmgTaken().get(i),0);
            
        }

        for (int i = ((maxDmg-2)/2 - 1); i < maxDmg-2; i++) {

            assertEquals(stats2.getDmgTaken().get(i),1);

        }


    }

    @Test
    void shouldThrowDeadPlayerException(){

        int maxDmg = 12;

        List<String> players = new ArrayList<>();
        players.add("Agent");
        players.add("Agent47");

        List<PlayerColor> colors = new ArrayList<>();
        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.BLUE);

        Model model = new Model(players,colors,1, 8);


        Stats stats = Model.getPlayer(0).getStats();

        for (int i = 0; i < (maxDmg -1); i++) {

            stats.addDmgTaken(1,0);

        }




        Stats stats1 = Model.getPlayer(1).getStats();

        stats1.addDmgTaken(11,0);


    }


    @Test
    void shouldAddMarksToDamage(){

        int maxDmg = 12;
        int maxMarks = 3;

        List<String> players = new ArrayList<>();
        players.add("Agent47");
        players.add("Agent");

        List<PlayerColor> colors = new ArrayList<>();
        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.BLUE);

        Model model = new Model(players,colors,1, 8);

        Stats stats = Model.getPlayer(0).getStats();

        for (int i = 0; i < maxMarks +2; i++) {
            stats.addMarks(1);
        }
        // max marks that can be added are maxMarks

        try {
            stats.addDmgTaken(4, 1);
        }catch (Exception e){

            e.printStackTrace();
        }

        assertEquals(3 + 4,stats.getDmgTaken().size());

    }




}