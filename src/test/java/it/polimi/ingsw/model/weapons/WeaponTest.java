package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.customsexceptions.NotAbleToReloadException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.AmmoBag;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.cachemodel.CacheModelParser;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.cachemodel.CachedFullWeapon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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


    @Test
    void reload() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");
        names.add("Stan");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);
        colors.add(PlayerColor.GREY);

        Model model = new Model(names,colors,2,8);

        Player player = Model.getPlayer(0);

        Weapon weapon = Model.getGame().drawWeapon();

        player.addWeapon(weapon);

        weapon.setLoaded(false);

        assertEquals(player.getCurrentWeapons().getList(), Arrays.asList(weapon));

        // adds the cubes

        player.addCube(new AmmoCube(Color.RED));
        player.addCube(new AmmoCube(Color.RED));
        player.addCube(new AmmoCube(Color.RED));

        player.addCube(new AmmoCube(Color.BLUE));
        player.addCube(new AmmoCube(Color.BLUE));
        player.addCube(new AmmoCube(Color.BLUE));

        player.addCube(new AmmoCube(Color.YELLOW));
        player.addCube(new AmmoCube(Color.YELLOW));
        player.addCube(new AmmoCube(Color.YELLOW));

        List<AmmoCube> initialCubes = new ArrayList<>();

        initialCubes.addAll(player.getAmmoBag().getList());

        // reload

        try {

            weapon.reload();

        }catch (NotAbleToReloadException e){

            e.printStackTrace();
        }

        assertTrue(weapon.isLoaded());

        //System.out.println(weapon.getReloadCost());

        List<AmmoCube> possessed = player.getAmmoBag().getList();

        //System.out.println(possessed);

        possessed.addAll(weapon.getReloadCost());

        possessed.sort(Comparator.comparing(AmmoCube::getColor));

        //System.out.println(possessed);

        assertEquals(initialCubes.stream().map(AmmoCube::getColor).collect(Collectors.toList()),possessed.stream().map(AmmoCube::getColor).collect(Collectors.toList()));

        assertEquals(player.getCurrentWeapons().getList(), Arrays.asList(weapon));
    }
}