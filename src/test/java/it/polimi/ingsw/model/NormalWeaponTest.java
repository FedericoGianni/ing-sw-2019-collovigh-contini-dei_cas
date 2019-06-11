package it.polimi.ingsw.model;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.weapons.*;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


class NormalWeaponTest {

    @Test
    void weaponsCreator() {

        //-----------------creates microEffects ecc
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();
        ArrayList <Weapon> weapons=new ArrayList<>();
        weapons.addAll(NormalWeapon.weaponsCreator());
        //-------------------
        assertNotNull(weapons);

    }

    @Test
    void shoot1() throws FrenzyActivatedException {//one shot, one effect, one target, free effect---the most basic possible
        //-----------------creates microEffects ecc
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();
        ArrayList <NormalWeapon> weapons=new ArrayList<>();
        weapons.addAll(NormalWeapon.weaponsCreator());

        //--------------creates Players ecc
        ArrayList<String> playerNames=new ArrayList<>();
        playerNames.add("shooter");
        playerNames.add("target");
        ArrayList<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.BLUE);
        //generate the map (type 2)
        Model m=new Model(playerNames,pc,2,8);


        //generate a player with a name and its starting position
        //Player p1 = new Player("Shooter",map.getCell(1,3));
        Player shooter = Model.getPlayer(0);
        shooter.setPlayerPos(Model.getMap().getCell(0,3));

        //Player p2 = new Player("Visible",map.getCell(0,3));
        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,3));


        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        ArrayList targets=new ArrayList();
        targets.add(target1);
        ArrayList<ArrayList<Player>>targetsLists=new ArrayList<>();
        targetsLists.add(targets);
        shooter.addWeapon(weapons.get(0));//not how it works but easy

        try{
            ArrayList <Integer>mEf=new ArrayList<>();
            mEf.add(0);
            shooter.getWeapons().get(0).shoot(targetsLists,mEf,null);
            //System.out.println(target1.getStats().getDmgTaken());
        }
        catch(WeaponNotLoadedException e){ e.printStackTrace();} catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shoot2() throws FrenzyActivatedException{//now two effects: 2 dmg and 1 mark to target 1 and 1 mark to target2
        // 2 damages and 1 mark
        //-----------------creates microEffects ecc
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();
        ArrayList <NormalWeapon> weapons=new ArrayList<>();
        weapons.addAll(NormalWeapon.weaponsCreator());

        ArrayList<String> playerNames=new ArrayList<>();
        playerNames.add("shooter");
        playerNames.add("target1");
        playerNames.add("target2");
        ArrayList<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.GREEN);
        //generate the map (type 2)
        Model m=new Model(playerNames,pc,2,8);


        //generate a player with a name and its starting position
        Player shooter = Model.getPlayer(0);
        shooter.setPlayerPos(Model.getMap().getCell(0,3));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,3));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,3));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.addWeapon(weapons.get(0));//not how it works but easy
        ArrayList targets0=new ArrayList();
        ArrayList targets1=new ArrayList();
        ArrayList<ArrayList<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);
        targets1.add(target2);
        targetLists.add(targets0);
        targetLists.add(targets1);
        Model.getMap().setUnvisited();

        try{
            ArrayList <Integer>mEf=new ArrayList<>();
            mEf.add(0);
            mEf.add(1);

            shooter.getWeapons().get(0).shoot(targetLists,mEf,null);
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shoot3() throws FrenzyActivatedException{//same as the shoot2 test but launches the notEnoughAmmoException
        //-----------------creates microEffects ecc
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();
        ArrayList <NormalWeapon> weapons=new ArrayList<>();
        weapons.addAll(NormalWeapon.weaponsCreator());

        List<String>  names=new ArrayList<>();
        names.add("shooter");
        names.add("target");
        List<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(names,pc,2,8);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target1=Model.getGame().getPlayers().get(1);
        shooter.setPlayerPos(Model.getMap().getCell(0,3));
        target1.setPlayerPos(Model.getMap().getCell(1,3));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));//one only for evitating null Pointer
        shooter.addWeapon(weapons.get(0));//not how it works but easy
        ArrayList targets=new ArrayList();
        ArrayList<ArrayList <Player>>targetsLists=new ArrayList<>();
        targets.add(target1);
        targetsLists.add(targets);
        try{
            ArrayList <Integer> mEf=new ArrayList<>();
            mEf.add(0);
            mEf.add(1);//costs 1 red AmmoCube

            shooter.getWeapons().get(0).shoot(targetsLists,mEf,null);

        }
        catch(WeaponNotLoadedException e){} catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shoot4() throws FrenzyActivatedException{//Shoot, 2 effects and mover effect that moves the target in the shooter cell
        //-----------------creates microEffects ecc
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();
        ArrayList <NormalWeapon> weapons=new ArrayList<>();
        weapons.addAll(NormalWeapon.weaponsCreator());
        //login stuff
        List<String>  names=new ArrayList<>();
        names.add("shooter");
        names.add("target");

        List<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);

        Model m=new Model(names,pc,2,8);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target1=Model.getGame().getPlayers().get(1);

        shooter.setPlayerPos(Model.getMap().getCell(0,3));
        target1.setPlayerPos(Model.getMap().getCell(1,3));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));

        shooter.addWeapon(weapons.get(5));//not actually how it works but easy
        //adding targets to the targets lists
        ArrayList targets1=new ArrayList();
        ArrayList targets2=new ArrayList();
        ArrayList<ArrayList<Player>>targetsLists=new ArrayList<>();
        targets1.add(target1);
        targets2.add(target1);
        targetsLists.add(targets1);
        targetsLists.add(targets2);
        ArrayList<Cell> cells=new ArrayList<>();
        //first traget don't move
        cells.add(null);
        cells.add(shooter.getCurrentPosition());
        try{
            ArrayList <Integer>mEf=new ArrayList<>();
            mEf.add(0);//tractor beam-- don't move(because you can see it) then 1 dmg
            mEf.add(1);//costs 1 red and 1 yellow AmmoCube--move it to my cell then 3dmg
            weapons.get(5).enableMoveBefore();

            shooter.getWeapons().get(0).shoot(targetsLists,mEf,cells);

            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());

            assertEquals(shooter.getCurrentPosition(),target1.getCurrentPosition());

        }
        catch(WeaponNotLoadedException e){} catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        }
    }

}

