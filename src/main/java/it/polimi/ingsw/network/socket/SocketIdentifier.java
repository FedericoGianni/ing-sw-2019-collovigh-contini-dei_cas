package it.polimi.ingsw.network.socket;

/**
 * This class is used to store a single socket connection identifier, which can be distinguished by the pair ip, port
 */
public class SocketIdentifier {

    private final String address;
    private final int port;

    public SocketIdentifier(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
