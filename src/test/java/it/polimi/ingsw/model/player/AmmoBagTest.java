package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmmoBagTest {

    @Test
    void leftMaxAmmo() {

        AmmoBag ammobag = new AmmoBag(null);

        assertEquals(2,ammobag.leftMaxAmmo(Color.RED));
        assertEquals(2,ammobag.leftMaxAmmo(Color.YELLOW));
        assertEquals(2,ammobag.leftMaxAmmo(Color.BLUE));

        ammobag.addItem(new AmmoCube(Color.RED));

        assertEquals(1,ammobag.leftMaxAmmo(Color.RED));
        assertEquals(2,ammobag.leftMaxAmmo(Color.YELLOW));
        assertEquals(2,ammobag.leftMaxAmmo(Color.BLUE));

        ammobag.addItem(new AmmoCube(Color.RED));

        assertEquals(0,ammobag.leftMaxAmmo(Color.RED));
        assertEquals(2,ammobag.leftMaxAmmo(Color.YELLOW));
        assertEquals(2,ammobag.leftMaxAmmo(Color.BLUE));

        ammobag.addItem(new AmmoCube(Color.RED));

        assertEquals(0,ammobag.leftMaxAmmo(Color.RED));
        assertEquals(2,ammobag.leftMaxAmmo(Color.YELLOW));
        assertEquals(2,ammobag.leftMaxAmmo(Color.BLUE));
    }
}