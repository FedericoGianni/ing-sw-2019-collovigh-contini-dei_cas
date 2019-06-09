package it.polimi.ingsw.network.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.PowerUpType;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.GameAlreadyStartedException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.OverMaxPlayerException;
import it.polimi.ingsw.utils.Protocol;
import it.polimi.ingsw.view.actions.*;
import it.polimi.ingsw.view.actions.usepowerup.GrenadeAction;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.actions.usepowerup.ScopeAction;
import it.polimi.ingsw.view.actions.usepowerup.TeleporterAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

/**
 * This class handles the socket input stream server-side. A new SocketConnectionReader thread is started for every
 * Socket connection to be handled. This class specifically handles the messages coming from client side.
 * As in SocketClientReader class, the SocketConnectionReader start a SocketConnectionWriter thread to handle
 * output stream to the client and then keep listening for String messages, splits them every time it encounters
 * a special character ('\f'), look for the function linked to the header String and uses the other String as
 * function parameters and execute the function in another thread.
 */
public class SocketConnectionReader extends Thread {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");

    private static Level level = INFO;

    /**
     * Reference to the socket to be handled, which is passed as a parameter to the constructor
     */
    private Socket socket;

    /**
     * Reference to the SocketConnectionWriter handling the output stream for the same server socket
     */
    private SocketConnectionWriter socketConnectionWriter;

    /**
     * Used to store the splitted message received from Client and then process it, depending on its header
     */
    private String[] commands;

    /**
     * Attribute representing a BufferedReader to manage input stream from socket
     */
    BufferedReader input;

    /**
     * Map which relates each string message which can be received to a function
     */
    private Map<String, FunctionInterface> headersMap;

    /**
     * Constructor
     * @param socket represent the channel to communicate between client and server, which is created by the
     *               Server when a client tries to connect to the SocketServer
     */
    public SocketConnectionReader(Socket socket) {
        this.socket = socket;
    }

    /**
     *
     * @return a reference to the socketConnectionWriter which is created at the beginning of run() method
     */
    public SocketConnectionWriter getSocketConnectionWriter() {
        return socketConnectionWriter;
    }

    int id;

