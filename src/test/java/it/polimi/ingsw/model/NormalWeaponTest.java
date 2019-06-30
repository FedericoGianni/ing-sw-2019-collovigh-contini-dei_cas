package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.weapons.*;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
    void shoot1() throws FrenzyActivatedException, PlayerAlreadyDeadException, DifferentPlayerNeededException, NotEnoughAmmoException, CardNotPossessedException {//one shot, one effect, one target, free effect---the most basic possible
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
        List targets=new ArrayList();
        targets.add(target1);
        List<List<Player>>targetsLists=new ArrayList<>();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void shoot2() throws FrenzyActivatedException, PlayerAlreadyDeadException, DifferentPlayerNeededException, NotEnoughAmmoException, CardNotPossessedException {//now two effects: 2 dmg and 1 mark to target 1 and 1 mark to target2
        // 2 damages and 1 mark to target 1
        // 1 mark to target2
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
        shooter.setPlayerPos(Model.getMap().getCell(1,1));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,1));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(0,0));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.addWeapon(weapons.get(0));//not how it works but easy
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
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
            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());

             */
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (Exception e) {
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
        shooter.setPlayerPos(Model.getMap().getCell(1,1));
        target1.setPlayerPos(Model.getMap().getCell(0,0));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));//one only for evitating null Pointer
        shooter.addWeapon(weapons.get(0));//not how it works but easy
        //System.out.println("weapon: "+ weapons.get(0).getName());
        List <Player> targets = new ArrayList();
        List <List <Player>>targetsLists=new ArrayList<>();
        targets.add(target1);


        targetsLists.add(targets);
        try{
            ArrayList <Integer> mEf=new ArrayList<>();
            mEf.add(0);
            //mEf.add(1);//costs 1 red AmmoCube

            shooter.getWeapons().get(0).shoot(targetsLists,mEf,null);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        assert(target1.getStats().getDmgTaken().size() == 0);
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
        //System.out.println("weap: " + weapons.get(5).getName());
        //adding targets to the targets lists
        List<Player> targets1=new ArrayList<>();
        List<Player> targets2=new ArrayList<>();
        List<List<Player>>targetsLists=new ArrayList<>();
        targets1.add(target1);
        targets2.add(target1);
        targetsLists.add(targets1);
        targetsLists.add(targets2);
        List<Cell> cells=new ArrayList<>();
        //first traget don't move
        cells.add(null);
        cells.add(shooter.getCurrentPosition());
        try{
            ArrayList <Integer>mEf=new ArrayList<>();
            mEf.add(0);//tractor beam-- don't move(because you can see it) then 1 dmg
            mEf.add(1);//costs 1 red and 1 yellow AmmoCube--move it to my cell then 3dmg

            shooter.getWeapons().get(0).shoot(targetsLists,mEf,cells);

            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());

             */

            assertEquals(shooter.getCurrentPosition(),target1.getCurrentPosition());

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void Shoot5()throws FrenzyActivatedException
    {
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
        shooter.setPlayerPos(Model.getMap().getCell(1,1));

        //Player p2 = new Player("Visible",map.getCell(0,3));
        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(0,0));


        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        List targets=new ArrayList();
        targets.add(target1);
        //System.out.println(target1.getCurrentPosition());
        List<List<Player>>targetsLists=new ArrayList<>();
        targetsLists.add(targets);
        shooter.addWeapon(weapons.get(0));//not how it works but easy


        try{
            ArrayList <Integer>mEf=new ArrayList<>();
            mEf.add(0);
            shooter.getWeapons().get(0).shoot(targetsLists,mEf,null);

            //assert(target1.getStats().getDmgTaken().size() == 0);
        }
        catch(Exception e){
            e.printStackTrace();
        }


        assert(target1.getStats().getDmgTaken().size() == 0);
    }

    @Test
    void lockRifleTest() throws FrenzyActivatedException, DifferentPlayerNeededException, PlayerAlreadyDeadException, NotEnoughAmmoException, CardNotPossessedException {//now two effects: 2 dmg and 1 mark to target 1 and 1 mark to target2
        // 2 damages and 1 mark to target 1
        // 1 mark to target2
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
        shooter.setPlayerPos(Model.getMap().getCell(1,1));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,1));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,1));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.addWeapon(weapons.get(0));//not how it works but easy
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);
        targets1.add(target2);
        targetLists.add(targets0);
        targetLists.add(targets1);
        Model.getMap().setUnvisited();

        try {
            ArrayList<Integer> mEf = new ArrayList<>();
            mEf.add(0);
            mEf.add(1);

            shooter.getWeapons().get(0).shoot(targetLists, mEf, null);


        } catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PrecedentPlayerNeededException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException e) {
            e.printStackTrace();
        } catch (WeaponNotLoadedException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (CellNonExistentException e) {
            e.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch(DifferentPlayerNeededException e)
        {
            e.printStackTrace();
        } catch (PlayerAlreadyDeadException e){
            e.printStackTrace();
        }
        /*
        System.out.println(target1.getPlayerName());
        System.out.println(target1.getStats().getDmgTaken());
        System.out.println(target1.getStats().getMarks());

        System.out.println(target2.getPlayerName());
        System.out.println(target2.getStats().getDmgTaken());
        System.out.println(target2.getStats().getMarks());

         */

        assert(target1.getStats().getDmgTaken().size() == 2);
        assert(target1.getStats().getMarks().size() == 1);
        assert(target2.getStats().getMarks().size() == 1);

    }

    @Test
    void machineGunTest() throws FrenzyActivatedException, PlayerAlreadyDeadException, DifferentPlayerNeededException, NotEnoughAmmoException, CardNotPossessedException {//now two effects: 2 dmg and 1 mark to target 1 and 1 mark to target2

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
        playerNames.add("target3");
        ArrayList<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.GREEN);
        pc.add(PlayerColor.YELLOW);
        //generate the map (type 2)
        Model m=new Model(playerNames,pc,2,8);


        //generate a player with a name and its starting position
        Player shooter = Model.getPlayer(0);
        shooter.setPlayerPos(Model.getMap().getCell(0,0));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,1));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,1));

        //unseeable for shooter
        Player target3 = Model.getPlayer(3);
        target3.setPlayerPos(Model.getMap().getCell(0,0));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(weapons.get(1));//not how it works but easy
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List targets2=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);
        targets0.add(target2);
        targets1.add(target2);
        targets2.add(target3);
        targets2.add(target1);
        targetLists.add(targets0);
        targetLists.add(targets1);
        targetLists.add(targets2);
        Model.getMap().setUnvisited();

        try{
            ArrayList <Integer>mEf=new ArrayList<>();
            mEf.add(0);
            mEf.add(1);
            mEf.add(2);

            shooter.getWeapons().get(0).shoot(targetLists,mEf,null);


        } catch(Exception e){
            e.printStackTrace();
        }

        /*
        System.out.println(target1.getPlayerName());
        System.out.println(target1.getStats().getDmgTaken());
        System.out.println(target1.getStats().getMarks());

        System.out.println(target2.getPlayerName());
        System.out.println(target2.getStats().getDmgTaken());
        System.out.println(target2.getStats().getMarks());

        System.out.println(target3.getPlayerName());
        System.out.println(target3.getStats().getDmgTaken());
        System.out.println(target3.getStats().getMarks());*/

        assert(target1.getStats().getDmgTaken().size() == 2);
        assert(target2.getStats().getDmgTaken().size() == 2);
        assert(target3.getStats().getDmgTaken().size() == 1);
    }

    @Test
    void whisperTest() throws FrenzyActivatedException, PlayerAlreadyDeadException, DifferentPlayerNeededException, NotEnoughAmmoException, CardNotPossessedException {//now two effects: 2 dmg and 1 mark to target 1 and 1 mark to target2

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
        playerNames.add("target3");
        ArrayList<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.GREEN);
        pc.add(PlayerColor.YELLOW);
        //generate the map (type 2)
        Model m=new Model(playerNames,pc,2,8);


        //generate a player with a name and its starting position
        Player shooter = Model.getPlayer(0);
        shooter.setPlayerPos(Model.getMap().getCell(2,3));

        Player target1 = Model.getPlayer(1); //1,2
        target1.setPlayerPos(Model.getMap().getCell(1,2));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,1));

        //unseeable for shooter
        Player target3 = Model.getPlayer(3);
        target2.setPlayerPos(Model.getMap().getCell(1,1));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(weapons.get(3));//not how it works but easy
        //System.out.println("weapon: " + weapons.get(3).getName());
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List targets2=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);
        //targets0.add(target2);
        targets1.add(target1);
        targets2.add(target3);
        targetLists.add(targets0);
        //targetLists.add(targets1);
        //targetLists.add(targets2);
        Model.getMap().setUnvisited();

        try{
            ArrayList <Integer>mEf=new ArrayList<>();
            mEf.add(0);
            //mEf.add(1);
            //mEf.add(2);

            /*
            shooter.getWeapons().get(0).shoot(targetLists,mEf,null);
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());

             */

        }catch(Exception e){
            e.printStackTrace();
        }

        //TODO fix dmg
        //assert(target1.getStats().getDmgTaken().size() == 3);
        //assert(target1.getStats().getMarks().size() == 1);
        //assert(target2.getStats().getDmgTaken().size() == 2);
        //assert(target3.getStats().getDmgTaken().size() == 1);
    }


    @Test
    public void electroshyteTest(){
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
        shooter.setPlayerPos(Model.getMap().getCell(1,1));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,1));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,1));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.addWeapon(weapons.get(4));//Electrohyte = falce protonica
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);
        targets1.add(target2);
        targetLists.add(targets0);
        targetLists.add(targets1);
        Model.getMap().setUnvisited();
        //System.out.println("Weapon " + weapons.get(4).getName());

        try {
            //NOTE This weapon has EXCLUSIVE effect types
            ArrayList<Integer> mEf = new ArrayList<>();
            mEf.add(0);
           // mEf.add(1);//only one usable effect per shoot
            List<Cell> cells = new ArrayList<>();

            shooter.getWeapons().get(0).shoot(targetLists, mEf, cells);
            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());*/
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (CardNotPossessedException e) {
            e.printStackTrace();
        } catch (CellNonExistentException e) {
            e.printStackTrace();
        } catch (PrecedentPlayerNeededException e) {
            e.printStackTrace();
        } catch (NotEnoughAmmoException e) {
            e.printStackTrace();
        } catch (DifferentPlayerNeededException e) {
            e.printStackTrace();
        } catch (PlayerAlreadyDeadException e){
            e.printStackTrace();
        }


        //System.out.println("target1 danni" + target1.getStats().getDmgTaken().size());
        //System.out.println("target2 danni" + target2.getStats().getDmgTaken().size());
        assert(target1.getStats().getDmgTaken().size() ==1);
        assert(target2.getStats().getDmgTaken().size() ==1);
    }

    @Test
    void tractorBeamTest() throws FrenzyActivatedException{//Shoot, 2 effects and mover effect that moves the target in the shooter cell
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

        shooter.setPlayerPos(Model.getMap().getCell(0,1));
        target1.setPlayerPos(Model.getMap().getCell(0,3));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));

        shooter.addWeapon(weapons.get(5));//not actually how it works but easy
        //System.out.println("weap: " + weapons.get(5).getName());
        //adding targets to the targets lists
        List<Player> targets1=new ArrayList<>();
        List<Player> targets2=new ArrayList<>();
        List<List<Player>>targetsLists=new ArrayList<>();
        targets1.add(target1);
        //targets2.add(target1);
        targetsLists.add(targets1);
        targetsLists.add(targets2);
        List<Cell> cells=new ArrayList<>();
        //first traget don't move
        cells.add(null);
        cells.add(shooter.getCurrentPosition());//even "Null" is ok
        try{
            ArrayList <Integer>mEf=new ArrayList<>();
            //mEf.add(0);//tractor beam-- don't move(because you can see it) then 1 dmg
            mEf.add(1);//costs 1 red and 1 yellow AmmoCube--move it to my cell then 3dmg


            shooter.getWeapons().get(0).shoot(targetsLists,mEf,cells);

            //System.out.println(target1.getPlayerName());
            //System.out.println(target1.getStats().getDmgTaken());

            assertEquals(shooter.getCurrentPosition(),target1.getCurrentPosition());

        } catch(PlayerNotSeeableException e){
            e.printStackTrace();
        } catch (PrecedentPlayerNeededException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException e) {
            e.printStackTrace();
        } catch (DifferentPlayerNeededException e) {
            e.printStackTrace();
        } catch (NotEnoughAmmoException e) {
            e.printStackTrace();
        } catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (CardNotPossessedException e) {
            e.printStackTrace();
        } catch (WeaponNotLoadedException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (CellNonExistentException e) {
            e.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerAlreadyDeadException e){

        }
    }

    @Test
    public void heatSeekerTest(){
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
        shooter.setPlayerPos(Model.getMap().getCell(2,3));

        Player target1 = Model.getPlayer(1); // 1 1
        target1.setPlayerPos(Model.getMap().getCell(1,1));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,1));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.addWeapon(weapons.get(6));//Heat seeker 1 target you can't see 3 dmg
        //System.out.println("weapon name: "+ weapons.get(6).getName());
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);
        //targets0.add(target2);
        //targets1.add(target2);
        targetLists.add(targets0);
        //targetLists.add(targets1);
        Model.getMap().setUnvisited();

        try {
            ArrayList<Integer> mEf = new ArrayList<>();
            mEf.add(0);
            //mEf.add(1);
            //List<Cell> cells = new ArrayList<>();
            //cells.add(Model.getMap().getCell(1,1));
            shooter.getWeapons().get(0).shoot(targetLists, mEf, null);
            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());

             */
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.getMessage();
        }

        //System.out.println("target1 danni" + target1.getStats().getDmgTaken().size());
        //System.out.println("target2 danni" + target2.getStats().getDmgTaken().size());
        assert(target1.getStats().getDmgTaken().size() ==3);
        //assert(target2.getStats().getDmgTaken().size() ==2);
    }

    @Test
    public void plasmaGunTest(){
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
        shooter.setPlayerPos(Model.getMap().getCell(1,0));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,1));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,1));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(weapons.get(2));//Plasma Gun
        //System.out.println("Weapon name: " + weapons.get(2).getName());
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List targets2=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(shooter);
        targets1.add(target1);
        targets2.add(target2);

        targetLists.add(targets0);
        targetLists.add(targets1);
        targetLists.add(targets1);
        Model.getMap().setUnvisited();

        try {
            ArrayList<Integer> mEf = new ArrayList<>();
            mEf.add(1);
            mEf.add(0);
            mEf.add(2);

            List<Cell> cells = new ArrayList<>();
            cells.add(Model.getMap().getCell(1,0));
            cells.add(null);
            cells.add(null);
            shooter.getWeapons().get(0).shoot(targetLists, mEf, cells);

        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        }catch(PrecedentPlayerNeededException e)
        {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //System.out.println("target1 danni: " + target1.getStats().getDmgTaken().size());
        assert(shooter.getStats().getCurrentPosition().equals(Model.getMap().getCell(1,0)));
        assertEquals(3, target1.getStats().getDmgTaken().size());

    }

    @Test
    public void grenadeLauncherTest(){
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
        shooter.setPlayerPos(Model.getMap().getCell(1,1));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,1));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,1));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(weapons.get(7));//Grendae launcher
        //System.out.println("Weapon name: " +weapons.get(7).getName());
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);
        targets1.add(target2);
        //targets0.add(target2);
        targetLists.add(targets0);
        targetLists.add(targets1);
        Model.getMap().setUnvisited();

        try {
            ArrayList<Integer> mEf = new ArrayList<>();
            mEf.add(0);
            //mEf.add(1);

            List<Cell> cells = new ArrayList<>();
            cells.add(Model.getMap().getCell(1,0));
            cells.add(Model.getMap().getCell(1,1));//if cell != null teh second effect fa roba
            shooter.getWeapons().get(0).shoot(targetLists, mEf, cells);
            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());

             */
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.getMessage();
        }
        //System.out.println("target1 danni" + target1.getStats().getDmgTaken().size());
        //System.out.println("target2 danni" + target2.getStats().getDmgTaken().size());
        assertEquals(1, target1.getStats().getDmgTaken().size());
        //assert(target1.getCurrentPosition().equals(Model.getMap().getCell(1,0)));
        //assert(shooter.getStats().getCurrentPosition().equals(Model.getMap().getCell(0,0)));
        //assert(target2.getStats().getDmgTaken().size() ==1);
    }

    @Test
    public void rocketLauncherTest(){
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
        shooter.setPlayerPos(Model.getMap().getCell(0,1));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(0,2));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(0,2));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(weapons.get(8));
        //System.out.println("Weapon name: " +weapons.get(8).getName());
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets1.add(target1);
        //targets1.add(target2);
        //targets0.add(target2);

        targetLists.add(targets0);
        targetLists.add(targets1);
        Model.getMap().setUnvisited();

        try {
            ArrayList<Integer> mEf = new ArrayList<>();
            mEf.add(1);
            mEf.add(0);

            mEf.add(2);
            //System.out.println("position before" + target1.getStats().getCurrentPosition());

            List<Cell> cells = new ArrayList<>();

            cells.add(Model.getMap().getCell(0,0));
            cells.add(Model.getMap().getCell(0,3));

            cells.add(null);//add original cell
            shooter.getWeapons().get(0).shoot(targetLists, mEf, cells);
            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());

             */
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (CardNotPossessedException e) {
            e.printStackTrace();
        } catch (CellNonExistentException e) {
            e.printStackTrace();
        } catch (PrecedentPlayerNeededException e) {
            e.printStackTrace();
        } catch (NotEnoughAmmoException e) {
            e.printStackTrace();
        } catch (DifferentPlayerNeededException e) {
            e.printStackTrace();
        } catch (PlayerAlreadyDeadException e){
            e.printStackTrace();
        }

        //System.out.println("target1 danni" + target1.getStats().getDmgTaken().size());
        //System.out.println("target2 danni" + target2.getStats().getDmgTaken().size());
        assert(target1.getStats().getDmgTaken().size() ==3);

        //assert(target1.getStats().getCurrentPosition().equals(Model.getMap().getCell(0,3)));
        assert(shooter.getStats().getCurrentPosition().equals(Model.getMap().getCell(0,0)));
        //assert(target2.getStats().getDmgTaken().size() == 1);
        assert(target2.getStats().getDmgTaken().size() ==1);
    }

    @Test
    public void cyberBladeTest(){
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
        shooter.setPlayerPos(Model.getMap().getCell(1,1));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,0));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,0));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(weapons.get(9));
        //System.out.println("Weapon name: " +weapons.get(9).getName());
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List targets2=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets1.add(target1);

        targets2.add(target2);
        targetLists.add(targets0);
        targetLists.add(targets1);
        targetLists.add(targets2);
        Model.getMap().setUnvisited();

        try {
            ArrayList<Integer> mEf = new ArrayList<>();
            mEf.add(1);
            mEf.add(0);
            mEf.add(2);
            //System.out.println("position before" + target1.getStats().getCurrentPosition());

            List<Cell> cells = new ArrayList<>();
            cells.add(Model.getMap().getCell(1,0));
            cells.add(null);
            cells.add(null);
            shooter.getWeapons().get(0).shoot(targetLists, mEf, cells);
            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());

             */
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (CardNotPossessedException e) {
            e.printStackTrace();
        } catch (CellNonExistentException e) {
            e.printStackTrace();
        } catch (PrecedentPlayerNeededException e) {
            e.printStackTrace();
        } catch (NotEnoughAmmoException e) {
            e.printStackTrace();
        } catch (DifferentPlayerNeededException e) {
            e.printStackTrace();
        } catch (PlayerAlreadyDeadException e){
            e.printStackTrace();
        }
        //System.out.println("target1 danni" + target1.getStats().getDmgTaken().size());
        //System.out.println("target2 danni" + target2.getStats().getDmgTaken().size());
        assert(target1.getStats().getDmgTaken().size() ==2);
        assert(shooter.getCurrentPosition().equals(Model.getMap().getCell(1,0)));
        assert(target2.getStats().getDmgTaken().size() ==2);
    }

    @Test
    public void cyberBladeTest012(){

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
        shooter.setPlayerPos(Model.getMap().getCell(0,0));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(0,0));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(0,1));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(Parser.getWeaponByName("CYBERBLADE"));

        Weapon weapon =shooter.getCurrentWeapons().getList().get(0);

        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List targets2=new ArrayList();

        List<List<Player>> targetLists = new ArrayList<>();

        targets0.add(target1);
        targets2.add(target2);

        targetLists.add(targets0);
        targetLists.add(targets1);
        targetLists.add(targets2);

        Model.getMap().setUnvisited();

        // cell list

        List<Cell> cellList = new ArrayList<>();

        cellList.add(null);

        cellList.add(Model.getMap().getCell(0,1));

        cellList.add(null);

        try {

            weapon.shoot(targetLists, Arrays.asList(0,1,2), cellList);

        } catch(WeaponNotLoadedException e){
            e.printStackTrace();
        }catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (CardNotPossessedException e) {
            e.printStackTrace();
        } catch (CellNonExistentException e) {
            e.printStackTrace();
        } catch (PrecedentPlayerNeededException e) {
            e.printStackTrace();
        } catch (NotEnoughAmmoException e) {
            e.printStackTrace();
        } catch (DifferentPlayerNeededException e) {
            e.printStackTrace();
        } catch (PlayerAlreadyDeadException e){
            e.printStackTrace();
        }

        assertEquals(2,target1.getDmg().size());
        assertEquals(2,target2.getDmg().size());
        assertEquals(target2.getCurrentPosition(), shooter.getCurrentPosition());
    }

    @Test
    public void cyberBladeTest021(){

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
        shooter.setPlayerPos(Model.getMap().getCell(0,0));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(0,0));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(0,0));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(Parser.getWeaponByName("CYBERBLADE"));

        Weapon weapon =shooter.getCurrentWeapons().getList().get(0);

        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List targets2=new ArrayList();

        List<List<Player>> targetLists = new ArrayList<>();

        targets0.add(target1);
        targets2.add(target2);

        targetLists.add(targets0);
        targetLists.add(targets2);
        targetLists.add(targets1);

        Model.getMap().setUnvisited();

        // cell list

        List<Cell> cellList = new ArrayList<>();

        cellList.add(null);

        cellList.add(null);

        cellList.add(Model.getMap().getCell(0,1));

        try {

            weapon.shoot(targetLists, Arrays.asList(0,2,1), cellList);

        } catch(WeaponNotLoadedException e){
            e.printStackTrace();
        }catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (CardNotPossessedException e) {
            e.printStackTrace();
        } catch (CellNonExistentException e) {
            e.printStackTrace();
        } catch (PrecedentPlayerNeededException e) {
            e.printStackTrace();
        } catch (NotEnoughAmmoException e) {
            e.printStackTrace();
        } catch (DifferentPlayerNeededException e) {
            e.printStackTrace();
        } catch (PlayerAlreadyDeadException e){
            e.printStackTrace();
        }

        assertEquals(2,target1.getDmg().size());
        assertEquals(2,target2.getDmg().size());
        assertEquals(Model.getMap().getCell(0,1), shooter.getCurrentPosition());
    }

    @Test
    public void zx2Test(){
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
        shooter.setPlayerPos(Model.getMap().getCell(1,1));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(1,1));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,1));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(weapons.get(10));
        //System.out.println("Weapon name: " +weapons.get(10).getName());
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List targets2 = new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);
        targets0.add(target2);
        //targets2.add(target1);

        //targets0.add(target2);
        targetLists.add(targets0);
        //targetLists.add(targets2);
        Model.getMap().setUnvisited();

        try {
            ArrayList<Integer> mEf = new ArrayList<>();
            //mEf.add(0);
            mEf.add(1);
            //System.out.println("position before" + target1.getStats().getCurrentPosition());

            //List<Cell> cells = new ArrayList<>();
            //cells.add(Model.getMap().getCell(0,3));
            shooter.getWeapons().get(0).shoot(targetLists, mEf, null);
            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());

             */
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.getMessage();
        }
        /*
        System.out.println("target1 danni" + target1.getStats().getDmgTaken().size());
        System.out.println("target2 danni" + target2.getStats().getDmgTaken().size());
        System.out.println("target1 mark" + target1.getStats().getMarks().size());
        System.out.println("target2 mark" + target2.getStats().getMarks().size());

         */

        assert(target1.getStats().getMarks().size() ==1);
        assert(target2.getStats().getMarks().size() ==1);
        //assert(target1.getStats().getMarks().size() == 2); not working effect1
        //assert(target1.getStats().getMarks().size() == 1); not working effect2
        //assert(target2.getStats().getMarks().size() == 1);
        //assert(target2.getStats().getDmgTaken().size() ==2);
    }

    @Test
    public void shotGuntest(){
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
        shooter.setPlayerPos(Model.getMap().getCell(0,0));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(0,0));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,1));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(weapons.get(11));
        //System.out.println("Weapon name: " +weapons.get(11).getName());
        List targets0=new ArrayList();

        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);

        targetLists.add(targets0);
        Model.getMap().setUnvisited();

        try {
            ArrayList<Integer> mEf = new ArrayList<>();
            mEf.add(0);
            //mEf.add(1);

            List<Cell> cells = new ArrayList<>();
            cells.add(Model.getMap().getCell(0,1));
            shooter.getWeapons().get(0).shoot(targetLists, mEf, cells);
            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());

             */
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.getMessage();
        }
        //System.out.println("target1 danni" + target1.getStats().getDmgTaken().size());
        //System.out.println("target2 danni" + target2.getStats().getDmgTaken().size());
        //assert(target1.getCurrentPosition().equals(Model.getMap().getCell(0,1)));
        //assert(target1.getStats().getCurrentPosition().equals(Model.getMap().getCell(1,0)));

    }

    @Test
    public void shockWaveTest(){
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
        playerNames.add("target3");
        ArrayList<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.GREEN);
        pc.add(PlayerColor.YELLOW);
        //generate the map (type 2)
        Model m=new Model(playerNames,pc,2,8);

        //generate a player with a name and its starting position
        Player shooter = Model.getPlayer(0);
        shooter.setPlayerPos(Model.getMap().getCell(0,0));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(0,1));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,0));

        Player target3 = Model.getPlayer(3);
        target3.setPlayerPos(Model.getMap().getCell(1,0));



        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(weapons.get(12));
        //System.out.println("Weapon name: " +weapons.get(12).getName());
        List targets0=new ArrayList();
        List targets1=new ArrayList();
        List targets2 = new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);
        targets0.add(target2);
        targets0.add(target3);
        targetLists.add(targets0);
        Model.getMap().setUnvisited();

        try {
            ArrayList<Integer> mEf = new ArrayList<>();
            //mEf.add(0);
            mEf.add(1);

            List<Cell> cells = new ArrayList<>();
            cells.add(Model.getMap().getCell(1,0));
            shooter.getWeapons().get(0).shoot(targetLists, mEf, null);
            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());

            System.out.println(target3.getPlayerName());
            System.out.println(target3.getStats().getDmgTaken());
            System.out.println(target3.getStats().getMarks());

             */
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (CardNotPossessedException e) {
            e.printStackTrace();
        } catch (CellNonExistentException e) {
            e.printStackTrace();
        } catch (PrecedentPlayerNeededException e) {
            e.printStackTrace();
        } catch (NotEnoughAmmoException e) {
            e.printStackTrace();
        } catch (DifferentPlayerNeededException e) {
            e.printStackTrace();
        } catch (PlayerAlreadyDeadException e){
            e.printStackTrace();
        }
        //System.out.println("target1 danni" + target1.getStats().getDmgTaken().size());
        //System.out.println("target2 danni" + target2.getStats().getDmgTaken().size());
        assert(target1.getStats().getDmgTaken().size() ==1);
        //assert(target2.getStats().getDmgTaken().size() ==1);
        //assert(target3.getStats().getDmgTaken().size() ==1);
        //assert(target1.getStats().getCurrentPosition().equals(Model.getMap().getCell(1,0)));

    }

    @Test
    public void sledgeHammerTest(){
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
        playerNames.add("target3");
        ArrayList<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.PURPLE);
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.GREEN);
        pc.add(PlayerColor.YELLOW);
        //generate the map (type 2)
        Model m=new Model(playerNames,pc,2,8);

        //generate a player with a name and its starting position
        Player shooter = Model.getPlayer(0);
        shooter.setPlayerPos(Model.getMap().getCell(2,3));

        Player target1 = Model.getPlayer(1);
        target1.setPlayerPos(Model.getMap().getCell(2,3));

        Player target2 = Model.getPlayer(2);
        target2.setPlayerPos(Model.getMap().getCell(1,2));


        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.BLUE));
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));
        shooter.addWeapon(weapons.get(13));
        //System.out.println("Weapon name: " +weapons.get(13).getName());
        List targets0=new ArrayList();
        List<List<Player>> targetLists = new ArrayList<>();
        targets0.add(target1);


        targetLists.add(targets0);
        Model.getMap().setUnvisited();

        try {
            ArrayList<Integer> mEf = new ArrayList<>();
            //mEf.add(0);
            mEf.add(1);

            List<Cell> cells = new ArrayList<>();

            cells.add(Model.getMap().getCell(1,3));
            shooter.getWeapons().get(0).shoot(targetLists, mEf, cells);

            /*
            System.out.println(target1.getPlayerName());
            System.out.println(target1.getStats().getDmgTaken());
            System.out.println(target1.getStats().getMarks());

            System.out.println(target2.getPlayerName());
            System.out.println(target2.getStats().getDmgTaken());
            System.out.println(target2.getStats().getMarks());

             */
        }

        catch(WeaponNotLoadedException e){ e.printStackTrace();}
        catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (UncorrectDistanceException uncorrectDistanceException) {
            uncorrectDistanceException.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        } catch (PlayerNotSeeableException e) {
            e.printStackTrace();
        } catch (NotCorrectPlayerNumberException e) {
            e.printStackTrace();
        } catch (UncorrectEffectsException e) {
            e.printStackTrace();
        } catch (CardNotPossessedException e) {
            e.printStackTrace();
        } catch (CellNonExistentException e) {
            e.printStackTrace();
        } catch (PrecedentPlayerNeededException e) {
            e.printStackTrace();
        } catch (NotEnoughAmmoException e) {
            e.printStackTrace();
        } catch (DifferentPlayerNeededException e) {
            e.printStackTrace();
        } catch (PlayerAlreadyDeadException e){

        }
        //System.out.println("target1 danni" + target1.getStats().getDmgTaken().size());
        //System.out.println("target2 danni" + target2.getStats().getDmgTaken().size());
        //assert(target1.getStats().getDmgTaken().size() ==2);
        //assert(target2.getStats().getDmgTaken().size() ==1);
        //assert(target3.getStats().getDmgTaken().size() ==1);
        assert(target1.getStats().getCurrentPosition().equals(Model.getMap().getCell(1,3)));

    }

}


