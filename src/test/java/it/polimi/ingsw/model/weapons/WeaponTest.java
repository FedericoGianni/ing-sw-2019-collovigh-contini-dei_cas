package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.AmmoBag;
import it.polimi.ingsw.utils.CacheModelParser;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.cachemodel.CachedFullWeapon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeaponTest {

    @Test
    void canPay() {

        List<AmmoCube> cost = new ArrayList<>();

        cost.add(new AmmoCube(Color.BLUE));
        cost.add(new AmmoCube(Color.RED));
        cost.add(new AmmoCube(Color.RED));

        AmmoBag bag = new AmmoBag(null);
        bag.addItem(new AmmoCube(Color.BLUE));
        bag.addItem(new AmmoCube(Color.BLUE));
        bag.addItem(new AmmoCube(Color.BLUE));
        bag.addItem(new AmmoCube(Color.RED));
        bag.addItem(new AmmoCube(Color.RED));
        bag.addItem(new AmmoCube(Color.YELLOW));

        Weapon weapon = new NormalWeapon("Boh",null,null);

        assertTrue(weapon.canPay(cost,bag));

    }

    @Test
    void checkCacheModelFullWeapon(){

        List<Weapon> weaponList = Parser.getFullWeaponList();

        List<CachedFullWeapon> cachedFullWeaponList = CacheModelParser
                .readCachedFullWeaponsFromList();

        List<String> cachedFullWeaponNames = cachedFullWeaponList
                .stream()
                .map(CachedFullWeapon::getName)
                .collect(Collectors.toList());

        for (Weapon weapon : weaponList){

            System.out.println(weapon.getName());

            assertTrue(cachedFullWeaponNames.contains(weapon.getName()));

            CachedFullWeapon cachedFullWeapon = cachedFullWeaponList
                    .stream()
                    .filter( x -> x.getName().equals(weapon.getName()))
                    .collect(Collectors.toList())
                    .get(0);

            assertEquals(cachedFullWeapon.getFirstEffectCost(),weapon.getReloadCost().stream().map(AmmoCube::getColor).collect(Collectors.toList()));
        }
    }
}