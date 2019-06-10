package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.Directions;

import java.awt.*;
import java.util.List;

import static it.polimi.ingsw.model.map.JsonMap.MAP_C;
import static it.polimi.ingsw.model.map.JsonMap.MAP_R;

/**
 * This class contains helper methods needed by both UserInterfaces (cli/gui)
 */
public class UiHelpers {

    public static final int DEFAULT_MAX_NORMAL_MOVES = 3;
    public static final int DEFAULT_DMG_TO_UNLOCK_ENHANCED_SHOOT = 6;
    public static final int DEFAULT_DMG_TO_UNLOCK_ENHANCED_GRAB = 3;
    public static final int DEFAULT_MOVES_WITH_GRAB = 1;
    public static final int DEFAULT_ENHANCED_MOVES_WITH_GRAB = 2;
    public static final int DEFAULT_ENHANCED_MOVES_WITH_SHOOT = 1;

    /**
     * Helper method to translate cardinal directions from IT to EN
     * @param s a String which is already in the right form (cardinal point to upper case)
     * @return the enum in Directions linked to the String
     */
    public static Directions directionTranslator(String s){
        Directions direction;

        switch (s){
            case "NORD":
                direction = Directions.NORTH;
                break;

            case "SUD":
                direction = Directions.SOUTH;
                break;

            case "EST":
                direction = Directions.EAST;
                break;

            case "OVEST":
                direction = Directions.WEST;
                break;

            default:
                //this can never happen
                System.out.println("[DEBUG] direzione non valida!");
                direction = Directions.NORTH;
                break;
        }

        return direction;
    }

    /**
     * Helper method to translate color with enum from italian to english
     * @param s color in italian
     * @return PlayerColor enum corrisponding to the italian color
     */
    public static PlayerColor colorTranslator(String s){
        PlayerColor playerColor;

        switch (s){

            case "VERDE":
                playerColor = PlayerColor.GREEN;
                break;

            case "GIALLO":
                playerColor = PlayerColor.YELLOW;
                break;

            case "VIOLA":
                playerColor = PlayerColor.PURPLE;
                break;

            case "GRIGIO":
                playerColor = PlayerColor.GREY;
                break;

            case "BLU":
                playerColor = PlayerColor.BLUE;
                break;

            default:
                System.out.println("[DEBUG] PlayerColor non valido!");
                playerColor = PlayerColor.GREY;
        }

        return playerColor;
    }

    public static Point genPointFromDirections(List<Directions> directions, Point start){

        Point finalPos = new Point(start);
        //generate final point destination to forward to the server
        for (Directions direction : directions) {
            switch (direction) {
                case NORTH:
                    if (finalPos.x > 0)
                        finalPos.x--;
                    break;

                case SOUTH:
                    if (finalPos.x < MAP_R-1)
                        finalPos.x++;
                    break;

                case WEST:
                    if (finalPos.y > 0)
                        finalPos.y--;
                    break;

                case EAST:
                    if (finalPos.y < MAP_C-1)
                        finalPos.y++;
                    break;
            }
        }

        return finalPos;
    }

}
