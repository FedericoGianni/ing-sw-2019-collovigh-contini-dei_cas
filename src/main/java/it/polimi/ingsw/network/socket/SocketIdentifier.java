package it.polimi.ingsw.network.socket;

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
