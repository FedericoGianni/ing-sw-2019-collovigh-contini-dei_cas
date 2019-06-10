package it.polimi.ingsw.model;

import it.polimi.ingsw.model.powerup.*;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpDeckTest {

    @Test
    void populatedDeck() {

        PowerUpDeck deck = PowerUpDeck.populatedDeck();

        for (int i = 0; i < 24; i++) {

            assertNotNull(deck.getRandomCard());

        }


    }

    @Test
    void reinsert() {

        PowerUpDeck deck = new PowerUpDeck();

        List<PowerUp> list1 = new ArrayList<>();
        List<PowerUp> list2 = new ArrayList<>();

        list1.add(new Newton(Color.BLUE));
        list1.add(new TagbackGrenade(Color.RED));
        list1.add(new TargetingScope(Color.YELLOW));
        list1.add(new Teleporter(Color.BLUE));

        for(PowerUp powerUp : list1){

            deck.reinsert(powerUp);

        }

        for (int i = 0; i < 4; i++) {

            list2.add(deck.getRandomCard());
            
        }

        assertTrue(list2.containsAll(list1));

    }

    @Test
    void getRandomCard() {

        // to be fair this test can possibly fail, because since the cards are drawn casually there is a chance that two set of card picked casually result to be the same set,
        // tho it is quite rare ( 1/24!)

        PowerUpDeck deck = PowerUpDeck.populatedDeck();

        List<PowerUp> list1 = new ArrayList<>();
        List<PowerUp> list2 = new ArrayList<>();


        for (int i = 0; i < 24; i++) {

            list2.add(deck.getRandomCard());

        }

        deck = PowerUpDeck.populatedDeck();

        for (int i = 0; i < 24; i++) {

            list1.add(deck.getRandomCard());

        }


        assertNotEquals(list1,list2);


    }

    @Test
    void cloneCreatorShouldBeEqual(){

        PowerUpDeck deck = PowerUpDeck.populatedDeck();

        int size = deck.getSize();

        List<PowerUp> list1 = new ArrayList<>();
        List<PowerUp> list2 = new ArrayList<>();


        for (int i = 0; i < 10; i++) {

            list1.add(deck.getRandomCard());

        }

        PowerUpDeck copy = new PowerUpDeck(deck);

        list1 = new ArrayList<>();

        for (int i = 0; i < (size - 10); i++) {

            list1.add(deck.getRandomCard());
            
        }

        for (int i = 0; i < (size - 10); i++) {

            list2.add(copy.getRandomCard());

        }

        Collections.sort(list1, Comparator.comparing(PowerUp::toString));

        Collections.sort(list2, Comparator.comparing(PowerUp::toString));

        assertEquals(list1,list2);



    }
}