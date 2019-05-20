package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToViewImpl implements ToView {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    //real attributes

    private final ToClient client;
    private final String ipAddress;
    private final String remoteName;

    public ToViewImpl(String ipAddress, String remoteName, ToClient client ) {
        this.client = client;
        this.ipAddress = ipAddress;
        this.remoteName = remoteName;
    }


    // Updates


    @Override
    public void sendUpdate(UpdateClass update) {

        try {

            client.sendUpdate(update);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }

    }


    // Game Handling


    @Override
    public void startSpawn() {

        try {

            LOGGER.log(level, "[RMI-ToView] forwarding startSpawn()");

            client.startSpawn();

            LOGGER.log(level,"[RMI-SERVER] sent startSpawn to player with address :{0}", ipAddress);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }

    }

    @Override
    public void startPowerUp() {

        try {

            client.startPowerUp();

            LOGGER.log(level,"[RMI-SERVER] sent startPowerUp to player with address :{0}", ipAddress);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }

    }

    @Override
    public void startAction() {

        try {

            client.startAction();

            LOGGER.log(level,"[RMI-SERVER] sent startAction to player with address :{0}", ipAddress);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }

    }

    @Override
    public void startReload() {

        try {

            client.startReload();

            LOGGER.log(level,"[RMI-SERVER] sent startReload to player with address :{0}", ipAddress);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }

    }

    @Override
    public void useGrenade() {

        try {

            client.useGrenade();

            LOGGER.log(level,"[RMI-SERVER] sent useGrenade to player with address :{0}", ipAddress);

        }catch (RemoteException e){
            LOGGER.log(Level.WARNING, e.getMessage(),e);
        }

    }
}
