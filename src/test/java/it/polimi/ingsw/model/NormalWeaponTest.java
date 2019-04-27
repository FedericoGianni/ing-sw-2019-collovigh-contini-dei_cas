package it.polimi.ingsw.model;

import customsexceptions.WeaponNotLoadedException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class NormalWeaponTest {

    @Test
    void weaponsCreator() {
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();
        NormalWeapon.weaponsCreator();
        /*for(int i = 0; i< NormalWeapon.getNormalWeapons().size(); i++)
        {
            System.out.println(NormalWeapon.getNormalWeapons().get(i).getName());
            for(int j = 0; j< NormalWeapon.getNormalWeapons().get(i).getEffects().size(); j++)
            {
                System.out.println(NormalWeapon.getNormalWeapons().get(i).getEffects().get(j).getName());
            }

        }*/
    }

    @Test
    void shoot() {
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
        shooter.getAmmoBag();
        shooter.getAmmoBag().addItem(new AmmoCube(Color.RED));//one only for evitating null Pointer
        shooter.addWeapon(NormalWeapon.getNormalWeapons().get(0));//not how it works but easy
        try{
        NormalWeapon.getNormalWeapons().get(1).shoot(target,NormalWeapon.getNormalWeapons().get(1).getEffects());}
        catch(WeaponNotLoadedException e){ e.printStackTrace();}
    }
}