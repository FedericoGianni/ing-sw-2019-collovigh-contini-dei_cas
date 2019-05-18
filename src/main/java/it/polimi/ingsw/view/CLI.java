package it.polimi.ingsw.view;

import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.network.socket.SocketClientWriter;
import it.polimi.ingsw.view.cachemodel.CacheModel;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.util.List;
import java.util.Scanner;

public class CLI implements UserInterface {

    private final Scanner scanner = new Scanner(System.in);
    private final View view;
    //private SocketClientReader socketClientReader;
    private SocketClientWriter socketClientWriter;

    public CLI(View view, SocketClient s) {
        this.view = view;
        this.socketClientWriter = s.getScw();
    }

    /*
    public CLI(SocketClient s) {
        this.socketClientReader = s.getScr();
        this.socketClientWriter = s.getScw();
        this.view = null;
    }*/

    @Override
    public void login() {

        String playerName;
        String playerColor;
        boolean validColorChoice = false;

        System.out.println("Enter player name: ");
        playerName = scanner.nextLine();

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
    public void startSpawn() {

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

    @Override
    public void startPowerUp(){
        System.out.println("[DEBUG] startPowerUp1");


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

            System.out.println("Scegli un powerUp da usare: ");
            read = scanner.nextInt();

            if (read >= 0 && read < powerUps.size()) validChoice = true;

        }while(!validChoice);


        int player;
        String direction;
        int r,c;

        switch (powerUps.get(1).getType()){

            case NEWTON:
                do {
                    System.out.println("Su quale giocatore vuoi usare Newton? >>> ");
                    player = scanner.nextInt();

                    if(player >= 0 && player <= CacheModel.getCachedPlayers().size())
                        validChoice = true;
                    else
                        System.out.println("Scelta non valida. Riprova.");
                }while(!validChoice);

                do{
                    System.out.println("In quale direzione vuoi spostare il player selezionato? (Nord, Sud, Ovest, Est) >>> ");
                    direction = scanner.next().toUpperCase();

                    if(direction.equals("NORD") || direction.equals("SUD")
                            || direction.equals("EST") || direction.equals("OVEST"))
                        validChoice = true;
                    else
                        System.out.println("Scelta non valida. Riprova.");
                }while(!validChoice);
                //TODO check amount parameter what's for and why user should choose it?
                view.useNewton(powerUps.get(read).getColor(), player, Directions.valueOf(direction), 0);
                break;

            case TELEPORTER:
                do{
                    System.out.println("Su quale giocatore vuoi usare Teleporter? >>> ");
                    player = scanner.nextInt();

                    if(player >= 0 && player <= CacheModel.getCachedPlayers().size())
                        validChoice = true;
                    else
                        System.out.println("Scelta non valida. Riprova.");

                }while(!validChoice);

                do{
                    System.out.println("In quale cella vuoi spostare il giocatore selezionato? >>> ");
                    //TODO when the cachedModel will have Map find a better method to get this cell
                    //this way you have to check locally if r,c is ok for every different type of map
                    System.out.println("riga >>> (0,1,2)");
                    r = scanner.nextInt();
                    System.out.println("colonna >>> (0,1,2,3)");
                    c = scanner.nextInt();

                    if(r >= 0 && r <= 2 && c >= 0 && c <= 3){
                        validChoice = true;
                    } else {
                        System.out.println("Cella non valida. Riprova.");
                    }

                }while(!validChoice);

                view.useTeleport(powerUps.get(read).getColor(), r,c);
                break;

            case TAG_BACK_GRENADE:
                break;

            case TARGETING_SCOPE:
                break;

        }

    }

    @Override
    public void startAction() {
        //TODO first action handling
    }

    @Override
    public void startReload() {
        Boolean validChoice = false;
        int read;


        do{
            System.out.println("Vuoi ricaricare? Le tue armi sono: ");
            //TODO show weapons -> need cachedWeapons
            System.out.println("Seleziona l'arma che vuoi ricaricare: >>> ");
            read = scanner.nextInt();

            //TODO check if weapon index is ok
            //if(read) validChoice = true;

        }while(!validChoice);
    }
}
