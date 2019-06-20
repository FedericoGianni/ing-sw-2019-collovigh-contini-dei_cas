package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.NotAbleToReloadException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    void preShoot() {

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

        try {

            vortexCannon.preShoot(targetListList, effectlist, cellList);

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Test
    void shoot() {
    }
}