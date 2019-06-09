package it.polimi.ingsw.utils;

import it.polimi.ingsw.view.cachemodel.CachedFullWeapon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CacheModelParserTest {

    @Test
    void readCachedFullWeaponsFromList() {

        List<CachedFullWeapon> weaponList = CacheModelParser.readCachedFullWeaponsFromList();

        assertNotNull(weaponList);

        for (CachedFullWeapon weapon : weaponList) { //TODO delete

            System.out.println(weapon.getName() );
        }
    }
}