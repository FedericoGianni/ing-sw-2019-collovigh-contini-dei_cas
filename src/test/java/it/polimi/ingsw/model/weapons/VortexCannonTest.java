package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.NotAbleToReloadException;
import it.polimi.ingsw.customsexceptions.UncorrectDistanceException;
import it.polimi.ingsw.customsexceptions.UncorrectEffectsException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.cachemodel.CacheModelParser;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.cachemodel.CachedFullWeapon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class VortexCannonTest {

    @Test
    void reload() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        // set the target to 1 distance from the vortex

        Cell vortex = Model.getMap().getCell(0,0);

        Model.getPlayer(1).setPlayerPos(vortex.getSouth());

        // create the weapon

        VortexCannon vortexCannon = new VortexCannon();

        Player player = Model.getPlayer(0);

        player.addWeapon(vortexCannon);

        // adds to the player the ammo he will need to reload

        player.addCube(new AmmoCube(Color.RED));

        player.addCube(new AmmoCube(Color.BLUE));

        // when bought the weapon should be loaded

        assertTrue(vortexCannon.isLoaded());

        // shoot

        List<List<Player>> targetListList = new ArrayList<>();

        targetListList.add(new ArrayList<>());

        targetListList.get(0).add(Model.getPlayer(1));


        List<Integer> effectlist = new ArrayList<>();

        effectlist.add(0);


        List<Cell> cellList = new ArrayList<>();

        cellList.add(vortex);

        try {

            vortexCannon.shoot(targetListList, effectlist, cellList);

        }catch (Exception e){

            e.printStackTrace();
        }

        assertFalse(vortexCannon.isLoaded());


        try {

            vortexCannon.reload();

        }catch (NotAbleToReloadException e){

            e.printStackTrace();

        }

        assertTrue(vortexCannon.isLoaded());

    }

    @Test
    void preShootPlayerNotSpawn() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        // set the target to 1 distance from the vortex

        Cell vortex = Model.getMap().getCell(0,0);

        Model.getPlayer(1).setPlayerPos(vortex.getSouth());

        // create the weapon

        VortexCannon vortexCannon = new VortexCannon();

        Player player = Model.getPlayer(0);

        player.addWeapon(vortexCannon);

        // pre - shoot

        List<List<Player>> targetListList = new ArrayList<>();

        targetListList.add(new ArrayList<>());

        targetListList.get(0).add(Model.getPlayer(2));


        List<Integer> effectlist = new ArrayList<>();

        effectlist.add(0);


        List<Cell> cellList = new ArrayList<>();

        cellList.add(vortex);

        assertThrows(UncorrectDistanceException.class, () -> {

            vortexCannon.preShoot(targetListList, effectlist, cellList);

        });

    }

    @Test
    void preShootPlayerTooDistant() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        // set the target to 1 distance from the vortex

        Cell vortex = Model.getMap().getCell(0,0);

        Model.getPlayer(1).setPlayerPos(Model.getMap().getCell(2,3));

        // create the weapon

        VortexCannon vortexCannon = new VortexCannon();

        Player player = Model.getPlayer(0);

        player.addWeapon(vortexCannon);

        // pre - shoot

        List<List<Player>> targetListList = new ArrayList<>();

        targetListList.add(new ArrayList<>());

        targetListList.get(0).add(Model.getPlayer(2));


        List<Integer> effectlist = new ArrayList<>();

        effectlist.add(0);


        List<Cell> cellList = new ArrayList<>();

        cellList.add(vortex);

        assertThrows(UncorrectDistanceException.class, () -> {

            vortexCannon.preShoot(targetListList, effectlist, cellList);

        });
    }

    @Test
    void preShootJustSecondEffect() {

        List<String> names = new ArrayList<>();

        names.add("Jerry");
        names.add("Frank");
        names.add("Tom");

        List<PlayerColor> colors = new ArrayList<>();

        colors.add(PlayerColor.BLUE);
        colors.add(PlayerColor.YELLOW);
        colors.add(PlayerColor.GREEN);

        Model model = new Model(names,colors,2,8);

        // set the target to 1 distance from the vortex

        Cell vortex = Model.getMap().getCell(0,0);

        Model.getPlayer(1).setPlayerPos(Model.getMap().getCell(2,3));

        // create the weapon

        VortexCannon vortexCannon = new VortexCannon();

        Player player = Model.getPlayer(0);

        player.addWeapon(vortexCannon);

        // pre - shoot

        List<List<Player>> targetListList = new ArrayList<>();

        targetListList.add(new ArrayList<>());

        targetListList.get(0).add(Model.getPlayer(2));


        List<Integer> effectlist = new ArrayList<>();

        effectlist.add(1);


        List<Cell> cellList = new ArrayList<>();

        cellList.add(vortex);

        assertThrows(UncorrectEffectsException.class, () -> {

            vortexCannon.preShoot(targetListList, effectlist, cellList);

        });
    }

    @Test
    void shootFullEffects() {

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

        // set the target to 1 distance from the vortex

        Cell vortex = Model.getMap().getCell(0,0);

        Model.getPlayer(1).setPlayerPos(vortex.getSouth());
        Model.getPlayer(2).setPlayerPos(vortex.getEast());

        // create the weapon

        VortexCannon vortexCannon = new VortexCannon();

        Player player = Model.getPlayer(0);

        player.addWeapon(vortexCannon);

        player.addCube(new AmmoCube(Color.RED));

        // pre - shoot

        List<List<Player>> targetListList = new ArrayList<>();

        // first effect

        targetListList.add(new ArrayList<>());

        targetListList.get(0).add(Model.getPlayer(1));

        List<Integer> effectList = new ArrayList<>();

        effectList.add(0);

        List<Cell> cellList = new ArrayList<>();

        cellList.add(vortex);

        // second effect

        targetListList.add(new ArrayList<>());

        targetListList.get(1).add(Model.getPlayer(2));

        effectList.add(1);


        try {

            vortexCannon.shoot(targetListList, effectList, cellList);

        }catch (Exception e){

            e.printStackTrace();
        }


        assertEquals(2,Model.getPlayer(1).getStats().getDmgTaken().size());

        assertEquals(1,Model.getPlayer(2).getStats().getDmgTaken().size());
    }

    @Test
    void getReloadCost() {

        VortexCannon vortexCannon = new VortexCannon();

        List<CachedFullWeapon> weaponList = CacheModelParser.readCachedFullWeaponsFromList();

        CachedFullWeapon vortex = weaponList
                .stream()
                .filter( x -> x.getName().equalsIgnoreCase(vortexCannon.getName() ))
                .collect(Collectors.toList())
                .get(0);

        assertEquals( vortexCannon.getReloadCost().stream().map(AmmoCube::getColor).collect(Collectors.toList()), vortex.getFirstEffectCost());

        assertEquals( vortexCannon.getCost().stream().map(AmmoCube::getColor).collect(Collectors.toList()) , vortex.getFirstEffectCost().subList(1, vortex.getFirstEffectCost().size()));
    }
}