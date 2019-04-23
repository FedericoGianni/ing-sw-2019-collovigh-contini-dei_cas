package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.PlayerColor;

public class ClientController {

    private int gameId;
    private int myPlayerId;
    private PlayerColor myColor;
    private int roundNumber;
    /**
     * this field is meant to describe the position in the single turn
     */
    private int sync;
    private int actionCount;
    private Boolean frenzy;
    private int frenzyStarter;

}
