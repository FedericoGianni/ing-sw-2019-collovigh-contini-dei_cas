package it.polimi.ingsw.utils;

import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.ANSI_RESET;
import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.ANSI_YELLOW;

public class DefaultReplies {

    public static final String DEFAULT_CANNOT_BUY_WEAPON = ANSI_YELLOW.escape() +
            "[!] Non hai abbastanza munizioni per comprare l'arma!" + ANSI_RESET.escape();

    public static final String DEFAULT_ALREADY_PICKED_AMMO_HERE = ANSI_YELLOW.escape() +
            "[!] Hai già raccolto in questa cella! Non ci sono piu munizioni." + ANSI_RESET.escape();
}
