package it.polimi.ingsw.model;

import customsexceptions.WeaponNotLoadedException;
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
        /*for(int i = 0; i< NormalWeapon.getNormalWeapons().size(); i++)
        {
            System.out.println(NormalWeapon.getNormalWeapons().get(i).getName());
            for(int j = 0; j< NormalWeapon.getNormalWeapons().get(i).getEffects().size(); j++)
            {
                System.out.println(NormalWeapon.getNormalWeapons().get(i).getEffects().get(j).getName());
                System.out.println(NormalWeapon.getNormalWeapons().get(i).getEffects().get(j).getEffectCost());
            }

        }*/
    }

    @Test
    void shoot() {//one shot, one effect, one target, free effect---the most basic possible
        weaponsCreator();

        List<String>  names=new ArrayList<String>();
        names.add("shooter");
        names.add("target");
        List<PlayerColor> pc=new ArrayList<PlayerColor>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(names,pc,2);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target=Model.getGame().getPlayers().get(1);
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy
        try{
            ArrayList <MacroEffect>mEf=new ArrayList<MacroEffect>();
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(0));
            shooter.getWeapons().get(0).shoot(target,mEf);}
        catch(WeaponNotLoadedException e){ e.printStackTrace();}
    }

    @Test
    void shoot2() {//now two effects ,you also need to pay and the player can, if it can't the notEnoughAmmoException sclera
        weaponsCreator();

        List<String>  names=new ArrayList<>();
        names.add("shooter");
        names.add("target");
        List<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(names,pc,2);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target=Model.getGame().getPlayers().get(1);
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy
        try{
            ArrayList <MacroEffect>mEf=new ArrayList<>();
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(0));
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(1));//costs 1 red AmmoCube
            shooter.getWeapons().get(0).shoot(target,mEf);}
        catch(WeaponNotLoadedException e){ e.printStackTrace();}
    }

    @Test
    void shoot3() {//same as the shoot2 test but launches the notEnoughAmmoException
        weaponsCreator();

        List<String>  names=new ArrayList<>();
        names.add("shooter");
        names.add("target");
        List<PlayerColor> pc=new ArrayList<>();
        pc.add(PlayerColor.BLUE);
        pc.add(PlayerColor.PURPLE);
        Model m=new Model(names,pc,2);

        Player shooter= Model.getGame().getPlayers().get(0);
        Player target=Model.getGame().getPlayers().get(1);
        shooter.getAmmoBag().addItem(new AmmoCube(Color.YELLOW));//one only for evitating null Pointer
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy
        try{
            ArrayList <MacroEffect>mEf=new ArrayList<>();
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(0));
            mEf.add(NormalWeapon.getNormalWeapons().get(0).getEffects().get(1));//costs 1 red AmmoCube
            shooter.getWeapons().get(0).shoot(target,mEf);}
        catch(WeaponNotLoadedException e){ e.printStackTrace();}
    }

}