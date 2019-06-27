package it.polimi.ingsw.model.player;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Subject;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.utils.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AmmoBag extends Subject implements Bag<AmmoCube> {

    public static final int MAX_FOR_COLOR = 3;

    /**
     * this is an ArrayList of AmmoCubes sorted by color
     */
    private List<AmmoCube> ammoCubes;
    private final Player player;


    /**
     * Constructor
     */
    public AmmoBag(Player player) {

        this.ammoCubes = new ArrayList<>();
        this.player = player;
    }


    /**
     *
     * @return a copy of the list of AmmoCubes
     */
    @Override
    public List<AmmoCube> getList() {
        return new ArrayList<>(this.ammoCubes);
    }


    /**
     * This function will add an element to the list (only if the list has less than MAX_FOR_COLOR ammo cubes) and sort it
     *
     * @param item is the AmmoCubes to add
     */
    @Override
    public void addItem(AmmoCube item) {

        List<AmmoCube> list = this.ammoCubes.stream()
                .filter(ammoCube ->
                    ammoCube.getColor() == item.getColor()
                ).collect(Collectors.toList());

        if (list.size() < MAX_FOR_COLOR) {

            this.ammoCubes.add(item);

            this.ammoCubes.sort(Comparator.comparing(AmmoCube::getColor));

            this.updateAll(this);
        }


    }


    /**
     *
     * @return the number of ammos of each type, 0->red,1->blue,->yellow
     */
    public int[] getAmount()
    {
        int []ammo=new int[3];
        for(int i=0;i<this.ammoCubes.size();i++)
        {
            if(this.ammoCubes.get(i).getColor()== Color.RED)
            {
                ammo[0]++;
            }else if(this.ammoCubes.get(i).getColor()==Color.BLUE)
            {
                ammo[1]++;
            }else{
                ammo[2]++;
            }
        }
        return ammo;
    }


    /**
     *
     * @param item is the AmmoCube that will be given as
     * @return and will be removed from the inventory
     * Than the method will sort again the list
     */
    @Override
    public AmmoCube getItem(AmmoCube item) throws CardNotPossessedException {

        if (this.ammoCubes.isEmpty()) throw new CardNotPossessedException();
        if (!this.ammoCubes.contains(item)) throw new CardNotPossessedException();

        else {
            this.ammoCubes.remove(item);
            this.ammoCubes.sort(Comparator.comparing(AmmoCube::getColor));

            updateAll(this);

            return item;
        }
    }


    /**
     * This method checks if the
     * @param item is held by the player and
     * @return true if this is the case
     */
    @Override
    public Boolean hasItem(AmmoCube item) {

        return this.ammoCubes.contains(item);
    }

    /**
     * This method will check if a player has max AmmoCube for a given color
     * @param powerUpColor is the color of the powerUp
     * @return the number of AmmoCube left before the given color reaches the max
     */
    public int leftMaxAmmo( Color powerUpColor ){

        int actualForColor =  (int) ammoCubes
                .stream()
                .filter( x -> x.getColor().equals(powerUpColor) )
                .count();

        return MAX_FOR_COLOR - actualForColor;
    }
}
