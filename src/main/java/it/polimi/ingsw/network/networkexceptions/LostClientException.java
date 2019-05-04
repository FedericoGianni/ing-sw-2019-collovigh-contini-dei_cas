package it.polimi.ingsw.network.networkexceptions;

public class LostClientException extends Exception{

    private final int pid;

    public LostClientException(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }
}
