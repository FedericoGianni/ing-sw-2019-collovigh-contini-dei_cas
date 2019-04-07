package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoCardTest {

    @Test
    void generateRandCard() {

        for (int i = 0; i < 10000; i++) {

            AmmoCard card = AmmoCard.generateRandCard();

            if (card.getPowerUp()){

                assertNotNull(card.getAmmoCube1());
                assertNotNull(card.getAmmoCube2());
                assertNull(card.getAmmoCube3());
            }

            else {

                assertNotNull(card.getAmmoCube1());
                assertNotNull(card.getAmmoCube2());
                assertNotNull(card.getAmmoCube3());
            }
        }
    }

    @Test
    void getPowerUp() {

        
    }

    @Test
    void getAmmoCube1() {
    }

    @Test
    void getAmmoCube2() {
    }

    @Test
    void getAmmoCube3() {
    }
}