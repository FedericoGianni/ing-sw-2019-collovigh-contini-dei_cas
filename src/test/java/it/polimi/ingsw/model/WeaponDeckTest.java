package it.polimi.ingsw.model;

import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponDeck;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeaponDeckTest {

    @Test
    void getRandomCard() {

        WeaponDeck deck = WeaponDeck.populateDeck();

        List<Weapon> list = new ArrayList<>();

        int size = deck.getSize();

        for (int i = 0; i < size; i++) {
            list.add(deck.getRandomCard());
        }

        assertEquals(size,list.size());
    }

    @Test
    void printWeaponDeck(){
        WeaponDeck deck = WeaponDeck.populateDeck();
        for(int i=0;i<deck.getSize();i++)
        {
           //deck.getRandomCard().print();
           //System.out.println("---------------------");
        }
    }
}