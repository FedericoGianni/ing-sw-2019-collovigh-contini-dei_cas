package it.polimi.ingsw.controller;

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
