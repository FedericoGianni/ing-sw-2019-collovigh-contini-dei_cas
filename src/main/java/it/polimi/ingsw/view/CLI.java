package it.polimi.ingsw.view;

import it.polimi.ingsw.customsexceptions.InvalidMapTypeException;
import it.polimi.ingsw.network.ProtocolType;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.utils.Protocol;
import it.polimi.ingsw.view.actions.*;
import it.polimi.ingsw.view.actions.usepowerup.GrenadeAction;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.actions.usepowerup.ScopeAction;
import it.polimi.ingsw.view.actions.usepowerup.TeleporterAction;
import it.polimi.ingsw.view.cachemodel.CachedFullWeapon;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.cachemodel.EffectType;
import it.polimi.ingsw.view.cachemodel.Player;
import it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;
import it.polimi.ingsw.view.cachemodel.cachedmap.FileRead;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoCell;
import it.polimi.ingsw.view.cachemodel.sendables.CachedSpawnCell;
import it.polimi.ingsw.view.exceptions.WeaponNotFoundException;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.updates.otherplayerturn.*;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.map.JsonMap.MAP_C;
import static it.polimi.ingsw.model.map.JsonMap.MAP_R;
import static it.polimi.ingsw.utils.DefaultReplies.*;
import static it.polimi.ingsw.utils.PowerUpType.TAG_BACK_GRENADE;
import static it.polimi.ingsw.utils.PowerUpType.TARGETING_SCOPE;
import static it.polimi.ingsw.view.UiHelpers.*;
import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.*;
import static java.lang.Thread.sleep;

public class CLI implements UserInterface {

    /**
     * default Scanner to get input from user
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * reference to the view linked to this UserInterface
     */
    private final View view;

    /**
     * used to ask the server if the specified direction is valid (= no walls), since the cacheModel version
     * doesn't store the cell adjacences we need to ask the server if a direction is valid or not
     */
    private int validMove = -1;

    /**
     * Object to synchronize System.out.println() with map, without interferences
     */
    private Object mapShowSync = new Object();

    /**
     * true if the action called on the UI by the controller is on frenzy phase
     */
    boolean isFrenzy = false;

    /**
     * used to store a previous version of online status, to check if user is reconnecting or the stats updates are the
     * same as before
     */
    private List<Boolean> wasOnline = new ArrayList<>(Collections.nCopies(5, true));

    /**
     * used to store a previous version of dmgs and marks, to check, when you receive a STATS update, if it has
     * changed something from the previous version
     */
    int previousDmg = 0, previousMarks = 0;

    /**
     * Default constructor
     *
     * @param view is the view
     */
    public CLI(View view) {
        this.view = view;
        //this.socketClientWriter = s.getScw();
    }

    /**
     * {@inheritDoc}
     */
    public void setValidMove(boolean validMove) {
        if (validMove) {
            this.validMove = 1;
        } else {
            this.validMove = 0;
        }
        synchronized (this) {
            this.notifyAll();
        }
    }


    // start Ui methods

    /**
     * {@inheritDoc}
     */
    @Override
    public void startUI() {

        int connectionType = -1;

        FileRead.showWelcome();

        System.out.println("\nCiao! \n Benvenuto in ADRENALINA");

        do {

            System.out.println("Digita: \n 1 per connetterti al server con SOCKET \n 2 per farlo con RMI");

            try {
                connectionType = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Valore non valido. Riprova!");
            }

            scanner.nextLine();

        } while (!(connectionType == 1 || connectionType == 2));

        switch (connectionType) {

            case 1:
                chooseNetProtocol(ProtocolType.SOCKET);
                break;

            case 2:
                chooseNetProtocol(ProtocolType.RMI);
                break;

            default:
                System.out.println("OPS: qualcosa è andato storto");
                break;
        }

    }


    /**
     * @param type is the type of connection specified
     */
    private void chooseNetProtocol(ProtocolType type) {

        view.createConnection(type);
    }


    // game initialization functions

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameSelection() {

        int choice = -1;

        do {

            System.out.println("Digita: \n 1 -> Nuova Partita \n 2 -> Riconnessione a partita già iniziata \n 3 -> Carica una partita salvata");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Non è un numero! Riprova: ");
                scanner.nextLine();
            }

        } while (!(choice == 1 || choice == 2 || choice == 3));


