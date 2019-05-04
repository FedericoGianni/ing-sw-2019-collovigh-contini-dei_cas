package it.polimi.ingsw.network.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;

public class RMIPinger implements Runnable{

    private final String remoteName;
    private final static int waitTime = 1000;
    private static String host;


    private Registry remote;

    public RMIPinger(String name) {

        this.remoteName = name;

        try {

            remote = LocateRegistry.getRegistry(host, 2020);

        }catch (Exception e){

            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        try {

            remote = LocateRegistry.getRegistry(host, 2020);

            ToClientImpl client = (ToClientImpl) remote.lookup(remoteName);
            Boolean b;
            do{

                Thread.sleep(waitTime);

                b = client.ping();

            }while (b);

        }catch (Exception e){

            e.printStackTrace();
        }

    }
}
