package it.polimi.ingsw.model.player;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void pay() {

        Player player = new Player("Alex",0,PlayerColor.BLUE);

        player.addCube(new AmmoCube(Color.RED));
        player.addCube(new AmmoCube(Color.RED));

        player.addCube(new AmmoCube(Color.YELLOW));

        player.addCube(new AmmoCube(Color.BLUE));

        List<AmmoCube> cost = new ArrayList<>();

        cost.add(new AmmoCube(Color.RED));
        cost.add(new AmmoCube(Color.RED));

        cost.add(new AmmoCube(Color.YELLOW));

        cost.add(new AmmoCube(Color.BLUE));

        assertTrue(player.canPay(cost));

        try {

            player.pay(cost);

        }catch (CardNotPossessedException e){

            e.printStackTrace();
        }

        assertEquals(0, player.getAmmoBag().getList().size());
    }

    @Test
    void payShouldThrowException() {

        Player player = new Player("Alex",0,PlayerColor.BLUE);

        player.addCube(new AmmoCube(Color.RED));
        player.addCube(new AmmoCube(Color.RED));

        player.addCube(new AmmoCube(Color.YELLOW));

        player.addCube(new AmmoCube(Color.BLUE));

        List<AmmoCube> cost = new ArrayList<>();

        cost.add(new AmmoCube(Color.RED));
        cost.add(new AmmoCube(Color.RED));

        cost.add(new AmmoCube(Color.YELLOW));

        cost.add(new AmmoCube(Color.BLUE));

        cost.add(new AmmoCube(Color.BLUE));

        assertFalse(player.canPay(cost));

        assertThrows(CardNotPossessedException.class, () -> {

            player.pay(cost);

        });

        assertEquals(4, player.getAmmoBag().getList().size());


    }
}