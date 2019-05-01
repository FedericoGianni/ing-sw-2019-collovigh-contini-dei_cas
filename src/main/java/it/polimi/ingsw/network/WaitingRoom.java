package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WaitingRoom {

    private static final int TIMER = 30;
    private static int timerCount = TIMER;

    List<String> players;
    List<PlayerColor> colors;
    List<Controller> games = new ArrayList<>();
    Boolean active;

    public WaitingRoom() {

        this.active = true;
        this.colors = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public Boolean isActive(){

        return this.active;
    }

    public synchronized void initGame(){
        
        int size = games.size();
        this.games.add(new Controller(players,colors,size));
        this.active = false;
    }

    /**
     *
     * @param name is the name of the new player
     * @param playerColor is the color of the new player
     * @return id of the player if name and color or '-1' if those are already taken
     */
    public synchronized int addPlayer(String name, PlayerColor playerColor){

        if (isNameAlreadyTaken(name)) {

            return -1;
        }

        if (isColorAlreadyTaken(playerColor)){

            return -1;
        }


        players.add(name);

        colors.add(playerColor);

        return players.indexOf(name);

    }

    /**
     *
     * @param name is the name to check
     * @return true if the name is free
     */
    public Boolean isNameAlreadyTaken(String name){

        return (players.contains(name));
    }

    /**
     *
     * @param color is the color to check
     * @return true if the color is free
     */
    public Boolean isColorAlreadyTaken(PlayerColor color){

        return (colors.contains(color));
    }

    /**
     *
     * @param id is the id of the player to remove
     */
    public synchronized void removePlayer(int id){

        players.remove(id);
        colors.remove(id);
    }

    public synchronized void restore(){

        //TODO
    }

    /**
     * This method will start a thread which runs a timer, decrementing a counter every second until it reaches 0
     */
    public void startTimer(){

        final TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                System.out.println("[DEBUG] WaitingRoomTimer counter: " + timerCount);
                timerCount--;
                if(timerCount == 0) {
                    System.out.println("[DEBUG] Timer has expired!");
                    //chiama initGame o gestire timer scaduto
                    //initGame();

                    //termina l'esecuzione del thread
                    this.cancel();
                }
            }
        };

        Timer timer = new Timer("WaitingRoomTimer");
        timer.scheduleAtFixedRate(timerTask, 30, 1000);

    }


}
