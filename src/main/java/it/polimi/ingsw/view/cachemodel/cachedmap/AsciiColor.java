package it.polimi.ingsw.view.cachemodel.cachedmap;

/**
 * ANSI color to have a nicer CLI
 */
public enum AsciiColor {
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_PURPLE ("\u001B[35m"),
    ANSI_CYAN ("\u001B[36m"),
    ANSI_WHITE ("\u001B[37m");

    static final String RESET = "\u001B[0m";

    private String escape;

    AsciiColor(String escape) {
        this.escape = escape;
    }
    public String escape(){
        return escape;
    }

}