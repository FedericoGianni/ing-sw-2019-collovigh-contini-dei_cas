package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.AmmoBag;
import it.polimi.ingsw.model.AmmoCube;
import it.polimi.ingsw.model.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AmmoBagTest {

    @Test
    void getList() {

        AmmoBag bag= new AmmoBag();

        assertTrue(bag.getList().isEmpty());

        List<AmmoCube> list = new ArrayList<>();
        list.add(new AmmoCube(Color.YELLOW));
        list.add(new AmmoCube(Color.RED));
        list.add(new AmmoCube(Color.YELLOW));

        bag.addItem(list.get(0));
        bag.addItem(list.get(1));
        bag.addItem(list.get(2));

        list.sort(Comparator.comparing(AmmoCube::getColor));

        System.out.println(list);

        assertEquals(list, bag.getList());
    }

    @Test
    void addItem() {

        AmmoBag bag= new AmmoBag();

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

        System.out.println(list);
        System.out.println(bag.getList());

        assertNotEquals(list, bag.getList());

        List<AmmoCube>list1 = bag.getList().stream()
                .filter(ammoCube ->
                        ammoCube.getColor() == Color.BLUE
                ).collect(Collectors.toList());

        System.out.println(list1);

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

        AmmoBag bag= new AmmoBag();

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

            AmmoCube ammoCube = bag.getItem(bag.getList().get(i));

            assertTrue(list.contains(ammoCube));

        }

        assertNull(bag.getItem(new AmmoCube(Color.YELLOW)));


    }

    @Test
    void hasItem() {

        AmmoBag bag= new AmmoBag();

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
}