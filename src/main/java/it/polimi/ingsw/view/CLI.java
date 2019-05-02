package it.polimi.ingsw.view;

import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.network.socket.SocketClientReader;
import it.polimi.ingsw.network.socket.SocketClientWriter;

import java.util.Scanner;

public class CLI implements UserInterface {

    private final Scanner scanner = new Scanner(System.in);
    private SocketClientReader socketClientReader;
    private SocketClientWriter socketClientWriter;

    public CLI(SocketClient s) {
        this.socketClientReader = s.getScr();
        this.socketClientWriter = s.getScw();
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

        System.out.println("PlayerName:  " + playerName);

        System.out.println("PlayerColor: " + playerColor);

        socketClientWriter.send("login" + "\t" + playerName + "\t" + playerColor);
    }

    public static void show(String s){
        new Thread(() -> {
            System.out.println(s);
        }).start();
    }

    @Override
    public void retryLogin() {

    }
}
