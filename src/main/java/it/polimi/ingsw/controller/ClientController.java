package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.PlayerColor;

public class ClientController {

    private int gameId;
    private final int myPlayerId;
    private int roundNumber;
    private Boolean frenzy;
    private final Controller controller;

    public ClientController(int myPlayerId, Controller controller) {
        this.myPlayerId = myPlayerId;
        this.controller = controller;
    }




}
