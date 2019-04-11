package it.polimi.ingsw;

import customsexceptions.DeadPlayerException;
import customsexceptions.OverMaxDmgException;
import customsexceptions.OverMaxMarkException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatsTest {

    @Test
    void setMarks() {

        Cell cell = new AmmoCell();
        Stats stats = new Stats(cell);

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
        Stats stats = new Stats(cell);

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
        Stats stats = new Stats(cell);

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
        Stats stats = new Stats(cell);

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
        Stats stats = new Stats(cell);

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

        Cell cell = new AmmoCell();
        Stats stats = new Stats(cell);

        assertThrows(DeadPlayerException.class, () -> {
            for (int i = 0; i < (maxDmg -1); i++) {

                stats.addDmgTaken(1,0);
                
            }
        });

        Stats stats1 = new Stats(cell);


        assertThrows(DeadPlayerException.class, () -> {
            for (int i = 0; i < (maxDmg + 5); i++) {

                stats1.addDmgTaken(1,0);

            }
        });

        Stats stats2 = new Stats(cell);

        try{

            stats2.addDmgTaken((maxDmg-2)/2 -1,0);
        }catch(DeadPlayerException e){
            e.printStackTrace();
        }

        try{

            stats2.addDmgTaken((maxDmg-2)/2 +1,1);
        }catch(DeadPlayerException e){
            e.printStackTrace();
        }


        for (int i = 0; i < ((maxDmg-2)/2 -1); i++) {

            assertEquals(stats2.getDmgTaken().get(i),0);
            
        }

        for (int i = ((maxDmg-2)/2 - 1); i < maxDmg-2; i++) {

            assertEquals(stats2.getDmgTaken().get(i),1);

        }

    }
}