package it.polimi.ingsw.model;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.AmmoBag;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AmmoBagTest {

    @Test
    void getList() {

        AmmoBag bag= new AmmoBag(null);

        assertTrue(bag.getList().isEmpty());

        List<AmmoCube> list = new ArrayList<>();
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.YELLOW));

        bag.addItem(list.get(0));
        bag.addItem(list.get(1));
        bag.addItem(list.get(2));

        list.sort(Comparator.comparing(AmmoCube::getColor));

        assertEquals(list, bag.getList());
    }

    @Test
    void addItem() {

        AmmoBag bag= new AmmoBag(null);

        List<AmmoCube> list = new ArrayList<>();
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.BLUE));
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.BLUE));
        list.add(new AmmoCube(Color.BLUE));
        list.add(new AmmoCube(Color.BLUE));
        list.add(new AmmoCube(Color.BLUE));



        for (AmmoCube a: list){

            bag.addItem(a);
        }

        list.sort(Comparator.comparing(AmmoCube::getColor));

        assertNotEquals(list, bag.getList());

        List<AmmoCube>list1 = bag.getList().stream()
                .filter(ammoCube ->
                        ammoCube.getColor() == Color.BLUE
                ).collect(Collectors.toList());

        assertTrue(list1.size() <= 3);

         list1 = bag.getList().stream()
                .filter(ammoCube ->
                        ammoCube.getColor() == Color.RED
                ).collect(Collectors.toList());

        assertTrue(list1.size() <= 3);

         list1 = bag.getList().stream()
                .filter(ammoCube ->
                        ammoCube.getColor() == Color.YELLOW
                ).collect(Collectors.toList());

        assertTrue(list1.size() <= 3);


    }

    @Test
    void getItem() {

        AmmoBag bag= new AmmoBag(null);

        List<AmmoCube> list = new ArrayList<>();
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.BLUE));
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.YELLOW));

        for (AmmoCube a: list){

            bag.addItem(a);
        }

        list.sort(Comparator.comparing(AmmoCube::getColor));


        for (int i = 0; i < bag.getList().size(); i++) {

            try {

                AmmoCube ammoCube = bag.getItem(bag.getList().get(i));

                assertTrue(list.contains(ammoCube));

            }catch (CardNotPossessedException e){
                e.printStackTrace();
            }



        }

        assertThrows(CardNotPossessedException.class,
                () -> {

                    bag.getItem(new AmmoCube(Color.YELLOW));

                });

    }

    @Test
    void hasItem() {

        AmmoBag bag= new AmmoBag(null);

        List<AmmoCube> list = new ArrayList<>();
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.BLUE));
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.BLUE));
        list.add(new AmmoCube(Color.BLUE));

        for (AmmoCube a: list){

            bag.addItem(a);
        }

        list.sort(Comparator.comparing(AmmoCube::getColor));

        for (int i = 0; i < bag.getList().size(); i++) {

            bag.hasItem(list.get(i));
            
        }




    }

    @Test
    void getAmount() {

        //0->red,1->blue,->yellow

        AmmoBag bag= new AmmoBag(null);

        List<AmmoCube> list = new ArrayList<>();
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.BLUE));
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.BLUE));
        list.add(new AmmoCube(Color.BLUE));

        for (AmmoCube a: list){

            bag.addItem(a);
        }

        List<AmmoCube>list1 = bag.getList().stream()
                .filter(ammoCube ->
                        ammoCube.getColor() == Color.RED
                ).collect(Collectors.toList());

        assertEquals(list1.size(),bag.getAmount()[0]);

        list1 = bag.getList().stream()
                .filter(ammoCube ->
                        ammoCube.getColor() == Color.BLUE
                ).collect(Collectors.toList());

        assertEquals(list1.size(),bag.getAmount()[1]);

        list1 = bag.getList().stream()
                .filter(ammoCube ->
                        ammoCube.getColor() == Color.YELLOW
                ).collect(Collectors.toList());

        assertEquals(list1.size(),bag.getAmount()[2]);



    }
}