package it.polimi.ingsw.utils;

import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.ANSI_RESET;
import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.ANSI_YELLOW;

public class DefaultReplies {

    //reload

    public static final String DEFAULT_GAME_NON_EXISTENT = "GAME_NON_EXISTENT";

    // powerUp phase

    public static final String DEFAULT_PLAYER_USED_NEWTON_ON_HIMSELF = "PLAYER_USED_NEWTON_ON_HIMSELF";
    public static final String DEFAULT_PLAYER_DOES_NOT_POSSESS_POWERUP = "PLAYER_DOES_NOT_POSSESS_POWERUP";
    public static final String DEFAULT_CELL_NOT_EXISTENT = "CELL_NOT_EXISTENT";

    // action phase

    public static final String DEFAULT_CANNOT_BUY_WEAPON = ANSI_YELLOW.escape() +
            "[!] Non hai abbastanza munizioni per comprare l'arma!" + ANSI_RESET.escape();

    public static final String DEFAULT_ALREADY_PICKED_AMMO_HERE = ANSI_YELLOW.escape() +
            "[!] Hai già raccolto in questa cella! Non ci sono piu munizioni." + ANSI_RESET.escape();

    public static final String DEFAULT_BUY_WEAPON_BUT_NO_NAME_SPECIFIED = "BUY_WEAPON_BUT_NO_NAME_SPECIFIED";
    public static final String DEFAULT_WEAPON_NOT_FOUND_IN_SPAWN = "WEAPON_NOT_FOUND_IN_SPAWN";
    public static final String DEFAULT_WEAPON_NOT_FOUND_IN_BAG = "WEAPON_NOT_FOUND_IN_BAG";
    public static final String DEFAULT_HAS_MAX_WEAPON_BUT_NOT_SPECIFIED_DISCARD = "HAS_MAX_WEAPON_BUT_NOT_SPECIFIED_DISCARD";
    public static final String DEFAULT_PLAYER_TRIED_TO_MOVE_ENHANCED_BUT_CANT = "PLAYER_TRIED_TO_MOVE_ENHANCED_BUT_CANT";
    public static final String DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX = "PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX";

    public static final String DEFAULT_WEAPON_NOT_LOADED = "WEAPON_NOT_LOADED";
    public static final String DEFAULT_PLAYER_IN_SAME_CELL = "PLAYER_IN_SAME_CELL";
    public static final String DEFAULT_PLAYER_IN_DIFFERENT_CELL = "PLAYER_IN_DIFFERENT_CELL";
    public static final String DEFAULT_UNCORRECT_DISTANCE = "UNCORRECT_DISTANCE";
    public static final String DEFAULT_SEEABLE_PLAYER = "SEEABLE_PLAYER";
    public static final String DEFAULT_UNCORRECT_EFFECTS = "UNCORRECT_EFFECTS";
    public static final String DEFAULT_NOT_CORRECT_PLAYER_NUMBER = "NOT_CORRECT_PLAYER_NUMBER";
    public static final String DEFAULT_PLAYER_NOT_SEEABLE = "PLAYER_NOT_SEEABLE";
    public static final String DEFAULT_NO_TARGETS_SPECIFIED = "NO_TARGETS_SPECIFIED";
    public static final String DEFAULT_INEXISTENT_TARGETS =  "UNEXISTENT_TARGETS";
    public static final String DEFAULT_DIFFERENT_PLAYER_NEEDED = "DIFFERENT_PLAYER_NEEDED";
    public static final String DEFAULT_TARGETING_SCOPE_ON_NON_TARGETED_PLAYER = "TARGETING_SCOPE_ON_NON_TARGETED_PLAYER";


    public static final String DEFAULT_RECEIVED_FRENZY_BUT_EXPECTED_NORMAL = "RECEIVED_FRENZY_BUT_EXPECTED_NORMAL";
    public static final String DEFAULT_RECEIVED_NORMAL_BUT_EXPECTED_FRENZY = "RECEIVED_NORMAL_BUT_EXPECTED_FRENZY";

    public static final String DEFAULT_NO_ENOUGH_AMMO = "NO_ENOUGH_AMMO";
    public static final String DEFAULT_WEAPON_ALREADY_LOADED = "WEAPON_ALREADY_LOADED";

    private DefaultReplies() {
    }
}
