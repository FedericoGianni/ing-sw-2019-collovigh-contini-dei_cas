package it.polimi.ingsw.model;

/**
 * //TODO
 */
public abstract class MicroEffect {
    /**
     *
     */
    public MicroEffect() {

    }

    /**
     *
     * This method is set abstract because it will have different implementation in each subclass, but we wanted to make sure that each of them has it
     *
     * @param player on which the effect will be applied
     */
    public abstract void applyOn(Player player);

    /**
     * This method is set abstract because it will have different implementation in each subclass, but we wanted to make sure that each of them has it
     *
     * @return a clone of this object (but sonar does not let me use the clone name)
     */
    public abstract MicroEffect copy();
    public abstract int type();
}
