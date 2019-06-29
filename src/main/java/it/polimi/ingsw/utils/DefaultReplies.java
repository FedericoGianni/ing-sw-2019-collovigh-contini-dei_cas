package it.polimi.ingsw.utils;

import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.ANSI_RESET;
import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.ANSI_YELLOW;

public class DefaultReplies {

    //reload

    public static final String DEFAULT_GAME_NON_EXISTENT = "GAME_NON_EXISTENT";

    // powerUp phase

    public static final String DEFAULT_PLAYER_USED_NEWTON_ON_HIMSELF = ANSI_YELLOW.escape() +
            "[!] Non puoi usare Newton su te stesso!" + ANSI_RESET.escape();

    public static final String DEFAULT_PLAYER_DOES_NOT_POSSESS_POWERUP = ANSI_YELLOW.escape() +
            "[!] Non possiedi il poweUp selezionato" + ANSI_RESET.escape();

    public static final String DEFAULT_CELL_NOT_EXISTENT = ANSI_YELLOW.escape() +
            "[!] Cella non esistente!" + ANSI_RESET.escape();

    // action phase

    public static final String DEFAULT_INVALID_SHOOT_HEADER = ANSI_YELLOW.escape() +
            "[!] Sparo non valido: " + ANSI_RESET.escape();

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

    public static final String DEFAULT_HAS_MAX_WEAPON_BUT_NOT_SPECIFIED_DISCARD = ANSI_YELLOW.escape() +
            "[!] Hai già 3 armi, non hai specificato un arma da scartare!" + ANSI_RESET.escape();

    public static final String DEFAULT_PLAYER_TRIED_TO_MOVE_ENHANCED_BUT_CANT = "PLAYER_TRIED_TO_MOVE_ENHANCED_BUT_CANT";
    public static final String DEFAULT_PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX = "PLAYER_TRIED_TO_MOVE_MORE_THAN_MAX";

    public static final String DEFAULT_WEAPON_NOT_LOADED = DEFAULT_INVALID_SHOOT_HEADER + ANSI_YELLOW.escape() +
            "L'arma selezionata è scarica!" + ANSI_RESET.escape();

    public static final String DEFAULT_PLAYER_IN_SAME_CELL = DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "giocatori nella stessa cella!" + ANSI_RESET.escape();

    public static final String DEFAULT_PLAYER_IN_DIFFERENT_CELL = DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "[!] Sparo non valido: giocatori in celle diverse!" + ANSI_RESET.escape();

    public static final String DEFAULT_UNCORRECT_DISTANCE = DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "[!] Sparo non valido: distanza non corretta!" + ANSI_RESET.escape();

    public static final String DEFAULT_SEEABLE_PLAYER = DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "[!] Sparo non valido: giocatori visibili!" + ANSI_RESET.escape();

    public static final String DEFAULT_UNCORRECT_EFFECTS = DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "[!] Sparo non valido: Scelta effetti non corretta!" + ANSI_RESET.escape();

    public static final String DEFAULT_NOT_CORRECT_PLAYER_NUMBER = DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "[!] Sparo non valido: Numero di giocatori non corretto!" + ANSI_RESET.escape();

    public static final String DEFAULT_PLAYER_NOT_SEEABLE = DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "[!] Sparo non valido: giocatori non visibili! " + ANSI_RESET.escape();

    public static final String DEFAULT_NO_TARGETS_SPECIFIED = DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "[!] Sparo non valido: Non hai selezionato i bersagli!" + ANSI_RESET.escape();

    public static final String DEFAULT_INEXISTENT_TARGETS =  DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "[!] Sparo non valido: Bersaglio non esistente!" + ANSI_RESET.escape();

    public static final String DEFAULT_DIFFERENT_PLAYER_NEEDED = DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "[!] Sparo non valido: servono bersagli differenti!" + ANSI_RESET.escape();

    public static final String DEFAULT_PRECEDENT_PLAYER_NEEDED = DEFAULT_INVALID_SHOOT_HEADER +
            ANSI_YELLOW.escape() + "[!] Sparo non valido: Il target dell'effetto deve essere lo stesso del precendete!" + ANSI_RESET.escape();

    public static final String DEFAULT_PLAYER_ALREADY_DEAD = ANSI_YELLOW.escape() +
            "[!] Sparo non valido: Uno dei giocatori è già morto, non puoi sparargli!" + ANSI_RESET.escape();

    public static final String DEFAULT_TARGETING_SCOPE_ON_NON_TARGETED_PLAYER = ANSI_YELLOW.escape() +
            "[!] Puoi usare mirino solo su uno dei bersagli dello sparo!" + ANSI_RESET.escape();

    public static final String DEFAULT_RECEIVED_FRENZY_BUT_EXPECTED_NORMAL = "RECEIVED_FRENZY_BUT_EXPECTED_NORMAL";
    public static final String DEFAULT_RECEIVED_NORMAL_BUT_EXPECTED_FRENZY = "RECEIVED_NORMAL_BUT_EXPECTED_FRENZY";

    public static final String DEFAULT_NO_ENOUGH_AMMO = ANSI_YELLOW.escape() +
            "[!] Non hai abbastanza munizioni!" + ANSI_RESET.escape();

    public static final String DEFAULT_WEAPON_ALREADY_LOADED = ANSI_YELLOW.escape() +
            "[!] Arma già carica!" + ANSI_RESET.escape();

    //timer expires
    public static final String DEFAULT_TIMER_EXPIRED = ANSI_YELLOW.escape() + "[!] Tempo scaduto! Verrai disconnesso" + ANSI_RESET.escape();
}
