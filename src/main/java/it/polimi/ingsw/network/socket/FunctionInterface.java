package it.polimi.ingsw.network.socket;

/**
 * Interface needed for headersMap implementation in SocketClientReader and SocketConnectionReader
 */
public interface FunctionInterface {

    void execute(String[] commands);
}
