package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.customsexceptions.*;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class Thor extends Weapon {

    private ArrayList<AmmoCube> weaponCost;//cost of the weapon
    private ArrayList<AmmoCube> effectsCost;//only this beacuse every effects cst 1 ammoCube so get(0)->second effect cost and get(1) is the second
    private boolean isLoaded;

    public  ArrayList<AmmoCube> getEffectsCost()
    {
        return this.effectsCost;
    }
    public Thor() {
        weaponCost=new ArrayList<>();
        effectsCost=new ArrayList<>();
        this.weaponCost.add(new AmmoCube(Color.BLUE));
        this.weaponCost.add(new AmmoCube(Color.RED));
        effectsCost.add(new AmmoCube(Color.BLUE));
        effectsCost.add(new AmmoCube(Color.BLUE));
        this.isLoaded = true;
    }

    @Override
    public boolean isLoaded() {
        return this.isLoaded;
    }

    @Override
    public void reload() throws NotAbleToReloadException {

        if(this.canBeReloaded()) {

            try {

                isPossessedBy().pay(weaponCost);

            } catch (CardNotPossessedException e) {

                throw new NotAbleToReloadException();
            }

            this.isLoaded = true;

        } else{

            throw new NotAbleToReloadException();

        }
    }


    private boolean canBeReloaded() {

        return isPossessedBy().canPay(weaponCost);
    }

    @Override
    public void shoot(List<List<Player>> targetLists, List<Integer> effects, List<Cell> cells) throws WeaponNotLoadedException, OverKilledPlayerException, DeadPlayerException, PlayerInSameCellException, PlayerInDifferentCellException, UncorrectDistanceException, SeeAblePlayerException, FrenzyActivatedException, UncorrectEffectsException, NotCorrectPlayerNumberException, PlayerNotSeeableException {

        for(int i=0;i<effects.size();i++)//checks that i can actually shoot
        {
            if(i==0)
            {
                if(!isPossessedBy().canSee().contains(targetLists.get(i).get(0)))
                    throw new PlayerNotSeeableException();
            }
            if(i>0)
            {

                if(!targetLists.get(i-1).get(0).canSee().contains(targetLists.get(i).get(0)))
                    throw new PlayerNotSeeableException();

            }

            if(effects.get(i)!=i)
                throw new UncorrectEffectsException();
        }

        for(int i=0;i<effects.size();i++)
        {

            if(i==0)//first macroeffect
            {
                targetLists.get(i).get(0).addDmg(isPossessedBy().getPlayerId(),2);
            }else if(i==1)//second macroeffect
            {
                targetLists.get(i).get(0).addDmg(isPossessedBy().getPlayerId(),1);
            }
            else if(i==2)//third macroeffect
            {
                targetLists.get(i).get(0).addDmg(isPossessedBy().getPlayerId(),2);
            }
        }
    }

    @Override
    public void print() {
        System.out.println(this.getName());
    }

    @Override
    public String getName() {
        return "T.H.O.R.";
    }

    @Override
    public List<AmmoCube> getCost() {
        return this.weaponCost.subList(1,weaponCost.size());
    }

    @Override
    public List<AmmoCube> getReloadCost() {

        return this.weaponCost;

    }

    public void setUnloaded()
    {
        this.isLoaded=false;
    }
}