    /**
     * Initialize the SocketConnectionReader, creates a new SocketConnectionWriter thread and runs it, populate the
     * headersMap containing a link between String headers and functions, and finally enters an infinite loop
     * in which the thread keep listening for incoming messages, splits them as said above class declaration,
     * look for the related function and execute it in another separate thread.
     */
    @Override
        public void run() {
            LOGGER.log(INFO, "Received client connection.");

            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                socketConnectionWriter = new SocketConnectionWriter(socket);
                socketConnectionWriter.start();
                /*try {
                    socketConnectionWriter.await();
                } catch(InterruptedException e){
                    e.getMessage();
                }*/


                populateHeadersMap();

                while(true) {

                    String msg = input.readLine();
                    if(msg != null) {

                        if (msg.startsWith("{")){

                            // if the string starts with a '{' read the message as a json

                            handleJson(msg);

                        }else {

                            // otherwise the string is passed to the hashMap

                            handleMsg(splitCommand(msg));

                        }
                    }
                }


            } catch(IOException e) {
                    e.getMessage();
            } finally {
                try {
                    socket.close();
                } catch(IOException e) {
                    e.getMessage();
                }
            }

        }

    /**
     *
     * @param msg String to be splitted
     * @return an array of Strings splitted by the special character '\f'
     */
    private String[] splitCommand(String msg) {
        if(msg == null){
            return null;
        }
        return msg.split("\f");
    }

    /**
     * Handle the array of Strings previously splitted, look for its corresponding function in the headersMap,
     * runs it in a separate thread
     * @param message the result of splitCommand
     */
    private void handleMsg(String[] message) {

        this.commands = message;
        FunctionInterface function = headersMap.get(message[0]);

        if (function == null)
            LOGGER.log(WARNING,  "[DEBUG] [SERVER] ERRORE nella lettura della funzione dalla hashmap!");
        else
            try {
                new Thread (() -> {
                    function.execute(commands);
                }).start();
            } catch(NumberFormatException e) {
               LOGGER.log(WARNING, "[DEBUG] [SERVER] ERRORE nel formato del messaggio socket ricevuto!");
            }
    }


    /**
     * Initialize the Map by binding a String to its related function
     * @throws NumberFormatException
     */

    private void populateHeadersMap() throws NumberFormatException {

        headersMap = new HashMap<>();

        // login username
        headersMap.put("login", (commands) -> {
            try {

                String address = socket.getInetAddress().toString();
                int port = socket.getPort();

                int key = -3;

                SocketIdentifier s = SocketServer
                        .getClients()
                        .values()
                        .stream()
                        .filter( c -> c.getAddress().equals(address) && c.getPort() == port)
                        .collect(Collectors.toList())
                        .get(0);

                for (int i = 0; i < SocketServer.getClients().size(); i++) {

                    if (SocketServer.getClients().get(i) == s) key = i;
                    
                }


                id = Server.addPlayer(commands[1], PlayerColor.valueOf(commands[2].toUpperCase()), socketConnectionWriter);
                if(id >= 0){
                    socketConnectionWriter.send(commands[0] + "\f" + Protocol.DEFAULT_LOGIN_OK_REPLY + "\f"+id);
                }


            }catch (NameAlreadyTakenException e){       //NOTE: temporary solution by D, just to make it compile
                socketConnectionWriter.send(commands[0] + "\f" + Protocol.DEFAULT_NAME_ALREADY_TAKEN_REPLY);
            }catch (ColorAlreadyTakenException e){
                socketConnectionWriter.send(commands[0] + "\f" + Protocol.DEFAULT_COLOR_ALREADY_TAKEN_REPLY);
            }catch (OverMaxPlayerException e){
                socketConnectionWriter.send(commands[0] + "\f" + Protocol.DEFAULT_MAX_PLAYER_READCHED);
            }catch (GameAlreadyStartedException e){
                socketConnectionWriter.send(commands[0] + "\f" + Protocol.DEFAULT_GAME_ALREADY_STARTED_REPLY);
            }
        });

        //ping
        headersMap.put("pong", (commands) -> {
            LOGGER.log(INFO, "Client reply to ping: " + commands[1]);
        });

        //spawn
        headersMap.put("spawn", (commands) -> {
            LOGGER.log(INFO, "received spawn from client");
            Server.getController().getVirtualView(id).spawn(new CachedPowerUp(PowerUpType.valueOf(commands[1]), Color.valueOf(commands[2])));
        });

        //askMoveValid
        headersMap.put("askMoveValid", (commands) -> {
           LOGGER.log(INFO, "received askValidDirection from client");
           Boolean valid = Server.getController().getVirtualView(id).askMoveValid(Integer.valueOf(commands[1]), Integer.valueOf(commands[2]), Directions.valueOf(commands[3]));

           if(valid){
               socketConnectionWriter.send(commands[0] + "\ftrue");
           } else{
               socketConnectionWriter.send(commands[0] + "\ffalse");
           }
        });
    }


    private void handleJson(String message){

        // get the type of the class contained in the UpdateClass

        String[] values = message.split("," );

        // LOG the update

        LOGGER.log(level, "[DEBUG] [SOCKET-CONN-READER] Received Json {0} : Calling handleJson method. ", values[values.length - 1 ]);

        JsonAction jsonAction = null;
        Gson gson = new Gson();

        switch (values[values.length - 1]){

            case "\"actionType\":\"POWER_UP\"}":

                switch (values[values.length - 2]){

                    case "\"powerUpType\":\"NEWTON\"":

                        jsonAction = gson.fromJson(message, NewtonAction.class);

                        break;

                    case "\"powerUpType\":\"TELEPORTER\"":

                        jsonAction = gson.fromJson(message, TeleporterAction.class);

                        break;

                    case "\"powerUpType\":\"TAG_BACK_GRENADE\"":

                        jsonAction = gson.fromJson(message, GrenadeAction.class);

                        break;

                    case "\"powerUpType\":\"TARGETING_SCOPE\"":

                        jsonAction = gson.fromJson(message, ScopeAction.class);

                        break;

                    default:

                        LOGGER.log(WARNING,"[SOCKET-CONN-READER] unknown PowerUp Type ");

                        break;
                }

                break;

            case "\"actionType\":\"MOVE\"}":

                jsonAction = gson.fromJson(message, Move.class);

                break;

            case "\"actionType\":\"GRAB\"}":

                jsonAction = gson.fromJson(message, GrabAction.class);

                break;

            case "\"actionType\":\"SHOOT\"}":

                jsonAction = gson.fromJson(message, ShootAction.class);

                break;

            case "{\"actionType\":\"SKIP\"}":

                jsonAction = gson.fromJson(message, SkipAction.class);

                break;

            case "{\"actionType\":\"FRENZY_MOVE\"}":

                jsonAction = gson.fromJson(message,FrenzyMove.class);

                break;

            case "{\"actionType\":\"FRENZY_GRAB\"}":

                jsonAction = gson.fromJson(message,FrenzyGrab.class);

                break;

            case "{\"actionType\":\"FRENZY_SHOOT\"}":

                jsonAction = gson.fromJson(message,FrenzyShoot.class);

                break;

            default:

                LOGGER.log(WARNING,"[SOCKET-CONN-READER] Unknown Action Type ");

                break;
        }

        LOGGER.log(level,"[SOCKET-CONN-READER] Forwarding Action to controller : {0}  ", jsonAction.getType() );

        Server.getController().doAction(jsonAction);
    }

}




