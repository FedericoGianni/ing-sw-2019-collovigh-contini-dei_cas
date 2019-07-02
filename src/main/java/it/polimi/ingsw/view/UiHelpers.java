package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.map.JsonMap.MAP_C;
import static it.polimi.ingsw.model.map.JsonMap.MAP_R;
import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.*;

/**
 * This class contains helper methods needed by both UserInterfaces (cli/gui)
 */
public class UiHelpers {

    public static final int DEFAULT_MAX_NORMAL_MOVES = 3;
    public static final int DEFAULT_MAX_FRENZY_MOVES = 4;
    public static final int DEFAULT_DMG_TO_UNLOCK_ENHANCED_SHOOT = 6;
    public static final int DEFAULT_DMG_TO_UNLOCK_ENHANCED_GRAB = 3;
    public static final int DEFAULT_MOVES_WITH_GRAB = 1;
    public static final int DEFAULT_ENHANCED_MOVES_WITH_GRAB = 2;
    public static final int DEFAULT_MOVES_WITH_FRENZY = 2;
    public static final int DEFAULT_MOVES_WITH_ENHANCED_FRENZY = 3;
    public static final int DEFAULT_ENHANCED_MOVES_WITH_SHOOT = 1;
    public static final int DEFAULT_MOVES_WITH_FRENZY_SHOOT = 1;
    public static final int DEFAULT_MOVES_WITH_ENHANCED_FRENZY_SHOOT = 2;
    public static final int DEFAULT_MAX_WEAPONS = 3;

