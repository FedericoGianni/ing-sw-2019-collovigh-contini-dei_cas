package it.polimi.ingsw.network;

import java.io.Serializable;

/**
 * This class is used to save/ load network startup config
 */
public class Config implements Serializable {

    /**
     * default min players
     */
    private static final int DEFAULT_MIN_PLAYER = 3;
    /**
     * default lobby timer
     */
    private static final int DEFAULT_LOBBY_TIMER = 10;

    /**
     * ip of the server
     */
    private final String serverIp;

    // Socket

    /**
     * socket port
     */
    private final int socketPort;

    // RMI

    /**
     * rmi server registry port
     */
    private final int rmiServerPort;
    /**
     * rmi registry client port
     */
    private final int rmiClientPort;

    // UI

    /**
     * ui selection ( -cli for cli, -gui for gui )
     */
    private final String gui;

    // controller

    /**
     * timer for the lobby
     */
    private final int lobbyTimer;

    /**
     * min player for the lobby to start
     */
    private final int minPlayer;

    /**
     * game id
     */
    private final int game;

    /**
     * set the timer to enable or disabled
     */
    private final boolean enableTimer;

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

        this.game = -1;

        this.enableTimer = true;

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

        this.game = -1;

        this.enableTimer = true;

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

        this.game = -1;

        this.enableTimer = true;

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

        this.game = -1;

        this.enableTimer = true;

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

    public int getGame() { return game; }

    public boolean isEnableTimer() {
        return enableTimer;
    }
}
 
