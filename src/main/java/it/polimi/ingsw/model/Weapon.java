package it.polimi.ingsw.model;

import customsexceptions.WeaponNotLoadedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Weapon {
    private boolean isLoaded;
    private boolean special;
    /**
     * @return Boolean if the weapon is loaded
     */
    public final Boolean isLoaded() {

        return this.isLoaded;
    }

    /**
     * reloads the weapon
     */
    public final void reload()
    {
        this.isLoaded=true;
    }

    /**
     * every weapon type need to say if it can be reloaded
     * @param aB
     * @return
     */
    public abstract boolean canBeReloaded(AmmoBag aB);

    public final Player isPossessedBy(){

        List<Player> list = Model.getGame().getPlayers().stream()
                .filter(player -> player.getCurrentWeapons().hasItem(this))
                .collect(Collectors.toList());

        return (list.isEmpty()) ? null : list.get(0);
    }

    public abstract void shoot(Player target, Player shooter, ArrayList<MacroEffect> mE)throws WeaponNotLoadedException;
    public final boolean isSpecial()
    {
        return this.special;
    }
    public final void setSpecial()
    {
        this.special=true;
    }

    public final void setNormal()
    {
        this.special=false;
    }

    /**
     *
     * @param cost
     * @param possessed
     * @return true if the player can pay something in ammo
     */

    public final boolean canPay(List<AmmoCube> cost,AmmoBag possessed)
    {
        int []ammo=possessed.getAmount();
        int[]ammoC=new int[3];
        for(int i=0;i<cost.size();i++)
        {
            if(cost.get(i).getColor()==Color.RED)
            {
                ammoC[0]++;
            }else if(cost.get(i).getColor()==Color.BLUE)
            {
                ammoC[1]++;
            }else{
                ammoC[2]++;
            }
        }
        if(ammo[0]-ammoC[0]>=0&&ammo[1]-ammoC[1]>=0&&ammo[2]-ammoC[2]>=0)//ammo i Have - ammo Cost for each type il >=0 then i can reload
        {
            return true;
        }else{
            return false;
        }
    }
}
