package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeaponDeckTest {

    @Test
    void getRandomCard() {

        WeaponDeck deck = WeaponDeck.populatedDeck();

        List<Weapon> list = new ArrayList<>();

        int size = deck.getSize();

        for (int i = 0; i < size; i++) {
            list.add(deck.getRandomCard());
        }

        assertEquals(size,list.size());
    }
}