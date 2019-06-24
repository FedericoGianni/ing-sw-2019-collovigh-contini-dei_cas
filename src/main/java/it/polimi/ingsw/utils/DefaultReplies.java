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

    public static final String DEFAULT_BUY_WEAPON_BUT_NO_NAME_SPECIFIED = ANSI_YELLOW.escape() +
            "[!] Arma da comprare non specificata!" + ANSI_RESET.escape();

    public static final String DEFAULT_WEAPON_NOT_FOUND_IN_SPAWN = ANSI_YELLOW.escape() +
            "[!] Arma non trovata nella spawn cell! " + ANSI_RESET.escape();

    public static final String DEFAULT_WEAPON_NOT_FOUND_IN_BAG = ANSI_YELLOW.escape() +
            "[!] Arma non presente tra le armi del giocatore " + ANSI_RESET.escape();

    public static final String DEFAULT_HAS_MAX_WEAPON_BUT_NOT_SPECIFIED_DISCARD = "HAS_MAX_WEAPON_BUT_NOT_SPECIFIED_DISCARD";
    public static final String DEFAULT_PLAYER_TRIED_TO_MOVE_ENHANCED_BUT_CANT = "PLAYER_TRIED_TO_MOVE_ENHANCED_BUT_CANT";
    public static final String DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX = "PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX";

    public static final String DEFAULT_WEAPON_NOT_LOADED = ANSI_YELLOW.escape() +
            "[!] L'arma selezionata è scarica!" + ANSI_RESET.escape();

    public static final String DEFAULT_PLAYER_IN_SAME_CELL = ANSI_YELLOW.escape() +
            "[!] Sparo non valido: giocatori nella stessa cella!" + ANSI_RESET.escape();

    public static final String DEFAULT_PLAYER_IN_DIFFERENT_CELL = ANSI_YELLOW.escape() +
            "[!] Sparo non valido: giocatori in celle diverse!" + ANSI_RESET.escape();

    public static final String DEFAULT_UNCORRECT_DISTANCE = ANSI_YELLOW.escape() +
            "[!] Sparo non valido: distanza non corretta!" + ANSI_RESET.escape();

    public static final String DEFAULT_SEEABLE_PLAYER = ANSI_YELLOW.escape() +
            "[!] Sparo non valido: giocatori visibili!" + ANSI_RESET.escape();

    public static final String DEFAULT_UNCORRECT_EFFECTS = "UNCORRECT_EFFECTS";

    public static final String DEFAULT_NOT_CORRECT_PLAYER_NUMBER = ANSI_YELLOW.escape() +
            "[!] Numero di giocatori non corretto!" + ANSI_RESET.escape();

    public static final String DEFAULT_PLAYER_NOT_SEEABLE = ANSI_YELLOW.escape() +
            "[!] Sparo non valido: giocatori non visibili! " + ANSI_RESET.escape();

    public static final String DEFAULT_NO_TARGETS_SPECIFIED = ANSI_YELLOW.escape() +
            "[!] Non hai selezionato i bersagli!" + ANSI_RESET.escape();

    public static final String DEFAULT_INEXISTENT_TARGETS =  ANSI_YELLOW.escape() +
            "[!] Bersaglio non esistente!" + ANSI_RESET.escape();

    public static final String DEFAULT_DIFFERENT_PLAYER_NEEDED = ANSI_YELLOW.escape() +
            "[!] Sparo non valido: servono bersagli differenti!" + ANSI_RESET.escape();

    public static final String DEFAULT_TARGETING_SCOPE_ON_NON_TARGETED_PLAYER = "TARGETING_SCOPE_ON_NON_TARGETED_PLAYER";

    public static final String DEFAULT_RECEIVED_FRENZY_BUT_EXPECTED_NORMAL = "RECEIVED_FRENZY_BUT_EXPECTED_NORMAL";
    public static final String DEFAULT_RECEIVED_NORMAL_BUT_EXPECTED_FRENZY = "RECEIVED_NORMAL_BUT_EXPECTED_FRENZY";

    public static final String DEFAULT_NO_ENOUGH_AMMO = ANSI_YELLOW.escape() +
            "[!] Non hai abbastanza munizioni!" + ANSI_RESET.escape();

    public static final String DEFAULT_WEAPON_ALREADY_LOADED = ANSI_YELLOW.escape() +
            "[!] Arma già carica!" + ANSI_RESET.escape();
    
}
