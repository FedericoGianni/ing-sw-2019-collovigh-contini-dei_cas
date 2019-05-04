package it.polimi.ingsw.model;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.customsexceptions.DeadPlayerException;
import it.polimi.ingsw.customsexceptions.FrenzyActivatedException;
import it.polimi.ingsw.customsexceptions.OverKilledPlayerException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

class NormalWeaponTest {

    @Test
    void weaponsCreator() {
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();
        NormalWeapon.weaponsCreator();
        assertNotNull(NormalWeapon.getNormalWeapons());

    }

    @Test
    void shoot() throws FrenzyActivatedException {//one shot, one effect, one target, free effect---the most basic possible
        weaponsCreator();

        List<String>  names=new ArrayList<String>();
        names.add("shooter");
        names.add("target");
        List<PlayerColor> pc=new ArrayList<PlayerColor>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(names,pc,2);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target1=Model.getGame().getPlayers().get(1);

        shooter.setPlayerPos(Model.getMap().getCell(0,3));
        target1.setPlayerPos(Model.getMap().getCell(1,3));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        ArrayList targets=new ArrayList();
        targets.add(target1);
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy
        try{
            ArrayList <MacroEffect>mEf=new ArrayList<>();
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(0));
            shooter.getWeapons().get(0).shoot(targets,mEf);
            //System.out.println(target1.getStats().getDmgTaken());
        }
        catch(WeaponNotLoadedException e){ e.printStackTrace();} catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectTargetDistance uncorrectTargetDistance) {
            uncorrectTargetDistance.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shoot2() throws FrenzyActivatedException{//now two effects ,you also need to pay and the player can, if it can't the notEnoughAmmoException sclera
        weaponsCreator();

        List<String>  names=new ArrayList<>();
        names.add("shooter");
        names.add("target");
        List<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(names,pc,2);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target1=Model.getGame().getPlayers().get(1);
        shooter.setPlayerPos(Model.getMap().getCell(0,3));
        target1.setPlayerPos(Model.getMap().getCell(1,3));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy
        ArrayList targets=new ArrayList();
        targets.add(target1);
        try{
            ArrayList <MacroEffect>mEf=new ArrayList<>();
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(0));
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(1));//costs 1 red AmmoCube
            shooter.getWeapons().get(0).shoot(targets,mEf);}
        catch(WeaponNotLoadedException e){ e.printStackTrace();} catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectTargetDistance uncorrectTargetDistance) {
            uncorrectTargetDistance.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shoot3() throws FrenzyActivatedException{//same as the shoot2 test but launches the notEnoughAmmoException
        weaponsCreator();

        List<String>  names=new ArrayList<>();
        names.add("shooter");
        names.add("target");
        List<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(names,pc,2);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target1=Model.getGame().getPlayers().get(1);
        shooter.setPlayerPos(Model.getMap().getCell(0,3));
        target1.setPlayerPos(Model.getMap().getCell(1,3));

        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));//one only for evitating null Pointer
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy
        ArrayList targets=new ArrayList();
        targets.add(target1);
        try{
            ArrayList <MacroEffect>mEf=new ArrayList<>();
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(0));
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(1));//costs 1 red AmmoCube
            shooter.getWeapons().get(0).shoot(targets,mEf);

        }
        catch(WeaponNotLoadedException e){} catch (PlayerInSameCellException e) {
            e.printStackTrace();
        } catch (DeadPlayerException e) {
            e.printStackTrace();
        } catch (UncorrectTargetDistance uncorrectTargetDistance) {
            uncorrectTargetDistance.printStackTrace();
        } catch (SeeAblePlayerException e) {
            e.printStackTrace();
        } catch (OverKilledPlayerException e) {
            e.printStackTrace();
        } catch (PlayerInDifferentCellException e) {
            e.printStackTrace();
        }
    }

}