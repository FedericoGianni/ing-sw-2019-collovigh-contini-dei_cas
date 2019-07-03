package it.polimi.ingsw.network;

import java.io.Serializable;

public class Config implements Serializable {

    private static final int DEFAULT_MIN_PLAYER = 3;
    private static final int DEFAULT_LOBBY_TIMER = 10;

    private final String serverIp;

    // Socket

    private final int socketPort;

    // RMI

    private final int rmiServerPort;
    private final int rmiClientPort;

    // UI

    private final String gui;

    // controller

    private final int lobbyTimer;

    private final int minPlayer;

    /**
     *  constructor for server w/ no rmi ports
     *
     * @param socketPort is the socket port
     */
    public Config( int socketPort) {

        // gets the local ip

        this.serverIp = "localhost";

        // sets the socketPort

        this.socketPort = socketPort;

        // sets gui to null but the server don't mind

        this.gui = null;

        // sets values to default

        this.rmiClientPort = 22221;

        this.rmiServerPort = 22220;

        this.lobbyTimer = DEFAULT_LOBBY_TIMER;

        this.minPlayer = DEFAULT_MIN_PLAYER;

    }

    /**
     *  constructor for server w/ rmi ports
     *
     * @param socketPort is the socket port
     */
    public Config( int socketPort, int rmiServerPort, int rmiClientPort) {

        // gets the local ip

        this.serverIp = "localhost";

        // sets the socketPort

        this.socketPort = socketPort;

        // sets gui to false but the server don't mind

        this.gui = null;

        // sets the rmi ports

        this.rmiServerPort = rmiServerPort;

        this.rmiClientPort = rmiClientPort;

        this.lobbyTimer = DEFAULT_LOBBY_TIMER;

        this.minPlayer = DEFAULT_MIN_PLAYER;
    }

    /**
     *  Constructor for client w/ no rmi parameters
     *
     * @param serverIp is the ip address of the server
     * @param socketPort is the socket port
     * @param gui is a string: "-gui" if the player wants gui or "-cli" if wants cli
     */
    public Config(String serverIp, int socketPort,  String gui) {

        // sets the serverIp

        this.serverIp = serverIp;

        // sets the socketPort

        this.socketPort = socketPort;

        this.gui = gui;

        // sets values to default

        this.rmiClientPort = 22221;

        this.rmiServerPort = 22220;

        this.lobbyTimer = DEFAULT_LOBBY_TIMER;

        this.minPlayer = DEFAULT_MIN_PLAYER;
    }

    /**
     *  Constructor for client w/ no rmi parameters
     *
     * @param serverIp is the ip address of the server
     * @param socketPort is the socket port
     * @param gui is a string: "-gui" if the player wants gui or "-cli" if wants cli
     * @param rmiClientPort is the port used by the rmi client registry
     * @param rmiServerPort is the port used by the rmi server registry
     */
    public Config(String serverIp, int socketPort,  String gui, int rmiServerPort, int rmiClientPort) {

        // sets the serverIp

        this.serverIp = serverIp;

        // sets the socketPort

        this.socketPort = socketPort;

        this.gui = gui;

        // sets rmi values

        this.rmiClientPort = rmiClientPort;

        this.rmiServerPort = rmiServerPort;

        this.lobbyTimer = DEFAULT_LOBBY_TIMER;

        this.minPlayer = DEFAULT_MIN_PLAYER;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getRmiServerPort() {
        return rmiServerPort;
    }

    public int getRmiClientPort() {
        return rmiClientPort;
    }

    public String getGui() {
        return gui;
    }

    public int getLobbyTimer() {
        return lobbyTimer;
    }

    public int getMinPlayer() {
        return minPlayer;
    }
}
