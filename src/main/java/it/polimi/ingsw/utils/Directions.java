package it.polimi.ingsw.utils;

public enum Directions {

    NORTH,
    SOUTH,
    EAST,
    WEST;

    public Directions getOpposite(){

        switch (this){

            case NORTH:

                return SOUTH;

            case SOUTH:

                return NORTH;

            case EAST:

                return WEST;

            case WEST:

                return EAST;

            default:

                return null;
        }
    }
}
