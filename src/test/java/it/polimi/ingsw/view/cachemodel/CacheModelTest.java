package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.view.exceptions.WeaponNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CacheModelTest {

    @Test
    void getWeaponInfo() {

        CacheModel cacheModel = new CacheModel(null);

        try{

            CachedFullWeapon weapon = cacheModel.getWeaponInfo("LOCK RIFLE");

            assertNotNull(weapon);

            //System.out.println(weapon.getName());

        }catch (WeaponNotFoundException e){

            e.printStackTrace();
        }

    }
}