        switch (choice) {

            case 1:
                login();
                break;

            case 2:
                reconnect();
                break;

            case 3:
                System.out.println("Still not implemented ;D");
                break;

            default:
                break;

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void login() {

        String playerName;
        String playerColor;
        boolean validColorChoice = false;

        System.out.println("Scegli un nome: ");
        playerName = scanner.nextLine();

        System.out.println("Seleziona un colore (VERDE, GIALLO, GRIGIO, VIOLA, BLU): ");

        do {
            playerColor = scanner.nextLine().toUpperCase();

            if (playerColor.equals("VERDE") || playerColor.equals("GRIGIO") ||
                    playerColor.equals("GIALLO") || playerColor.equals("VIOLA") ||
                    playerColor.equals("BLU")) {
                validColorChoice = true;
            } else {
                System.out.println("Colore non valido. Riprova:  ");
            }

        } while (!validColorChoice);

        //System.out.println("[DEBUG] PlayerName:  " + playerName);
        //System.out.println("[DEBUG] PlayerColor: " + playerColor);


        view.joinGame(playerName, UiHelpers.colorTranslator(playerColor.toUpperCase()));

    }

    /**
     *{@inheritDoc}
     */
    @Override
    public List<Integer> askMapAndSkulls() {

        List<Integer> mapAndSkulls = new ArrayList<>();
        int mapChoice = -1;
        int skullChoice = -1;
        boolean valid = false;

        for (int i = 1; i <= 3; i++) {
            System.out.println("Mappa " + i);

            try {
                FileRead.populateMatrixFromFile(i);
                synchronized (mapShowSync) {
                    FileRead.showBattlefield();
                }
            } catch (InvalidMapTypeException e){

            }

            System.out.println("");
        }

        System.out.println("Seleziona il tipo di mappa che vuoi usare: 1/2/3");

        do{

            try{

                mapChoice = scanner.nextInt();

            } catch (InputMismatchException e){
                System.out.println("Non è un numero! Riprova: ");
                scanner.nextLine();
            }

            if(mapChoice > 0 && mapChoice < 4){
                valid = true;
                mapAndSkulls.add(mapChoice);
            } else {
                System.out.println("Scelta non valida! Riprova: ");
            }

        } while(!valid);

        valid = false;

        do{

            System.out.println("Seleziona il numero di teschi che vuoi posizionare sulla plancia: ");

            try{

                skullChoice = scanner.nextInt();

            } catch (InputMismatchException e){
                System.out.println("Non è un numero! Riprova: ");
                scanner.nextLine();
            }

            if(skullChoice > 0 && skullChoice < 9){
                valid = true;
                mapAndSkulls.add(skullChoice);
            } else {
                System.out.println("Scelta non valida! Riprova: ");
            }


        } while (!valid);

        return mapAndSkulls;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void retryLogin(String error) {
        switch (error) {
            case Protocol.DEFAULT_NAME_ALREADY_TAKEN_REPLY:
                System.out.println(ANSI_YELLOW.escape() + "[!] Nome già preso." + ANSI_RESET.escape());
                break;
            case Protocol.DEFAULT_COLOR_ALREADY_TAKEN_REPLY:
                System.out.println(ANSI_YELLOW.escape() + "[!] Colore già preso." + ANSI_RESET.escape());
                break;
            case Protocol.DEFAULT_GAME_ALREADY_STARTED_REPLY:
                System.out.println(ANSI_YELLOW.escape() + "[!] Gioco già avviato!" + ANSI_RESET.escape());
                break;
            case Protocol.DEFAULT_MAX_PLAYER_REACHED:
                System.out.println(ANSI_YELLOW.escape() + "[!] Massimo numero di giocatori raggiunto!" + ANSI_RESET.escape());
                break;
            default:
                System.out.println(" Qualcosa è andato storto");
        }
        System.out.println(ANSI_YELLOW.escape() + "[!] Login fallito. Riprova" + ANSI_RESET.escape());
        login();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void retryLogin(Exception e) {

        System.out.println(e.getMessage());

        System.out.println("Login fallito. Riprova");
        login();

    }

    /**
     * reconnect the user, asking him the name used before getting disconnected
     */
    private void reconnect(){

        String name;

        System.out.println("RICONNESSIONE");
        System.out.println("Inserisci il nome usato: ");

        name = scanner.nextLine();

        view.reconnect(name);

    }


    /**
     * {@inheritDoc}
     */
    public void show(String s) {

        synchronized (mapShowSync) {

            System.out.println(s);


            //if weapon buy has failed, re-sync the local cli map with real player position
            if (s.equals(DEFAULT_CANNOT_BUY_WEAPON) || s.startsWith(DEFAULT_INVALID_SHOOT_HEADER) || s.equals(DEFAULT_NO_ENOUGH_AMMO)) {
                FileRead.removePlayer(view.getPlayerId());
                Point p = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosition();
                FileRead.insertPlayer(p.x, p.y, Character.forDigit(view.getPlayerId(), 10));
                FileRead.showBattlefield();
            }
        }
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyUpdate(UpdateType updateType, int playerId, TurnUpdate turnUpdate) {

        switch (updateType) {

            case LOBBY:

                List<String> names = view.getCacheModel()
                        .getCachedPlayers()
                        .stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());

                System.out.println(ANSI_GREEN.escape() + "[LOBBY] Player connessi : " + names + ANSI_RESET.escape());

                break;

            case INITIAL:
                FileRead.loadMap(view.getCacheModel().getMapType());
                System.out.println(ANSI_GREEN.escape() + "[NOTIFICA] Ho scelto casualmente la mappa di tipo: "
                        + view.getCacheModel().getMapType() + ANSI_RESET.escape());

                break;

            case STATS:
                //TODO mostrare i cambiamenti di danni subiti e disconnessioni
                //System.out.println("[DEBUG] Ricevuto STATS update!");

                if (view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosition() != null) {
                    //new positions
                    //System.out.println(ANSI_GREEN.escape() + "[!] Il giocatore: " + playerId + " si è spostato!" + ANSI_RESET.escape());
                    int x = view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosX();
                    int y = view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosY();

                    FileRead.removePlayer(playerId);
                    FileRead.insertPlayer(x, y, Character.forDigit(playerId, 10));
                    synchronized (mapShowSync) {
                        FileRead.showBattlefield();
                    }
                }

                //player disconnected
                if (!view.getCacheModel().getCachedPlayers().get(playerId).getStats().getOnline()) {
                    wasOnline.set(playerId, false);
                    show(ANSI_GREEN.escape() + "[!] Il giocatore: " + playerId + " si è disconnesso!" + ANSI_RESET.escape());

                } else if(view.getCacheModel().getCachedPlayers().get(playerId).getStats().getOnline() &&
                    !wasOnline.get(playerId)){
                    //player reconnected
                    show(ANSI_GREEN.escape() + "[!] Il giocatore: " + playerId + " si è riconnesso!" + ANSI_RESET.escape());
                    wasOnline.set(playerId, true);
                }

                //damage taken
                if (view.getCacheModel().getCachedPlayers().get(playerId).getStats().getDmgTaken() != null) {
                    //TODO show dmg updates or let the user sees them from player info?
                }

                break;

            case POWERUP_BAG:
                //System.out.println("[NOTIFICA] POWEUPBAG update ricevuto!");
                break;

            case WEAPON_BAG:
                //System.out.println("[NOTIFICA] WEAPON_BAG update ricevuto!");
                //show update only on the player who has bought the weapon
                if (playerId == view.getPlayerId()) {
                    show(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag().toString());
                }
                break;

            case AMMO_BAG:
                //System.out.println("[NOTIFICA] AMMO_BAG update ricevuto!");
                if (playerId == view.getPlayerId()) {
                    show(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getAmmoBag().toString());
                }
                break;

            case GAME:
                //System.out.println("[NOTIFICA] GAME update ricevuto!");
                break;

            case SPAWN_CELL:
                //System.out.println("[NOTIFICA] SPAWN_CELL update ricevuto!");
                break;

            case AMMO_CELL:
                //insert AmmoCard in FileRead
                for (int i = 0; i < MAP_R; i++) {
                    for (int j = 0; j < MAP_C; j++) {
                        if (view.getCacheModel().getCachedMap().getCachedCell(i, j) != null) {
                            if (view.getCacheModel().getCachedMap().getCachedCell(i, j).getCellType().equals(CellType.AMMO)) {

                                FileRead.removeAmmoCard(i, j);

                                //if ammoCell has been picked up don't show it on map
                                if (!((CachedAmmoCell) view.getCacheModel().getCachedMap().getCachedCell(i, j)).getAmmoList().isEmpty()) {
                                    FileRead.insertAmmoCard(i, j, (CachedAmmoCell) view.getCacheModel().getCachedMap().getCachedCell(i, j));
                                }

                            }
                        }
                    }
                }

                break;

            case TURN:
                notifyTurnUpdate(turnUpdate);
                break;

            default:

                break;
        }


    }

    /**
     * Notify update regarding other players actions
     * @param turnUpdate
     */
    private void notifyTurnUpdate(TurnUpdate turnUpdate) {

        PowerUpTurnUpdate powerUpTurnUpdate;
        ShootTurnUpdate shootTurnUpdate;
        GrabTurnUpdate grabTurnUpdate;
        MoveTurnUpdate moveTurnUpdate;

        switch (turnUpdate.getActionType()) {

            case POWERUP:

                powerUpTurnUpdate = (PowerUpTurnUpdate) turnUpdate;
                show(ANSI_BLUE.escape() + "[!] Il giocatore " + turnUpdate.getPlayerId() +
                        " ha usato il powerUp " + powerUpTurnUpdate.getPowerUp() + ANSI_RESET.escape());
                break;

            case SHOOT:

                shootTurnUpdate = (ShootTurnUpdate) turnUpdate;
                show(ANSI_BLUE.escape() + "[!] Il giocatore " + turnUpdate.getPlayerId() +
                        " ha sparato con l'arma " + UiHelpers.weaponTranslator(shootTurnUpdate.getWeapon()) + " al player con id: " +
                        shootTurnUpdate.getTargetId() + ANSI_RESET.escape());
                break;

            case GRAB:

                grabTurnUpdate = (GrabTurnUpdate) turnUpdate;

                if(grabTurnUpdate.getWeapon() != null){
                    show(ANSI_BLUE.escape() + "[!] Il giocatore " + turnUpdate.getPlayerId() +
                            " ha raccolto " + UiHelpers.weaponTranslator(grabTurnUpdate.getWeapon()) + ANSI_RESET.escape());
                } else {
                    show(ANSI_BLUE.escape() + "[!] Il giocatore " + turnUpdate.getPlayerId() +
                            " ha raccolto " + ANSI_RESET.escape());
                }

                break;

            case MOVE:

                moveTurnUpdate = (MoveTurnUpdate) turnUpdate;
                show(ANSI_BLUE.escape() + "[!] Il giocatore " + turnUpdate.getPlayerId() +
                        " si è mosso" + ANSI_RESET.escape());

                break;

            default:

                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        System.out.println("Gioco iniziato!");

        while (view.getCacheModel().getCachedMap() == null) {
            System.out.println("Attendi ricezione del tipo di mappa...");
            try {
                sleep(200);
            } catch (Exception e) {

            }
        }

        try {
            FileRead.populateMatrixFromFile(view.getCacheModel().getMapType());
        } catch (InvalidMapTypeException e) {
            System.out.println("[DEBUG] Errore nel caricamento della mappa: \n" + e.getMessage());
        }

        synchronized (mapShowSync) {
            FileRead.showBattlefield();
        }
        showInfo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSpawn() {
        //TODO consume scanner buffer if user type random numbers when waiting for its turn
        scanner.reset();

        List<CachedPowerUp> powerUps;

        Boolean validChoice = false;
        int read;

        System.out.println("SPAWN PHASE");

        do {

            while (view.getCacheModel().getCachedPlayers().size() <= 0 || view.getPlayerId() == -1) {
                System.out.println("Attendi ricezione dell'update iniziale...");

                try {
                    sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            while (view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getPowerUpBag().getPowerUpList().isEmpty()) {

                System.out.println("Attendi ricezione dei PowerUp pescati...");
            }

            powerUps = view
                    .getCacheModel()
                    .getCachedPlayers()
                    .get(view.getPlayerId())
                    .getPowerUpBag()
                    .getPowerUpList();


            System.out.println("Hai questi PowerUp:");

            for (int i = 0; i < powerUps.size(); i++) {
                System.out.println(i + " " + powerUps.get(i).toString());

            }

            System.out.println("Scegli un powerUp da scartare: ");

            try {
                read = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Non è un numero! Riprova: ");
                scanner.nextLine();
                read = -1;
            }

            if (read >= 0 && read < powerUps.size()) validChoice = true;


        } while (!validChoice);

        view.spawn(powerUps.get(read));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startPowerUp() {
        //TODO consume scanner buffer if user type random numbers when waiting for its turn

        while(view.getPlayerId() == -1) {
            try{
                sleep(200);
             } catch (InterruptedException e){

            }
        }


        //System.out.println("[DEBUG] startPowerUp");
        System.out.println("POWERUP PHASE");


        List<CachedPowerUp> powerUps;
        List<CachedPowerUp> usablePowerUps;
        Boolean validChoice = false;
        int read = -1;
        scanner.reset();

        do {

            powerUps = view
                    .getCacheModel()
                    .getCachedPlayers()
                    .get(view.getPlayerId())
                    .getPowerUpBag()
                    .getPowerUpList()
                    .stream()
                    .collect(Collectors.toList());

            usablePowerUps = view
                    .getCacheModel()
                    .getCachedPlayers()
                    .get(view.getPlayerId())
                    .getPowerUpBag()
                    .getPowerUpList()
                    .stream()
                    .filter(x -> ((x.getType() != TAG_BACK_GRENADE) && (x.getType() != PowerUpType.TARGETING_SCOPE)))
                    .collect(Collectors.toList());

            read = -1;
            System.out.println("Hai questi PowerUp:");

            for (int i = 0; i < powerUps.size(); i++) {
                System.out.println(i + " :" + powerUps.get(i).toString());
            }

            System.out.println("Puoi usare uno di questi:");
            for (int i = 0; i < usablePowerUps.size(); i++) {
                System.out.println(i + " :" + usablePowerUps.get(i).toString());
            }

            System.out.println("9 -> non usare powerUp");
            scanner.reset();
            System.out.println("Scegli un powerUp da usare: ");

            while (read == -1) {
                try {
                    read = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Non è un numero! Riprova");
                    scanner.nextLine();
                }
            }

            if ((read >= 0 && read < usablePowerUps.size()) || read == 9) validChoice = true;
            else {
                System.out.println("Scelta non valida! Riprova");
                scanner.reset();
                scanner.nextLine();
            }

        } while (!validChoice);

        if (read == 9) {
            // if the user types 9 -> end of powerUp phase
            view.doAction(new SkipAction());

        } else if (read == 8) {
            showInfo();
        } else {
            usePowerUp(usablePowerUps.get(read));
        }


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askGrenade() {

        boolean valid;
        int choice = -1;

        List<CachedPowerUp> grenades = view
                .getCacheModel()
                .getCachedPlayers()
                .get(view.getPlayerId())
                .getPowerUpBag()
                .getPowerUpList()
                .stream()
                .filter(x -> x.getType().equals(TAG_BACK_GRENADE))
                .collect(Collectors.toList());

        do {

            scanner.reset();

            valid = false;

            System.out.println("Ti hanno sparato: vuoi usare una granata ? \n hai queste granate: ");

            for (int i = 0; i < grenades.size(); i++) {

                System.out.println(i + grenades.get(i).toString());

            }

            System.out.println("Digita il numero della carta che vuoi usare o '9' per non usarne nessuna: ");

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Non è un numero! Riprova: ");
                scanner.nextLine();
            }

            if (choice == 9 || (choice >= 0 && choice < grenades.size())) valid = true;


        } while (!valid);

        // if the player choose not to use grenades a grenadeAction will be also sent, but will have color null

        if (choice == 9) view.doAction(new GrenadeAction(null, view.getPlayerId()));
        else view.doAction(new GrenadeAction(grenades.get(choice).getColor(), view.getPlayerId()));


    }

    /**
     *
     * @param powerUp CachedPowerUp to be used, only newton and teleporter can be used in the powerup phases
     */
    private void usePowerUp(CachedPowerUp powerUp) {

        switch (powerUp.getType()) {

            case NEWTON:
                useNewton(powerUp);
                break;

            case TELEPORTER:
                useTeleporter(powerUp);
                break;

            default:
                break;

        }

    }

    /**
     * handle newton requests
     * @param newton cachedPowerUp chosen to use by the player
     */
    private void useNewton(CachedPowerUp newton) {

        String retryMessage = "Scelta non valida. Riprova.";

        Boolean validChoice = false;
        int player = -1;
        String direction;
        int amount = 1;


        do {

            validChoice = false;

            System.out.println("Su quale giocatore vuoi usare Newton? >>> ");
            try {
                player = scanner.nextInt();

                if (player >= 0 && player < view.getCacheModel().getCachedPlayers().size())
                    validChoice = true;
                else
                    System.out.println(retryMessage);

            } catch (InputMismatchException e) {
                System.out.println("Non è un numero! Riprova >>> ");
                scanner.nextLine();
            }

        } while (!validChoice);


        do {

            validChoice = false;

            System.out.println("In quale direzione vuoi spostare il player selezionato? (Nord, Sud, Ovest, Est) >>> ");
            direction = scanner.next().toUpperCase();
            scanner.nextLine();

            if (direction.equals("NORD") || direction.equals("SUD")
                    || direction.equals("EST") || direction.equals("OVEST"))
                validChoice = true;
            else
                System.out.println(retryMessage);
        } while (!validChoice);


        do {
            validChoice = false;

            System.out.println("Di quanto vuoi muovere il player? (1,2) >>> ");
            try {
                amount = scanner.nextInt();

                if (amount == 1 || amount == 2)
                    validChoice = true;

                else
                    System.out.println(retryMessage);

            } catch (InputMismatchException e){
                System.out.println("Non è un numero! Riprova>>> ");
                scanner.nextLine();
            }

        } while (!validChoice);

        // Create a NewtonAction object

        NewtonAction newtonAction = new NewtonAction(newton.getColor(), player, amount, directionTranslator(direction));

        // sends it to the Virtual view

        view.doAction(newtonAction);
    }

    /**
     * handle teleport request to the user
     * @param teleporter CachedPowerUp teleport to consume
     */
    private void useTeleporter(CachedPowerUp teleporter) {

        Point p = askCell();

        JsonAction jsonAction = new TeleporterAction(teleporter.getColor(), p);

        view.doAction(jsonAction);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAction(boolean isFrenzy, boolean isBeforeFrenzyStarter) {

        while(view.getPlayerId() == -1) {
            try{
                sleep(200);
            } catch (InterruptedException e){

            }
        }

        //TODO consume scanner buffer if user type random numbers when waiting for its turn
        scanner.reset();

        //TODO check if this works
        if(isFrenzy){
            this.isFrenzy = true;
        } else {
            this.isFrenzy = false;
        }

        boolean valid;
        int choice = -1;
        int playerDmg = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getDmgTaken().size();

        List<String> actions = new ArrayList<>(Arrays.asList("MUOVI", "MUOVI E RACCOGLI", "SPARA"));

        do {

            valid = false;

            //NOTE: startAction uses show instead of system.out.println because there's a chance that
            //when the controller reply back with a message the showBattlefield method and the startAction method
            //and the map won't be displayed correctly
            if(isFrenzy){
                show("FASE FRENZY");
            }
            show("SELEZIONA UN AZIONE:");

            for (int i = 0; i < actions.size(); i++) {
                System.out.println(i + ": " + actions.get(i));
            }
            show("6: mostra mappa");
            show("7: mostra info giocatori");
            show("8: mostra armi in vendita nelle celle §");
            show("9: SKIP");

            try {
                scanner.reset();
                if(scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                }
                scanner.nextLine();

            } catch (InputMismatchException e) {
                System.out.println("Non è un numero: Riprova!");
                scanner.nextLine();
            }


            if ((choice >= 0 && choice < actions.size()) || choice ==6 || choice == 7 || choice == 8 || choice == 9) {
                valid = true;

            } else {
                System.out.println(" Scelta non valida: Riprova");
            }

        } while (!valid);

        switch (choice) {

            case 0:
                if (isFrenzy) {
                    if (isBeforeFrenzyStarter) {
                        startMove(DEFAULT_MAX_FRENZY_MOVES);
                    } else {
                        System.out.println("[!] Azione non disponibile: sei dopo il Frenzy Starter!");
                        startAction(isFrenzy, isBeforeFrenzyStarter);
                    }
                } else {
                    startMove(DEFAULT_MAX_NORMAL_MOVES);
                }
                break;

            case 1:
                if (isFrenzy) {
                    if (isBeforeFrenzyStarter) {
                        startGrab(DEFAULT_MOVES_WITH_FRENZY);
                    } else {
                        startGrab(DEFAULT_MOVES_WITH_ENHANCED_FRENZY);
                    }

                } else {
                    //grab -> if player has more than 2 dmg -> 2 moves else -> only 1 move
                    if (playerDmg >= DEFAULT_DMG_TO_UNLOCK_ENHANCED_GRAB) {
                        startGrab(DEFAULT_ENHANCED_MOVES_WITH_GRAB);
                    } else {
                        startGrab(DEFAULT_MOVES_WITH_GRAB);
                    }
                }
                break;

            case 2:
                if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag() != null) {
                    if (view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag().getWeapons().isEmpty()) {
                        System.out.println("[!] Non hai armi per sparare!");
                        startAction(isFrenzy, isBeforeFrenzyStarter);
                    } else {

                        if (isFrenzy) {
                            if (isBeforeFrenzyStarter) {
                                startFrenzyShoot(DEFAULT_MOVES_WITH_FRENZY_SHOOT);
                            } else {
                                startFrenzyShoot(DEFAULT_MOVES_WITH_ENHANCED_FRENZY_SHOOT);
                            }
                        } else {

                            if (playerDmg >= DEFAULT_DMG_TO_UNLOCK_ENHANCED_SHOOT) {
                                startShoot(DEFAULT_ENHANCED_MOVES_WITH_SHOOT);
                            } else {
                                startShoot(0);
                            }
                        }
                    }
                } else if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag() == null) {
                    System.out.println("[!] Non hai armi per sparare!");
                    startAction(isFrenzy, isBeforeFrenzyStarter);
                }
                break;

            case 6:
                synchronized (mapShowSync) {
                    FileRead.showBattlefield();
                }
                startAction(isFrenzy, isBeforeFrenzyStarter);
                break;

            case 7:
                showInfo();
                startAction(isFrenzy, isBeforeFrenzyStarter);
                break;

            case 8:

                showWeapInSpawnCells();
                startAction(isFrenzy, isBeforeFrenzyStarter);
                break;

            case 9:
                view.doAction(new SkipAction());
                break;

            default:

                System.out.println("Azione non esistente");
                break;
        }
    }

    @Override
    public void reDoFrenzyAtomicShoot() {
        // TODO
    }

    /**
     * Handle a move request, asking to move to up to maxMoves, or stop before
     * @param maxMoves max moves which can be done by the user
     * @return a List of Directions with the choices of the user
     */
    private List<Directions> handleMove(int maxMoves) {

        int moves = 0;
        List<Directions> directionsList = new ArrayList<>();
        boolean valid = false;
        String choice;
        Point temp_moves;
        int x;
        int y;
        List<Directions> previous = new ArrayList<>();

        do {

            System.out.println("In che direzione ti vuoi muovere? Ti restano " + (maxMoves - moves) + " movimenti.");
            System.out.println("Inserisci una direzione (Nord, Sud, Ovest, Est, Stop per fermarti qui) >>> ");

            do {
                choice = scanner.nextLine();
                choice = choice.toUpperCase();

                x = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosX();
                y = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosY();
                temp_moves = new Point(x, y);

                if (choice.equals("STOP")) {
                    return directionsList;
                }

                if ((choice.equals("NORD") || choice.equals("SUD") || choice.equals("EST") || choice.equals("OVEST"))) {

                    //System.out.println("[CLI] ask to server if move: " + directionTranslator(choice) + " from pos: " +  x + ", "  + y);
                    //System.out.println( "[CLI] cell: " + view.getCacheModel().getCachedMap().getCachedCell( p.x, p.y).getCellType() );

                    Directions d = directionTranslator(choice);

                    Point start = new Point(x, y);
                    Point finalPos = genPointFromDirections(previous, start);

                    validMove = -1;
                    view.askMoveValid(finalPos.x, finalPos.y, d);

                    while (validMove == -1) {
                        try {
                            synchronized (this) {
                                //System.out.println("Waiting to receive validMove reply...");
                                this.wait();
                            }

                        } catch (InterruptedException e) {

                        }
                        //System.out.println("Received validMove reply!");
                    }

                    //System.out.println("[DEBUG] controllo valid Move...");
                    if (validMove == 1) {
                        System.out.println("Direzione valida!");
                        valid = true;
                        previous.add(d);
                        directionsList.add(directionTranslator(choice));
                        moves++;

                        //update map view with single movements
                        temp_moves = genPointFromDirections(directionsList, temp_moves);
                        FileRead.removePlayer(view.getPlayerId());
                        FileRead.insertPlayer(temp_moves.x, temp_moves.y, Character.forDigit(view.getPlayerId(), 10));

                        synchronized (mapShowSync){
                            FileRead.showBattlefield();
                        }

                    } else if (validMove == 0) {

                        System.out.println("Direzione non valida! Riprova >>> ");
                        valid = false;
                    }

                } else {
                    System.out.println("Scrivi correttamente la direzione dei punti cardinali! Riprova");
                }

            } while (!valid);

        } while (moves < maxMoves);

        return directionsList;
    }


    public void startMove(int maxMoves) {

        int x = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosX();
        int y = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosY();
        Point startingPoint = new Point(x, y);


        //move -> always max 3 movements
        List<Directions> directionsList = handleMove(maxMoves);
        Point finalPos = genPointFromDirections(directionsList, startingPoint);

        view.doAction(new Move(directionsList, finalPos));
    }

    private void startGrab(int maxMoves) {

        int x = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosX();
        int y = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosY();
        Point startingPoint = new Point(x, y);

        List<Directions> directionsList;

        //grab -> if player has more than 2 dmg -> 2 moves else -> only 1 move
        if (view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getDmgTaken().size() >= DEFAULT_DMG_TO_UNLOCK_ENHANCED_GRAB) {
            directionsList = handleMove(maxMoves);
        } else {
            directionsList = handleMove(maxMoves);
        }

        Point finalPos = genPointFromDirections(directionsList, startingPoint);

        CellType cellType = view.getCacheModel().getCachedMap().getCachedCell(finalPos.x, finalPos.y).getCellType();

        switch (cellType) {

            case AMMO:
                view.doAction(new GrabAction(directionsList));
                break;

            case SPAWN:
                List<String> weapons = handleWeaponGrab(finalPos);
                List<CachedPowerUp> powerUpsToDiscard = new ArrayList<>();
                try {
                    CachedFullWeapon w = view.getCacheModel().getWeaponInfo(weapons.get(1));
                    powerUpsToDiscard = checkPayWithPowerUp(w.getBuyEffect());
                } catch (WeaponNotFoundException e) {
                }
                view.doAction(new GrabAction(directionsList, weapons.get(0), weapons.get(1), powerUpsToDiscard));
                break;
        }
    }

    /**
     * Handle weapons grab: if player has already 3 weapons he has to choose 1 to discard
     *
     * @param pos current spawn cell from where he wants to buy the weapon
     * @return a List of String representing the name of the weapon to buy and, if needed, the one to discard
     */
    //TODO i need to check that he buy weap when i have more ammo and that it let me choose one to discard when i have already 3
    private List<String> handleWeaponGrab(Point pos) {

        CachedSpawnCell cell = (CachedSpawnCell) view.getCacheModel().getCachedMap().getCachedCell(pos.x, pos.y);

        List<String> weapons = cell.getWeaponNames();
        List<String> choice = new ArrayList<>();

        System.out.println("ARMI IN VENDITA: ");
        for (int i = 0; i < cell.getWeaponNames().size(); i++) {
            try {
                CachedFullWeapon w = view.getCacheModel().getWeaponInfo(cell.getWeaponNames().get(i));
                System.out.println("Arma " + i + " : " + UiHelpers.weaponTranslator(cell.getWeaponNames().get(i)) +
                        " Costo: " + UiHelpers.ammoTranslator(w.getBuyEffect()));
            } catch (WeaponNotFoundException e){
                System.out.println("[DEBUG] Weapon not found! " + e.getMessage());
            }
        }

        System.out.println("Digita il numero dell'arma che vuoi acquistare >>> ");
        int buy = -1, discard = -1;
        boolean valid = false;
        scanner.reset();

        do {

            scanner.reset();

            try {
                buy = scanner.nextInt();

                if (buy >= 0 && buy < weapons.size()) {
                    valid = true;
                } else {
                    System.out.println("Numero non valido! Riprova >>> ");
                }

            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Non è un numero! Riprova >>> ");
            }

        } while (!valid);

        //if the player has already 3 weapons he has to choose one to discard to buy another one
        List<String> currWeap = new ArrayList<>();
        if (view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag() != null) {
            currWeap = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag().getWeapons();
        }

        if (currWeap.size() >= DEFAULT_MAX_WEAPONS) {
            System.out.println("Hai già 3 armi. Scegli un arma da scartare >>>");

            showCurrWeapons();

            System.out.println("Seleziona il numero dell'arma che vuoi scartare >>> ");
            valid = false;

            do {

                scanner.nextLine();
                scanner.reset();

                try {
                    discard = scanner.nextInt();

                    if (discard >= 0 && discard <= currWeap.size()) {
                        valid = true;
                    } else {
                        System.out.println("Numero non valido! Riprova >>> ");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Non è un numero! Riprova >>> ");
                }

            } while (!valid);

            switch (discard) {

                case 0:
                    choice.add(currWeap.get(0));
                    break;

                case 1:
                    choice.add(currWeap.get(1));
                    break;

                case 2:
                    choice.add(currWeap.get(2));
                    break;

                default:
                    //this can't happen since we do valid checks before switch case
                    System.out.println("Scelta arma da scartare non valida!");
            }

        } else {
            //if player has less than 3 weapon, he doesn't need to choose one to discard
            choice.add(null);
        }


        switch (buy) {

            case 0:
                choice.add(weapons.get(0));
                break;

            case 1:
                choice.add(weapons.get(1));
                break;

            case 2:
                choice.add(weapons.get(2));
                break;

            default:
                //this cannot happen since we control valid number before switch case
                System.out.println("Scelta non valida!");
        }

        return choice;
    }

    /**
     *
     * @param cost cost to be checked if payable with powerups
     * @param powerUps take powerups as parameter because you can remove some of them for partial cost checks
     * @param ammo take ammo as parameter because you can remove some of them for partial checks
     * @return a list of CachedPowerUps to discard to pay the needed cost
     */
    private List<CachedPowerUp> checkPayWithPowerUp(List<Color> cost, List<CachedPowerUp> powerUps, List<Color> ammo){

        boolean valid = false;
        List<CachedPowerUp> powerUpsToDiscard = new ArrayList<>();

        for (Color c : cost) {
            if (ammo.contains(c) && hasPowerUpOfColor(powerUps, c)){

                System.out.println("Puoi pagare " + UiHelpers.ammoTranslator(c) + " usando un PowerUp o con una munizione.");
                String powerUpOrAmmo = null;
                boolean validPowerUpChoice = false;
                System.out.println("Vuoi usare un PowerUp per pagare al posto delle munizioni? (si/no)");
                do {
                    powerUpOrAmmo = scanner.nextLine();

                    if (powerUpOrAmmo.toUpperCase().equals("SI") || powerUpOrAmmo.toUpperCase().equals("NO")) {
                        validPowerUpChoice = true;
                    }
                } while (!validPowerUpChoice);

                if (powerUpOrAmmo.toUpperCase().equals("SI")) {

                    List<CachedPowerUp> powerUpChoiceList = powerUps
                            .stream()
                            .filter(x -> x.getColor().equals(c))
                            .collect(Collectors.toList());

                    System.out.println("Scegli quale PowerUp scartare al posto della munizione: ");

                    for (int i = 0; i < powerUpChoiceList.size(); i++) {
                        System.out.println(i + " : " + powerUpChoiceList.get(i));
                    }

                    valid = false;
                    int powerUpToDiscardChoice = -1;

                    do {
                        try {
                            powerUpToDiscardChoice = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Non è un numero! Riprova: ");
                            scanner.nextLine();
                        }

                        if (powerUpToDiscardChoice >= 0 && powerUpToDiscardChoice < powerUpChoiceList.size()) {
                            valid = true;
                        } else {
                            System.out.println("Scelta non valida! Riprova: ");
                        }

                    } while (!valid);

                    CachedPowerUp powerUpToDiscard = powerUpChoiceList.get(powerUpToDiscardChoice);
                    powerUps.remove(powerUpToDiscard);
                    powerUpsToDiscard.add(powerUpToDiscard);
                    //cost.remove(c);
                }

            } else if (hasPowerUpOfColor(powerUps, c) && !ammo.contains(c)) {

                //TODO tell him if he wants to drop a powerup to buy
                System.out.println("Puoi pagare " + UiHelpers.ammoTranslator(c) + " solamente con un PowerUp: ");
                List<CachedPowerUp> powerUpChoiceList = powerUps
                        .stream()
                        .filter(x -> x.getColor().equals(c))
                        .collect(Collectors.toList());

                for (int i = 0; i < powerUpChoiceList.size(); i++) {
                    System.out.println(i + " : " + powerUpChoiceList.get(i));
                }

                valid = false;
                int powerUpToDiscardChoice = -1;

                do {
                    try {
                        powerUpToDiscardChoice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Non è un numero! Riprova: ");
                        scanner.nextLine();
                    }

                    if (powerUpToDiscardChoice >= 0 && powerUpToDiscardChoice < powerUpChoiceList.size()) {
                        valid = true;
                    } else {
                        System.out.println("Scelta non valida! Riprova: ");
                    }

                } while (!valid);

                CachedPowerUp powerUpToDiscard = powerUpChoiceList.get(powerUpToDiscardChoice);
                powerUps.remove(powerUpToDiscard);
                powerUpsToDiscard.add(powerUpToDiscard);
                //cost.remove(c);


            } else if (ammo.contains(c)) {
                ammo.remove(c);
                //cost.remove(c);
            } else {
                //this shouldn't do anythign , just forward the choice and then controller will
                //reply back that player hasn't got enough ammo
            }
        }

        //System.out.println("[DEBUG] PowerUp da scartare scelti: " + powerUpsToDiscard);
        return powerUpsToDiscard;
    }

    /**
     * Same as checkPayWithPowerUp but simpler version, which in case you don't need to specify local ammo and powerups
     * will just read them from cacheModel and then call the main checkPayWithPowerUps method with them as parameters
     * @param cost to be checked
     * @return a list of CachedPowerUp to discard to pay the specified cost
     */
    private List<CachedPowerUp> checkPayWithPowerUp(List<Color> cost) {

        List<CachedPowerUp> powerUps = new ArrayList<>();
        CopyOnWriteArrayList<Color> ammo = new CopyOnWriteArrayList<>();
        List<CachedPowerUp> powerUpsToDiscard = new ArrayList<>();

        if (view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getAmmoBag() != null)
            ammo.addAll(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getAmmoBag().getAmmoList());

        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getPowerUpBag() != null) {
            powerUps = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getPowerUpBag().getPowerUpList();
            //powerUpsColor = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getPowerUpBag().getPowerUpColorList();
        }

        powerUpsToDiscard = checkPayWithPowerUp(cost, powerUps, ammo);

        return powerUpsToDiscard;

    }

    private boolean hasPowerUpOfColor(List<CachedPowerUp> powerUps, Color c){
        List<CachedPowerUp> result = powerUps
        .stream()
        .filter(x -> x.getColor().equals(c))
        .collect(Collectors.toList());

        return  !result.isEmpty();

    }


    private void showCurrWeapons(){

        List<String> currWeap = new ArrayList<>();

        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag() != null){
            currWeap = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag().getWeapons();
        }

        for (int i = 0; i < currWeap.size(); i++) {
            System.out.println("Arma " + i + " " + UiHelpers.weaponTranslator(currWeap.get(i)));
            try {
                CachedFullWeapon w = view.getCacheModel().getWeaponInfo(currWeap.get(i));
                System.out.println("Effetto base: " + w.getEffectsDescriptions().get(0));
                System.out.println("costo ricarica: " + UiHelpers.ammoTranslator(w.getFirstEffectCost()));

                if (w.getSecondEffectCost() != null) {
                    System.out.println("Effetto 1: " + w.getEffectsDescriptions().get(1));
                    System.out.println("costo effetto 1: " + UiHelpers.ammoTranslator(w.getSecondEffectCost()));
                }

                if (w.getThirdEffectCost() != null) {
                    System.out.println("Effetto 2: " + w.getEffectsDescriptions().get(2));
                    System.out.println("costo effetto 2: " + UiHelpers.ammoTranslator(w.getThirdEffectCost()));
                }
            } catch (WeaponNotFoundException e){
                System.out.println("[DEBUG] Weapon Not Found!" + e.getMessage());
            }
        }

    }



    private void startShoot(int maxMoves){

        //INPUT requirements
        boolean valid = false;
        int choice = -1;

        //SHOOT ACTION requirements
        List<List<Integer>> targetList;
        List<Integer> effects = new ArrayList<>();
        List<Point> cells = new ArrayList<>();
        List<Directions> directionsList = new ArrayList<>();
        CachedFullWeapon weapon = null;

        //EFFECT PAYMENT requirements
        List<CachedPowerUp> localPowerUps = new ArrayList<>();
        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getPowerUpBag() != null)
            localPowerUps = new ArrayList<>(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getPowerUpBag().getPowerUpList());
        List<Color> localAmmo = new ArrayList<>();
        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getAmmoBag() != null)
            localAmmo = new ArrayList<>(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getAmmoBag().getAmmoList());
        List<CachedPowerUp> powerUpsToDiscard = new ArrayList<>();


        //weapon checks
        int targets = -1; //number of targets needed by the weapons (reads this from json CachedFullWeapons)
        boolean needCell = false; //if this weapon needs a Cell for movements related to the shoot


        //if player has > 5 dmg he can do one movement, otherwise no moves
        if(maxMoves > 0){
            Point startingPoint = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosition();
            directionsList = handleMove(maxMoves);
            Point finalPos = genPointFromDirections(directionsList, startingPoint);
        } else {
            directionsList.add(null);
        }

        // At this point there are 3 cases:
        // case A -> player hasn't gained enhanced shoot
        // case B -> player has gained an extra move with enhanced shoot and does it
        // case C -> player has gained ane extra move with enhanced shoot but didn't move
        // directionList and finalPos will be sent in case B or C

        //CHOOSE WEAPON PHASE

        do{

            System.out.println("Seleziona l'arma con cui vuoi sparare: ");
            showCurrWeapons();

            try{

                scanner.reset();
                choice = scanner.nextInt();

                int weaponBagSize = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag().getWeapons().size();
                if(choice >= 0 && choice < weaponBagSize){
                    valid = true;

                } else {
                    System.out.println("Scelta non valida! Riprova >>> ");
                }

            } catch (InputMismatchException e){
                System.out.println("Non è un numero! Riprova >>> ");
                scanner.nextLine();
            }


        } while (!valid);

        try {

            weapon = view.getCacheModel()
                .getWeaponInfo(view.getCacheModel().getCachedPlayers().get(view.getPlayerId())
                        .getWeaponbag().getWeapons().get(choice));

        } catch (WeaponNotFoundException e){
            System.out.println("Weapon not found "+ e.getMessage());
        }

        // PRE-SHOOT PHASE choose wep effects, checks if he can pay
        // choose target/s and additional info needed to shoot with a particular weapon

        //EFFECTS REQUESTS and COST check (if user wants to pay with powerups)
        effects = chooseEffects(weapon);

        List<Color> totEffectCost = new ArrayList<>();
        //TODO check if total effect cost is correct, export it in an outside method
        for(Integer i : effects){
            switch (i){
                case 1:
                    totEffectCost.addAll(weapon.getSecondEffectCost());
                    break;

                case 2:
                    totEffectCost.addAll(weapon.getThirdEffectCost());
                    break;

            }
        }

        if(canPay(totEffectCost, UiHelpers.genColorListFromPowerUps(localPowerUps), localAmmo)){
            powerUpsToDiscard = checkPayWithPowerUp(totEffectCost, localPowerUps, localAmmo);

            //update local powerups list with powerups which user has chosen to discard
            //it will be useful for targeting scope final check before shoot
            for(CachedPowerUp p : powerUpsToDiscard){
                localPowerUps.remove(p);
            }

        } else {
            System.out.println("[!] Non hai abbastanza munizioni per usare gli effetti selezionati!");
            //TODO check if it works, should let him retry shoot from the beginning
            startShoot(maxMoves);
        }

        scanner.nextLine();

        //System.out.println("[DEBUG] Number of targets: " + weapon.getEffectRequirements().get(0).getNumberOfTargets());
        //System.out.println("[DEBUG] Effects size: " + effects.size());

        //TARGETS REQUESTS
        targetList = chooseTargets(weapon, effects.size());

        //CELL REQUESTS
        boolean cellRequired = false;

        for (int i = 0; i < effects.size(); i++) {
            //e is the number inside the effect array i.e. i could have effect 2 in index 0 of array effects
            int e = effects.get(i);
            if(weapon.getEffectRequirements().get(e).getCellRequired()){
                Point p = askCell(weapon, e);
                cells.add(p);
                cellRequired = true;
            } else {
                cells.add(null);
            }
        }

        if(!cellRequired){
            cells = null;
        }


        //TARGETING SCOPE
        ScopeAction scopeAction = null;
        CachedPowerUp scope = null;
        int scopeTarget = -1;
        boolean useScope = false;
        boolean validScopeChoice = false;

        if(UiHelpers.genTypeListFromPowerUps(localPowerUps).contains(TARGETING_SCOPE)){
            System.out.println("Puoi usare un mirino, vuoi farlo? (si/no): ");
            do {
                String s = scanner.nextLine();

                if(s.equalsIgnoreCase("SI") || s.equalsIgnoreCase("NO")){
                    validScopeChoice = true;
                    if(s.equalsIgnoreCase("SI")){
                        useScope = true;
                    }
                } else {
                    System.out.println("Scelta non valida! Riprova: ");
                }

            } while(!validScopeChoice);

            if(useScope){
                scope = handleScopeRequest(localPowerUps);
                scopeTarget = handleScopeTarget();
                scopeAction = new ScopeAction(UiHelpers.genColorFromPowerUp(scope), scopeTarget);

            }
        }


        //forward the shoot action to the controller -> if shoot fails it won't do the shoot and
        //let the user retry the shoot specifiying why shoot has failed
        if(isFrenzy){
            view.doAction(new FrenzyShoot(new ShootAction(weapon.getName(), targetList, effects, cells, powerUpsToDiscard, scopeAction)));
        } else {
            view.doAction(new ShootAction(weapon.getName(), directionsList.get(0), targetList, effects, cells, powerUpsToDiscard, scopeAction));
        }
    }

    private void startFrenzyShoot(int maxMoves){

        List<Directions> directionsList = new ArrayList<>();

        //FORWARD PART 1: MOVE ACTION
        directionsList = handleMove(maxMoves);
        Point startingPoint = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosition();
        Point finalPos = genPointFromDirections(directionsList, startingPoint);

        view.doAction(new FrenzyShoot(new Move(directionsList, finalPos)));

        //FORWARD PART 2: RELOAD ACTION
        startReload();

        //FORWARD PART 3: SHOOT ACTION
        startShoot(0);

    }

    private CachedPowerUp handleScopeRequest(List<CachedPowerUp> powerUps){

        int read = -1;
        boolean valid = false;

        List<CachedPowerUp> scopePowerUps = powerUps
                .stream()
                .filter(x -> (x.getType() == PowerUpType.TARGETING_SCOPE))
                .collect(Collectors.toList());

        System.out.println("Seleziona un mirino da usare: ");

        for (int i = 0; i < scopePowerUps.size(); i++) {
            System.out.println(i + ": " + scopePowerUps.get(i));
        }

        scanner.nextLine();

        do {

            try {
                read = scanner.nextInt();

                if(read >= 0 && read < scopePowerUps.size()){
                    valid = true;
                } else {
                    System.out.println("Scelta non valida! Riprova: ");
                }

            } catch (InputMismatchException e) {
                System.out.println("Non è un numero! Riprova: ");
                scanner.nextLine();
            }

        } while(!valid);

        return scopePowerUps.get(read);
    }

    private int handleScopeTarget(){

        boolean validChoice = false;
        int player = -1;

        do {

            System.out.println("Su quale giocatore vuoi usare il mirino? >>> ");
            try {
                player = scanner.nextInt();
                scanner.nextLine();

                if (player >= 0 && player <= view.getCacheModel().getCachedPlayers().size() && player != view.getPlayerId())
                    validChoice = true;
                else
                    System.out.println("Scelta non valida! Riprova>>> ");

            } catch (InputMismatchException e){
                System.out.println("Non è un numero! Riprova");
            }

        } while (!validChoice);

        return player;
    }



    private Point askCell(){

        boolean validChoice = false;
        int r = -1, c = -1;

        do {

            validChoice = false;

            System.out.println("Seleziona cella >>> ");

            //this way you have to check locally if r,c is ok for every different type of map

            do {
                System.out.println("riga >>> (0,1,2)");

                r = scanner.nextInt();
                scanner.nextLine();
            } while (r < 0 || r > 2);

            do {
                System.out.println("colonna >>> (0,1,2,3)");

                c = scanner.nextInt();
                scanner.nextLine();
            } while (c < 0 || c > 3);

            if (view.getCacheModel().getCachedMap().getCachedCell(r, c) == null) {

                System.out.println("Cella non esistente. Riprova: ");

            } else {

                validChoice = true;

            }

        } while (!validChoice);

        return new Point(r,c);

    }

    private Point askCell(CachedFullWeapon w, int e){
        System.out.println("Cella richiesta per effetto " + e);
        Point p = askCell();
        return p;
    }



    /**
     * Helper method needed by startShoot to collect target/s to be shot
     * @return a List of List<Integer> representing the targets for each of the weapon effect
     * (index 0 -> base effect, index 1 -> second effect, index 2 -> third effect)
     */
    private List<List<Integer>> chooseTargets(CachedFullWeapon w, int effectsNum){

        List<List<Integer>> targetsList = new ArrayList<>();
        boolean valid = false;

        for (int i = 0; i < effectsNum; i++) {

            List<Integer> tempTargetList = new ArrayList<>();

            System.out.println("Seleziona i bersagli a cui vuoi sparare: ");
            System.out.println("Effetto: " + i);

            int cont = 0;

            for (int j = 0; j < w.getEffectRequirements().get(i).getNumberOfTargets().size(); j++) {

                do {

                    int read = -1;

                    System.out.println("Seleziona un bersaglio (ID) >>> ");

                    try {

                        if(cont > 0){
                            System.out.println("9 -> per selezionare solo questi bersagli.");
                        }

                        scanner.reset();
                        read = scanner.nextInt();

                        if(read >= 0 && read <= view.getCacheModel().getCachedPlayers().size() && read != view.getPlayerId()){
                            cont++;
                            valid = true;
                            tempTargetList.add(read);
                        } else if(read == 9){
                            //TODO check if it works, should let him target up to max target (min 1) instead of exactly max
                            valid = true;
                            j = w.getEffectRequirements().get(i).getNumberOfTargets().size();
                        }

                        if(read == view.getPlayerId()){
                            System.out.println("Non puoi selezionarti come bersaglio! Riprova >>> ");
                            scanner.nextLine();
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("Non è un numero! Riprova >>> ");
                        scanner.nextLine();
                    }

                } while (!valid);
            }

            targetsList.add(tempTargetList);

        }

        return targetsList;
    }

    /**
     * Helper method to retrieve which weapon effect the player wants to use.
     * @param w CachedFullWeapon chosen by the player to shoot (need it to read effect types/cost)
     * @return a List<Integer> representing which weapon effects the user wants to use
     */
    private List<Integer> chooseEffects(CachedFullWeapon w){

        Scanner scanner = new Scanner(System.in);

        boolean valid = false;
        List<Integer> effects = new ArrayList<>();
        int read = -1;

        do {

            System.out.println(w.getEffectsDescriptions());

            //in this case weapon has only base effect
            if(w.getEffectTypes() == null){
                valid = true;
                read = 0;
            } else {

                if (w.getEffectTypes().get(0).equals(EffectType.CONCATENABLE)) {

                    System.out.println("Seleziona gli effetti dell'arma che vuoi utilizzare: ");

                    if(w.getThirdEffectCost() != null) {

                        System.out.println("[0] -> Effetto base");
                        System.out.println("[1] -> effetto base + effetto 1");
                        System.out.println("[2] -> effetto base + effetto 2");
                        System.out.println("[12] -> effetto base + effetto 1 + effetto 2");
                        System.out.println("Inserisci il numero che rappresenta la combinazione di effetti scelta: ");

                    } else if(w.getName().equals("T.H.O.R")) {

                        System.out.println("[0] -> Effetto base");
                        System.out.println("[12] -> effetto base + effetto 1 + effetto 2");

                    } else {
                        System.out.println("[0] -> Effetto base");
                        System.out.println("[1] -> effetto base + effetto 1");
                    }

                }
                else if(w.getEffectTypes().get(0).equals(EffectType.CONCATENABLE_NON_ORD)){

                    System.out.println("Seleziona gli effetti dell'arma che vuoi utilizzare: ");

                    if(w.getThirdEffectCost() != null) {

                        System.out.println("[0] -> Effetto base");
                        System.out.println("[1] -> effetto base + effetto 1");
                        System.out.println("[2] -> effetto base + effetto 2");
                        System.out.println("[3] -> effetto 1 + effetto base");
                        System.out.println("[4] -> effetto 1 + effetto base + effetto 2");
                        System.out.println("[12] -> effetto base + effetto 1 + effetto 2");

                    } else {
                        System.out.println("[0] -> Effetto base");
                        System.out.println("[1] -> effetto base + effetto 1");
                        System.out.println("[3] -> effetto 1 + effetto base");
                    }

                    System.out.println("Inserisci il numero che rappresenta la combinazione di effetti scelta: ");

                } else if (w.getEffectTypes().get(0).equals(EffectType.ESCLUSIVE)) {

                    System.out.println("Per quest'arma puoi scegliere solo o l'effetto base o l'effetto alternativo.");
                    System.out.println("Seleziona l'effetto che vuoi utilizzare: ");
                    System.out.println("[0] -> effetto base");
                    System.out.println("[1] -> effetto 1 (alternativo)");
                }


                //differentiates valids effect choices
                try {
                    read = scanner.nextInt();
                    //scanner.nextLine();

                    if (w.getEffectTypes().get(0).equals(EffectType.CONCATENABLE)) {

                        if (w.getThirdEffectCost() != null) {

                            if ((read >= 0 && read < 3) || read == 12) {
                                valid = true;
                            } else {
                                System.out.println("Scelta effetti non valida! Riprova");
                            }

                        //only base effect or all effects togheter
                        } else if (w.getName().equals("T.H.O.R")) {

                            if (read == 0 || read == 12) {
                                valid = true;

                            } else {
                                System.out.println("Scelta effetti non valida! Riprova");
                            }

                        } else {

                            if ((read >= 0 && read < 2) || read == 12) {
                                valid = true;
                            } else {
                                System.out.println("Scelta effetti non valida! Riprova");
                            }
                        }

                    //only exclusive effects -> only base effect or only other effect
                    } else if(w.getEffectTypes().get(0).equals(EffectType.ESCLUSIVE)){

                        if((read == 0 || read == 1)){
                            valid = true;
                        } else{
                            System.out.println("Scelta effetti non valida! Riprova");
                        }

                    //can choose to do first effect before base effect
                    } else if (w.getEffectTypes().get(0).equals(EffectType.CONCATENABLE_NON_ORD)){

                        if(w.getThirdEffectCost() != null) {

                            if ((read >= 0 && read < 5) || read == 12) {
                                valid = true;
                            } else {
                                System.out.println("Scelta effetti non valida! Riprova");
                            }

                        } else {

                            if ((read == 0 || read == 1 || read == 2 || read == 12)) {
                                valid = true;
                            } else {
                                System.out.println("Scelta effetti non valida! Riprova");
                            }
                        }
                    }

                } catch (InputMismatchException e){
                    System.out.println("Non è un numero! Riprova!");
                    scanner.nextLine();
                }
            }


        } while (!valid);

        switch (read){

            //BASE EFFECT ONLY
            case 0:
                effects.add(0);
                break;

            //BASE EFFECT + EFFECT 1 if CONCATENABLE, EFFECT 1 ONLY IF EXCLUSIVE
            case 1:
                if(w.getEffectTypes().get(0).equals(EffectType.ESCLUSIVE)) {
                    effects.add(1);
                }
                else{
                    effects.add(0);
                    effects.add(1);
                }
                break;

            //BASE EFFECT + EFFECT 2
            case 2:
                effects.add(0);
                effects.add(2);
                break;

            //EFFECT 1 BEFORE BASE EFFECT
            case 3:
                effects.add(1);
                effects.add(0);
                break;

            //EFFECT 1 BEFORE BASE EFFECT + EFFECT 2
            case 4:
                effects.add(1);
                effects.add(0);
                effects.add(2);
                break;

            //BASE EFFECT + EFFECT 1 + EFFECT2
            case 12:
                effects.add(0);
                effects.add(1);
                effects.add(2);
                break;
        }

        System.out.println("[DEBUG] RISULTATO: " + effects);

        //TODO check if player can pay for the effects chosen w/ ammo/powerups

        return effects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startReload() {


        boolean isDone = false;
        boolean canReloadAgain = false;
        int reloadCount = 0;
        List<String> weapons = new ArrayList<>();


        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag() != null){
            weapons = new ArrayList<>(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag().getWeapons());
        }

        //local copy of the powerups and ammo inside cachemodel, needed to handle local checks if user can pay
        List<CachedPowerUp> cachedPowerUps = new ArrayList<>(view.getCacheModel().getCachedPlayers().get(view.getPlayerId())
                .getPowerUpBag().getPowerUpList());

        List<Color> cachedAmmoCubes = new ArrayList<>();
        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getAmmoBag() != null)
            cachedAmmoCubes = new ArrayList<>(view.getCacheModel().getCachedPlayers().get(view.getPlayerId())
                    .getAmmoBag().getAmmoList());

        //powerups and ammocubes needed to forward reload action to the controller
        List<String> weaponsToReload = new ArrayList<>();
        List<CachedPowerUp> powerUpsToDiscard = new ArrayList<>();

        System.out.println("RICARICA");
        System.out.println("Digita: ");
        System.out.println("Un tasto qualsiasi -> per cominciare la fase di ricarica");
        System.out.println("9 -> per saltare la fase di ricarica");

        int choice = -1;

        try {
            choice = scanner.nextInt();

            if (choice == 9) {
                isDone = true;
                view.doAction(new SkipAction());
                return;
            }

        } catch (InputMismatchException e) {
            scanner.nextLine();
        }

        //TODO user can reload every weapons he can in a single reload phase!
        while(!isDone && reloadCount < weapons.size()) {

            String weapToAdd = handleWeaponReload(weapons, cachedPowerUps, cachedAmmoCubes, powerUpsToDiscard);

            if(weapToAdd != null){
                weaponsToReload.add(weapToAdd);
                weapons.remove(weapToAdd);
            }

            for(String w : weapons){
                if (canReload(w, cachedAmmoCubes, cachedPowerUps)) {
                    canReloadAgain = true;
                }
            }

            if(!weapons.isEmpty() && canReloadAgain){
                System.out.println("Vuoi ricaricare ancora? si/no");
                String s = scanner.nextLine();

                if(s.toUpperCase().equals("SI")){
                    isDone = false;
                } else {
                    isDone = true;
                }
            }

            if(!canReloadAgain){
                isDone = true;
            }
        }

        //TODO forward RELOAD action to the view
        if(isFrenzy) {
            view.doAction(new FrenzyShoot(new ReloadAction(weaponsToReload, powerUpsToDiscard)));
        } else {
            view.doAction(new ReloadAction(weaponsToReload, powerUpsToDiscard));
        }
    }

    /**
     * Handle a signle weapon reload
     * @param weapons list of weapons not yet reloaded, which can be reloaded
     * @param cachedPowerUps list of powerups left in user's hand after previous handleWeaponReload
     * @param cachedAmmoCubes list of ammo left in user's hand after prevouiouse handleWeaponReload
     * @param powerUpsToDiscard powerups which the user will use as payment if he wants
     * @return a String representing the weapon name to be reloaded
     */
    private String handleWeaponReload(List<String> weapons, List<CachedPowerUp> cachedPowerUps, List<Color> cachedAmmoCubes,
                                    List<CachedPowerUp> powerUpsToDiscard){

        int read = -1;
        boolean validChoice = false;


        do {
            System.out.println("Le tue armi sono: ");
            for (int i = 0; i < weapons.size(); i++) {
                try {
                    CachedFullWeapon w = view.getCacheModel().getWeaponInfo(weapons.get(i));
                    System.out.println(i + ": " + UiHelpers.weaponTranslator(weapons.get(i)));
                    System.out.println("Costo ricarica: " + UiHelpers.ammoTranslator(w.getFirstEffectCost()));

                } catch (WeaponNotFoundException e){
                    System.out.println("[DEBUG] Weapon Not Found!");
                }
            }
            System.out.println("9 -> fine");

            System.out.println("Seleziona l'arma che vuoi ricaricare: >>> ");
            try {
                read = scanner.nextInt();
                //scanner.nextLine();

            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Non è un numero! Riprova >>> ");
            }

            //TODO check if this if works correctly with weapon size checks
            if ((read >= 0 && read < weapons.size()) || read == 9) validChoice = true;

            if(read == 9){
                validChoice = true;
                return null;
            }

        } while (!validChoice);

        String weaponName = null;

        if(canReload(weapons.get(read), cachedAmmoCubes, cachedPowerUps)){
            try {
                CachedFullWeapon w = view.getCacheModel().getWeaponInfo(weapons.get(read));
                //TODO check if this works
                //TODO add checks for powerups
                powerUpsToDiscard = checkPayWithPowerUp(w.getFirstEffectCost());

                //TODO update local powerups and ammo to calc
                cachedPowerUps.removeAll(powerUpsToDiscard);
                List<Color> weaponReloadCost = new ArrayList<>(w.getFirstEffectCost());
                for(Color c : UiHelpers.genColorListFromPowerUps(cachedPowerUps)){
                    weaponReloadCost.remove(c);
                }
                cachedAmmoCubes.removeAll(weaponReloadCost);
                weaponName = weapons.get(read);
                weapons.remove(w.getName());

            } catch (WeaponNotFoundException e){

            }
        }

        return weaponName;
    }

    /**
     *
     * @param weapon name of the weapon to be checked
     * @param ammoCubes list of ammocubes copied from cachemodel (can be modified by methods)
     * @param powerUps list of powerups copied from cachemodel (can be modified by methods, to track local changes)
     * @return true if the weapon can be reloaded with current powerups and ammo, false otherwise
     */
    private boolean canReload(String weapon, List<Color> ammoCubes, List<CachedPowerUp> powerUps){

        CachedFullWeapon w = null;

        try {
            w = view.getCacheModel().getWeaponInfo(weapon);
        } catch (WeaponNotFoundException e){

        }

        w.getFirstEffectCost();

        if(canPay(w.getFirstEffectCost(), ammoCubes, UiHelpers.genColorListFromPowerUps(powerUps))){
            return true;
        } else {
            return false;
        }
    }



    /**
     * This method will show the end game screen
     */
    @Override
    public void endGame() {

        System.out.println(ANSI_RED.escape() + "[!] GIOCO FINITO" + ANSI_RESET.escape());
        List<Player> players = view.getCacheModel().getCachedPlayers();
        List<Player> winners = new ArrayList<>();
        int maxScore = 0;

        for(Player p : players){
            String s = new String();
            s = s.concat(UiHelpers.colorAsciiTranslator(p.getPlayerColor()).escape());
            s = s.concat("\tID: " + p.getPlayerId());
            s = s.concat("\tNome: " + p.getName());
            s = s.concat("\tPunti: " + p.getStats().getScore());

            System.out.println(s);

            if(p.getStats().getScore() >= maxScore){
                maxScore = p.getStats().getScore();
            }
        }

        //TODO se vincitori hanno stesso punteggio verificare chi ha piu punti nel killshottrack
        for(Player p : players){
            if(p.getStats().getScore() == maxScore) {
                System.out.println("\n" + "\t" + UiHelpers.colorAsciiTranslator(p.getPlayerColor()).escape() +
                        "Vincitore: " + p.getPlayerId() + " : " + p.getName());
            }
        }

        System.exit(0);
    }

    /**
     * Show informations needed by the user to play his turn i.e. other players stats, weapons...
     */
    private void showInfo(){

        System.out.println("[INFO PARTITA]");
        if(view.getCacheModel().getGame() != null)
            System.out.println("Teschi: " + view.getCacheModel().showKillShotTrack(view.getCacheModel().getGame().getKillShotTrack()));

        System.out.println("Giocatori: ");

        List<Player> players = view.getCacheModel().getCachedPlayers();

        players.stream()
                .forEachOrdered(System.out::println);
    }

    /**
     * Show info needed by the user to see which weapons are avaiable in spawn cells to be bought
     */
    private void showWeapInSpawnCells(){
        System.out.println("[ARMI - SPAWN CELLS]");

        for (int i = 0; i < MAP_R; i++) {
            for (int j = 0; j < MAP_C; j++) {
                CachedCell c = view.getCacheModel().getCachedMap().getCachedCell(i, j);
                if(c != null){
                    if(c.getCellType().equals(CellType.SPAWN)){
                         CachedSpawnCell spawnCell = (CachedSpawnCell) c;

                        //need only row index to determine which spawn cell we are in
                        switch ((int) c.getPosition().getX()){
                            case 0:
                                System.out.println(AsciiColor.ANSI_BLUE.escape() + "SPAWN BLU" + AsciiColor.ANSI_RESET.escape());
                                break;

                            case 1:
                                System.out.println(AsciiColor.ANSI_RED.escape() + "SPAWN ROSSO" + AsciiColor.ANSI_RESET.escape());
                                break;

                            case 2:
                                System.out.println(AsciiColor.ANSI_YELLOW.escape() + "SPAWN GIALLO" + AsciiColor.ANSI_RESET.escape());
                                break;

                        }

                        for (int k = 0; k < spawnCell.getWeaponNames().size(); k++) {
                            try {
                                CachedFullWeapon w = view.getCacheModel().getWeaponInfo(spawnCell.getWeaponNames().get(k));
                                System.out.println("Arma " + k + " : " + UiHelpers.weaponTranslator(spawnCell.getWeaponNames().get(k)) +
                                        " Costo: " + UiHelpers.ammoTranslator(w.getBuyEffect()));
                            } catch (WeaponNotFoundException e){
                                //TODO check if it ever catch this Exception -> means there is a wrong weapon name
                                System.out.println("[DEBUG] Weapon not found! " + e.getMessage());
                            }


                        }
                    }
                }
            }
        }
    }

    @Override
    public void close() {

        System.out.println(DEFAULT_TIMER_EXPIRED);
        System.exit(0);
    }
}
