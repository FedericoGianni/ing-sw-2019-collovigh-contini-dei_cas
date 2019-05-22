package it.polimi.ingsw.network.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.updates.InitialUpdate;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

public class TEST {

    public static void main(String[] args) {

        String msg = "{\"type\":\"INITIAL\",\"update\":{\"names\":[\"Alex\",\"Bob\"],\"colors\":[\"BLUE\",\"YELLOW\"],\"mapType\":0,\"gameId\":-1},\"playerId\":0}";
        handleJson(msg);
    }

    public static void handleJson(String msg){

        // get the type of the class contained in the UpdateClass

        String type =msg.substring(9,12);

        // LOG the update

        System.out.println("[DEBUG] [CLIENT] Received Json. Calling handleJson method. ");

        // gets the json of the class contained (GSON can not detect the class)

        String update = msg.substring(msg.indexOf("\"update\":") + 9 , msg.indexOf(",\"playerId\""));

        // gets the playerId attribute

        int playerId = Integer.parseInt(msg.substring(msg.lastIndexOf(":") + 1, msg.length() - 1));

        // creates a new UpdateClass variable ( will be instantiated in the switch )

        UpdateClass updateClass = null;

        // switchCase on the type

        Gson gson;

        switch (type){

            case "POW" :

                gson = new Gson();

                CachedPowerUpBag cachedPowerUpBag = gson.fromJson(update,CachedPowerUpBag.class);

                updateClass = new UpdateClass(UpdateType.POWERUP_BAG, cachedPowerUpBag, playerId);

                break;

            case "INI" :

                gson = new Gson();

                // gets the inner class from the "update" json string

                InitialUpdate initialUpdate = gson.fromJson(update,InitialUpdate.class);

                // creates a new UpdateClass from the obtained parameters

                updateClass = new UpdateClass(UpdateType.INITIAL,initialUpdate,playerId);

                break;
        }

        System.out.println("UPDATE_CLASS: " + updateClass + '\n' + updateClass.getType() );
        System.out.println("[DEBUG] [CLIENT] Created Update class from Json received. ");
        //RunClient.getView().sendUpdates(update);
    }
}
