package it.polimi.ingsw.network.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

public class TEST {

    public static void main(String[] args) {

        String msg = "{\"type\":\"POWERUP_BAG\",\"update\":{\"powerUpList\":[{\"color\":\"BLUE\",\"type\":\"NEWTON\"},{\"color\":\"RED\",\"type\":\"TAG_BACK_GRENADE\"}]},\"playerId\":0}";
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

        switch (type){

            case "POW" :

                Gson gson = new Gson();

                CachedPowerUpBag cachedPowerUpBag = gson.fromJson(update,CachedPowerUpBag.class);

                updateClass = new UpdateClass(UpdateType.POWERUP_BAG, cachedPowerUpBag, playerId);


        }

        System.out.println("UPDATE_CLASS: " + updateClass);
        System.out.println("[DEBUG] [CLIENT] Created Update class from Json received. ");
        //RunClient.getView().sendUpdates(update);
    }
}
