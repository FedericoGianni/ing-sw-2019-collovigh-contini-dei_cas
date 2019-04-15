package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.AmmoCard;
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

        for (int w = 0; w < 100; w++) {


            int i = 0;
            int CARD_NUMBER = 36;
            int cardTested = CARD_NUMBER * 10;
            int tolerance = 30;


            for (int k = 0; k < (cardTested); k++) {


                    Boolean temp = AmmoCard.generateRandCard().getPowerUp();


                if (temp) i++;


            }

            System.out.println(i);
            System.out.println((cardTested/2) - i);

            assertTrue(Math.abs((cardTested/2) - i) <= tolerance);

        }

        
    }


}