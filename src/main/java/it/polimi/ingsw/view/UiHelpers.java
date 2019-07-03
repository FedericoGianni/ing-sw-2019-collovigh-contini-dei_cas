package it.polimi.ingsw.view;

import it.polimi.ingsw.utils.PlayerColor;
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

    public static String ammoTranslator(Color c){
        List<Color> list = new ArrayList<>();
        list.add(c);

        return ammoTranslator(list);
    }

    public static List<Color> genColorListFromPowerUps(List<CachedPowerUp> powerUps){
        List<Color> colorList = new ArrayList<>();

        for(CachedPowerUp p : powerUps){
            colorList.add(p.getColor());
        }

        return  colorList;
    }

    public static Color genColorFromPowerUp(CachedPowerUp p){
        return p.getColor();
    }

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
