package it.polimi.ingsw.network.jsonconfig;

import java.io.Serializable;

public class Config implements Serializable {

    private final String serverIp;

    // Socket

    private final int socketServerPort;
    private final int socketClientPort;

    // RMI

    private final int rmiServerPort = 2020;
    private final int rmiClientPort = 2021;
    private final String rmiServerName = "rmi_server";

    // UI

    private final Boolean gui;

    public Config(String serverIp, int socketServerPort, int socketClientPort,  Boolean gui) {
        this.serverIp = serverIp;
        this.socketServerPort = socketServerPort;
        this.socketClientPort = socketClientPort;
        this.gui = gui;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getSocketServerPort() {
        return socketServerPort;
    }

    public int getSocketClientPort() {
        return socketClientPort;
    }

    public int getRmiServerPort() {
        return rmiServerPort;
    }

    public int getRmiClientPort() {
        return rmiClientPort;
    }

    public String getRmiServerName() {
        return rmiServerName;
    }

    public String getGui() {
        return gui?"-gui":"-cli";
    }
}