    /**
     * Helper method to translate cardinal directions from IT to EN
     * @param s a String which is already in the right form (cardinal point to upper case)
     * @return the enum in Directions linked to the String
     */
    public static Directions directionTranslator(String s){
        Directions direction;

        switch (s){
            case "N":
                direction = Directions.NORTH;
                break;

            case "S":
                direction = Directions.SOUTH;
                break;

            case "E":
                direction = Directions.EAST;
                break;

            case "O":
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

    /**
     * generates the final point from a starting point with the directionList specified
     * @param directions list of directions to specify
     * @param start starting point
     * @return final position after applying in order all direction from start point
     */
    public static Point genPointFromDirections(List<Directions> directions, Point start){

        Point finalPos = new Point(start);

        if(directions != null && !directions.isEmpty()) {

            //generate final point destination to forward to the server
            for (Directions direction : directions) {
                switch (direction) {
                    case NORTH:
                        if (finalPos.x > 0)
                            finalPos.x--;
                        break;

                    case SOUTH:
                        if (finalPos.x < MAP_R - 1)
                            finalPos.x++;
                        break;

                    case WEST:
                        if (finalPos.y > 0)
                            finalPos.y--;
                        break;

                    case EAST:
                        if (finalPos.y < MAP_C - 1)
                            finalPos.y++;
                        break;
                }
            }
        }

        return finalPos;
    }

    /**
     * Useful method to translate weapon names from EN to IT (since we use EN weapon names in json)
     * @param weapon name of the weapon to be translated
     * @return the weapon name in italian
     */
    public static String weaponTranslator(String weapon){

        String translate;

        switch (weapon) {

            case "LOCK RIFLE":
                translate = "DISTRUTTORE";
                break;

            case "MACHINE GUN":
                translate = "MITRAGLIATRICE";
                break;

            case "ELECTROSCYTHE":
                translate = "FALCE PROTONICA";
                break;

            case "TRACTOR BEAM":
                translate = "RAGGIO TRAENTE";
                break;

            case "THOR":
                translate = "TORPEDINE";
                break;

            case "VORTEX CANNON":
                translate = "CANNONE VORTEX";
                break;

            case "PLASMA GUN":
                translate = "FUCILE AL PLASMA";
                break;

            case "FURNACE":
                translate = "VULCANIZZATORE";
                break;

            case "HEATSEEKER":
                translate = "RAZZO TERMICO";
                break;

            case "WHISPER":
                translate = "FUCILE DI PRECISIONE";
                break;

            case "HELLION":
                translate = "RAGGIO SOLARE";
                break;

            case "FLAME THROWER":
                translate = "LANCIAFIAMME";
                break;

            case "GRENADE LAUNCHER":
                translate = "LANCIAGRANATE";
                break;

            case "SHOTGUN":
                translate = "FUCILE A POMPA";
                break;

            case "ROCKET LAUNCHER":
                translate = "LANCIARAZZI";
                break;

            case "POWER GLOVE":
                translate = "CYBERGUANTO";
                break;

            case "RAYLGUN":
                translate = "FUCILE LASER";
                break;

            case "SHOCKWAVE":
                translate = "ONDA D'URTO";
                break;

            case "CYBERBLADE":
                translate = "SPADA FOTONICA";
                break;

            case "SLEDGEHAMMER":
                translate = "MARTELLO IONICO";
                break;

            case "RAILGUN":
                translate = "FUCILE LASER";
                break;

            default:
                translate = weapon;

            }

        return translate;

    }

    /**
     * translate player color to ansi color, useful for CLI methods
     * @param playerColor color of the player to generate ANSI color
     * @return ANSI color of the specified player color
     */
    public static AsciiColor colorAsciiTranslator(PlayerColor playerColor){

        switch (playerColor){
            case BLUE:
                return AsciiColor.ANSI_BLUE;

            case GREY:
                return AsciiColor.ANSI_WHITE;

            case YELLOW:
                return AsciiColor.ANSI_YELLOW;

            case PURPLE:
                return AsciiColor.ANSI_PURPLE;

            case GREEN:
                return AsciiColor.ANSI_GREEN;
        }

        System.out.println("[DEBUG] PlayerColor not valid!");
        return AsciiColor.ANSI_WHITE;
    }

    /**
     * translates the list of color passed as paramter with a nicer version of ammo to display
     * @param ammoList list of color to display
     * @return a string representing the ammo view
     */
    public static String ammoTranslator(List<Color> ammoList){

        String s = new String();

        for (Color c : ammoList) {

            switch (c){

                case BLUE:
                    s = s.concat(ANSI_BLUE.escape());
                    break;

                case RED:
                    s = s.concat(ANSI_RED.escape());
                    break;

                case YELLOW:
                    s = s.concat(ANSI_YELLOW.escape());
                    break;

                default:
                    s = s.concat(ANSI_WHITE.escape());
            }

            s = s.concat("°" + ANSI_RESET.escape());
        }

        return s;
    }

    /**
     * translate a single ammo, same as ammoTranslator with color list but just with a single color
     * @param c color to translate
     * @return string representing a view of the ammo
     */
    public static String ammoTranslator(Color c){
        List<Color> list = new ArrayList<>();
        list.add(c);

        return ammoTranslator(list);
    }

    /**
     * generates a color list from a list of powerups
     * @param powerUps list of cached power ups
     * @return a list of color from the colors of the cached powerups passed as parameters
     */
    public static List<Color> genColorListFromPowerUps(List<CachedPowerUp> powerUps){
        List<Color> colorList = new ArrayList<>();

        for(CachedPowerUp p : powerUps){
            colorList.add(p.getColor());
        }

        return  colorList;
    }

    /**
     * same as genColorLitFromPowerups but with a single CachedPowerUp instead of a list
     * @param p cachedPowerup whom color has to be extracted
     * @return color corrispoding to the color of the CachedPowerUp passed as a parameter
     */
    public static Color genColorFromPowerUp(CachedPowerUp p){
        return p.getColor();
    }

    /**
     * extract the powerups types form a list of cached power ups
     * @param powerUps cachedPowerups list
     * @return a list of enum PowerUpType
     */
    public static List<PowerUpType> genTypeListFromPowerUps(List<CachedPowerUp> powerUps){
        List<PowerUpType> powerUpTypes = new ArrayList<>();

        for(CachedPowerUp p : powerUps){
            powerUpTypes.add(p.getType());
        }

        return powerUpTypes;
    }

    /**
     * Check if the player can pay the specified cost, either with powerups or ammo
     * @param cost to be checked
     * @param ammo current ammo in user's hand
     * @param powerUps current powerups in user's hand
     * @return
     */
    public static boolean canPay(List<Color> cost, List<Color> ammo, List<Color> powerUps){

        for(Color c : cost){
            if(ammo.contains(c)){
                ammo.remove(c);
            } else if (powerUps.contains(c)){
                powerUps.remove(c);
            } else {
                return false;
            }
        }

        return true;
    }


}
