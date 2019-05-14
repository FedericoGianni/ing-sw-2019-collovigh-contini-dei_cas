package it.polimi.ingsw.view;

import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.network.socket.SocketClientReader;
import it.polimi.ingsw.network.socket.SocketClientWriter;
import it.polimi.ingsw.view.cacheModel.CacheModel;
import it.polimi.ingsw.view.cacheModel.CachedPowerUp;

import java.util.List;
import java.util.Scanner;

public class CLI implements UserInterface {

    private final Scanner scanner = new Scanner(System.in);
    private final View view;
    private SocketClientReader socketClientReader;
    private SocketClientWriter socketClientWriter;

    public CLI(View view) {
        this.view = view;
    }

    public CLI(SocketClient s) {
        this.socketClientReader = s.getScr();
        this.socketClientWriter = s.getScw();
        this.view = null;
    }

    @Override
    public void login() {

        String playerName;
        String playerColor;
        boolean validColorChoice = false;

        System.out.println("Enter player name: ");
        playerName = scanner.nextLine();



        //socketClientWriter.send("check" + "\t" + playerName);

        System.out.println("Choose a player color (GREEN, GREY, PURPLE, BLUE,YELLOW): ");

        do {
            playerColor = scanner.nextLine();

            if(playerColor.toUpperCase().equals("GREEN") || playerColor.toUpperCase().equals("GREY") ||
                    playerColor.toUpperCase().equals("YELLOW") || playerColor.toUpperCase().equals("PURPLE") ||
                    playerColor.toUpperCase().equals("BLUE")) {
                validColorChoice = true;
            } else {
                System.out.println("Please enter a valid color choice: ");
            }

        }while(!validColorChoice);

        System.out.println("[DEBUG] PlayerName:  " + playerName);
        System.out.println("[DEBUG] PlayerColor: " + playerColor);

        socketClientWriter.send("login" + "\f" + playerName + "\f" + playerColor);

    }

    public static void show(String s){
        new Thread(() -> {
            System.out.println(s);
        }).start();
    }

    @Override
    public void retryLogin() {
        System.out.println("Login failed. Retry ");
        login();
    }

    @Override
    public void startPhase0() {


        System.out.println("[DEBUG] START PHASE 0 test");

        List<CachedPowerUp> powerUps;

        Boolean validChoice = false;
        int read;


        do{

            powerUps = CacheModel
                    .getCachedPlayers()
                    .get(view.getPlayerId())
                    .getPowerUpBag()
                    .getPowerUpList();

            System.out.println("Hai questi PowerUp:");

            for (int i = 0; i < powerUps.size(); i++) {

                System.out.println( i + powerUps.get(i).toString());

            }

            System.out.println("scegli un powerUp da scartare: ");
            read = scanner.nextInt();

            if (read >= 0 && read < powerUps.size()) validChoice = true;


        }while(!validChoice);

        view.spawn(powerUps.get(read));

    }
}
