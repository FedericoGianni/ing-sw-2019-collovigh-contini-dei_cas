package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.AmmoBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    @Test
    void canPay() {

        List<AmmoCube> cost = new ArrayList<>();

        cost.add(new AmmoCube(Color.BLUE));
        cost.add(new AmmoCube(Color.RED));
        cost.add(new AmmoCube(Color.RED));

        AmmoBag bag = new AmmoBag();
        bag.addItem(new AmmoCube(Color.BLUE));
        bag.addItem(new AmmoCube(Color.BLUE));
        bag.addItem(new AmmoCube(Color.BLUE));
        bag.addItem(new AmmoCube(Color.RED));
        bag.addItem(new AmmoCube(Color.RED));
        bag.addItem(new AmmoCube(Color.YELLOW));

        Weapon weapon = new NormalWeapon("Boh",null,null);

        assertTrue(weapon.canPay(cost,bag));

    }
}