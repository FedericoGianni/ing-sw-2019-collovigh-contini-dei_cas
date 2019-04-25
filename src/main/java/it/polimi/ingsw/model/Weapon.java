package it.polimi.ingsw.model;

public abstract class Weapon {
    boolean isLoaded;

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


}
