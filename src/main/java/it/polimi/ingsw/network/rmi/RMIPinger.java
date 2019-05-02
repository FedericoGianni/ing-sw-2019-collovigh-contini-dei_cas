package it.polimi.ingsw.network.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;

public class RMIPinger implements Runnable{

    private static final String REMOTENAME = "rmi_client";
    private static String host;

    private Registry remote;

    public RMIPinger() {

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

            ToClientImpl client = (ToClientImpl) remote.lookup(REMOTENAME);
            Boolean b;
            do{

                b = client.ping();

            }while (b);

        }catch (Exception e){

            e.printStackTrace();
        }

    }
}
