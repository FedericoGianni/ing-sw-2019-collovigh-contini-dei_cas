package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.CellNonExistentException;
import it.polimi.ingsw.customsexceptions.PlayerInSameCellException;
import it.polimi.ingsw.customsexceptions.UncorrectEffectsException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FurnaceTest {

    @Test
    void preShootSecondEffect() {

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

        Cell target = Model.getMap().getCell(0,0);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(target);

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(target);

        // create the weapon

        Furnace weapon = new Furnace();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(Model.getMap().getCell(2,3));

        player.addWeapon(weapon);

        // check exception CellNonExistent

        assertThrows(CellNonExistentException.class, () -> {

            weapon.preShoot(new ArrayList<>(), Arrays.asList(1), new ArrayList<>());
        });

        assertThrows(CellNonExistentException.class, () -> {

            List<Cell> cells = new ArrayList<>();
            cells.add(null);

            weapon.preShoot(new ArrayList<>(), Arrays.asList(1), cells);
        });

        // check double effect specified

        assertThrows(UncorrectEffectsException.class, () -> {

            weapon.preShoot(new ArrayList<>(), Arrays.asList(0,1), Arrays.asList(target));
        });

        // player in same cell

        player.setPlayerPos(target);

        assertThrows(PlayerInSameCellException.class, () -> {

            weapon.preShoot(Arrays.asList(Arrays.asList(target_1,target_2)), Arrays.asList(1), Arrays.asList(target));
        });

    }

    @Test
    void preShootBaseEffect() {

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

        Cell target = Model.getMap().getCell(0,0);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(target);

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(target);

        // create the weapon

        Furnace weapon = new Furnace();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(Model.getMap().getCell(2,3));

        player.addWeapon(weapon);

        // check exception CellNonExistent

        assertThrows(CellNonExistentException.class, () -> {

            weapon.preShoot(new ArrayList<>(), Arrays.asList(0), new ArrayList<>());
        });

        assertThrows(CellNonExistentException.class, () -> {

            List<Cell> cells = new ArrayList<>();
            cells.add(null);

            weapon.preShoot(new ArrayList<>(), Arrays.asList(0), cells);
        });

        // player in same cell

        player.setPlayerPos(target);

        assertThrows(PlayerInSameCellException.class, () -> {

            weapon.preShoot(Arrays.asList(Arrays.asList(target_1,target_2)), Arrays.asList(0), Arrays.asList(target));
        });
    }

    @Test
    void shootBaseEffect() {

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

        Cell target = Model.getMap().getCell(0,0);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(target);

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(target.getEast());

        Player target_3 = Model.getPlayer(3);
        target_3.setPlayerPos(target.getSouth());

        // create the weapon

        Furnace weapon = new Furnace();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(Model.getMap().getCell(2,3));

        player.addWeapon(weapon);

        try {

            weapon.shoot(new ArrayList<>(),Arrays.asList(0), Arrays.asList(target) );

        }catch (Exception e){

            e.printStackTrace();
        }

        // 2 dmg for player in same room
        //TODO fix dmg

        assertEquals(1,target_1.getDmg().size());
        //assertEquals(0,target_2.getDmg().size());

        // 0 dmg for player in other room

        assertEquals(0,target_3.getDmg().size());
    }

    @Test
    void shootSecondEffect() {

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

        Cell target = Model.getMap().getCell(0,0);

        Player target_1 =Model.getPlayer(1);
        target_1.setPlayerPos(target);

        Player target_2 = Model.getPlayer(2);
        target_2.setPlayerPos(target.getEast());

        Player target_3 = Model.getPlayer(3);
        target_3.setPlayerPos(target.getSouth());

        // create the weapon

        Furnace weapon = new Furnace();

        Player player = Model.getPlayer(0);

        player.setPlayerPos(Model.getMap().getCell(2,3));

        player.addWeapon(weapon);

        try {

            weapon.shoot(new ArrayList<>(),Arrays.asList(1), Arrays.asList(target) );

        }catch (Exception e){

            e.printStackTrace();
        }

        // 2 dmg for player in same room

        assertEquals(1,target_1.getDmg().size());
        assertEquals(1,target_1.getMarks().size());

        // 0 dmg for player in other cell

        assertEquals(0,target_2.getDmg().size());

        // 0 dmg for player in other room

        assertEquals(0,target_3.getDmg().size());
    }
}