package it.polimi.ingsw.view;

import it.polimi.ingsw.customsexceptions.InvalidMapTypeException;
import it.polimi.ingsw.network.ProtocolType;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PowerUpType;
import it.polimi.ingsw.utils.Protocol;
import it.polimi.ingsw.view.actions.*;
import it.polimi.ingsw.view.actions.usepowerup.GrenadeAction;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.actions.usepowerup.TeleporterAction;
import it.polimi.ingsw.view.cachemodel.CachedFullWeapon;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.cachemodel.Player;
import it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;
import it.polimi.ingsw.view.cachemodel.cachedmap.FileRead;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoCell;
import it.polimi.ingsw.view.cachemodel.sendables.CachedSpawnCell;
import it.polimi.ingsw.view.exceptions.WeaponNotFoundException;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.updates.otherplayerturn.TurnUpdate;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.map.JsonMap.MAP_C;
import static it.polimi.ingsw.model.map.JsonMap.MAP_R;
import static it.polimi.ingsw.utils.DefaultReplies.DEFAULT_CANNOT_BUY_WEAPON;
import static it.polimi.ingsw.view.UiHelpers.*;
import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.*;
import static java.lang.Thread.sleep;

public class CLI implements UserInterface {

    private Scanner scanner = new Scanner(System.in);
    private final View view;
    private int validMove = -1;
    private Object obj = new Object();

    /**
     * Default constructor
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
        if(validMove){
            this.validMove = 1;
        } else {
            this.validMove = 0;
        }
        synchronized(this) {
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
            }catch (InputMismatchException e){
                System.out.println("Valore non valido. Riprova!");
            }

            scanner.nextLine();

        }while (!(connectionType == 1 || connectionType == 2));

        switch (connectionType){

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
            } catch (InputMismatchException e){
                System.out.println("Non è un numero! Riprova: ");
                scanner.nextLine();
            }

        }while (!(choice == 1 || choice == 2 || choice == 3));


        switch (choice){

            case 1:
                login();
                break;

            case 2:
                // TODO reconnect
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

            if(playerColor.equals("VERDE") || playerColor.equals("GRIGIO") ||
                    playerColor.equals("GIALLO") || playerColor.equals("VIOLA") ||
                    playerColor.equals("BLU")) {
                validColorChoice = true;
            } else {
                System.out.println("Colore non valido. Riprova:  ");
            }

        }while(!validColorChoice);

        //System.out.println("[DEBUG] PlayerName:  " + playerName);
        //System.out.println("[DEBUG] PlayerColor: " + playerColor);


        view.joinGame(playerName, UiHelpers.colorTranslator(playerColor.toUpperCase()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void retryLogin(String error) {
        switch (error){
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
     * {@inheritDoc}
     */
    public synchronized void show(String s){

            System.out.println(s);

            //if weapon buy has failed, re-sync the local cli map with real player position
            if (s.equals(DEFAULT_CANNOT_BUY_WEAPON)) {
                FileRead.removePlayer(view.getPlayerId());
                Point p = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosition();
                FileRead.insertPlayer(p.x, p.y, Character.forDigit(view.getPlayerId(), 10));
                FileRead.showBattlefield();
            }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyUpdate(UpdateType updateType, int playerId) {

        switch (updateType){

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

                if(view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosition() != null) {
                    //new positions
                    System.out.println(ANSI_GREEN.escape() + "[!] Il giocatore: " + playerId + " si è spostato!" + ANSI_RESET.escape());
                    int x = view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosX();
                    int y = view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosY();

                    FileRead.removePlayer(playerId);
                    FileRead.insertPlayer(x, y, Character.forDigit(playerId, 10));
                    FileRead.showBattlefield();
                }

                //player disconnected
                if(!view.getCacheModel().getCachedPlayers().get(playerId).getStats().getOnline()){
                    show(ANSI_GREEN.escape() + "[!] Il giocatore: " + playerId + " si è disconnesso!" + ANSI_RESET.escape());
                }

                if(view.getCacheModel().getCachedPlayers().get(playerId).getStats().getDmgTaken() != null){
                    //TODO show dmg updates or let the user sees them from player info?
                }

                break;

            case POWERUP_BAG:
                //System.out.println("[NOTIFICA] POWEUPBAG update ricevuto!");
                break;

            case WEAPON_BAG:
                System.out.println("[NOTIFICA] WEAPON_BAG update ricevuto!");
                //show update only on the player who has bought the weapon
                if(playerId == view.getPlayerId()) {
                    System.out.println(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag());
                }
                break;

            case AMMO_BAG:
                //System.out.println("[NOTIFICA] AMMO_BAG update ricevuto!");
                if(playerId == view.getPlayerId()) {
                    System.out.println(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getAmmoBag());
                }
                break;

            case GAME:
                System.out.println("[NOTIFICA] GAME update ricevuto!");
                break;

            case SPAWN_CELL:
                System.out.println("[NOTIFICA] SPAWN_CELL update ricevuto!");
                break;

            case AMMO_CELL:
                //insert AmmoCard in FileRead
                for (int i = 0; i < MAP_R; i++) {
                    for (int j = 0; j < MAP_C; j++) {
                        if (view.getCacheModel().getCachedMap().getCachedCell(i, j) != null) {
                            if (view.getCacheModel().getCachedMap().getCachedCell(i, j).getCellType().equals(CellType.AMMO)) {

                                FileRead.removeAmmoCard(i,j);

                                //if ammoCell has been picked up don't show it on map
                                if(!((CachedAmmoCell) view.getCacheModel().getCachedMap().getCachedCell(i,j)).getAmmoList().isEmpty()) {
                                    FileRead.insertAmmoCard(i, j, (CachedAmmoCell) view.getCacheModel().getCachedMap().getCachedCell(i, j));
                                }

                            }
                        }
                    }
                }

                break;

            case TURN:


                break;

            default:

                break;
        }


    }

    private void notifyTurnUpdate(TurnUpdate turnUpdate){

        switch (turnUpdate.getActionType()){

            case POWERUP:

                //TODO

                break;

            case SHOOT:

                //TODO

                break;

            case GRAB:

                //TODO

                break;

            case MOVE:

                //TODO

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

        while(view.getCacheModel().getCachedMap() == null){
            System.out.println("Attendi ricezione del tipo di mappa...");
            try{
                sleep(200);
            } catch (Exception e){

            }
        }

        try {
            FileRead.populateMatrixFromFile(view.getCacheModel().getMapType());
        } catch(InvalidMapTypeException e){
            System.out.println("[DEBUG] Errore nel caricamento della mappa: \n" + e.getMessage());
        }

        FileRead.showBattlefield();
        showInfo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSpawn() {
        //TODO consume scanner buffer if user type random numbers when waiting for its turn

        List<CachedPowerUp> powerUps;

        Boolean validChoice = false;
        int read;

        System.out.println("SPAWN PHASE");

        do{

            while(view.getCacheModel().getCachedPlayers().size() <= 0) {
                System.out.println("Attendi ricezione dell'update iniziale...");

                try {
                    sleep(200);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            while (view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getPowerUpBag().getPowerUpList().isEmpty()){

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
                System.out.println( i + " " + powerUps.get(i).toString());

            }

            System.out.println("Scegli un powerUp da scartare: ");

            try {
                read = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e){
                System.out.println("Non è un numero! Riprova: ");
                scanner.nextLine();
                read = -1;
            }

            if (read >= 0 && read < powerUps.size()) validChoice = true;


        }while(!validChoice);

        view.spawn(powerUps.get(read));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startPowerUp(){
        //TODO consume scanner buffer if user type random numbers when waiting for its turn

        //System.out.println("[DEBUG] startPowerUp");
        System.out.println("POWERUP PHASE");


        List<CachedPowerUp> powerUps;
        List<CachedPowerUp> usablePowerUps;
        Boolean validChoice = false;
        int read = -1;
        scanner.reset();

        do{

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
                    .filter( x -> (( x.getType() != PowerUpType.TAG_BACK_GRENADE ) && ( x.getType() != PowerUpType.TARGETING_SCOPE )))
                    .collect(Collectors.toList());


            System.out.println("Hai questi PowerUp:");

            for (int i = 0; i < powerUps.size(); i++) {
                System.out.println( i + " :" + powerUps.get(i).toString());
            }

            System.out.println("Puoi usare uno di questi:");
            for (int i = 0; i < usablePowerUps.size(); i++) {
                System.out.println(i + " :" + usablePowerUps.get(i).toString());
            }

            System.out.println("9 -> non usare powerUp");
            scanner.reset();
            System.out.println("Scegli un powerUp da usare: ");

            while(read == -1) {
                try {
                    read = scanner.nextInt();
                    //scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Non è un numero! Riprova");
                    scanner.nextLine();
                }
            }

            if ((read >= 0 && read < usablePowerUps.size()) || read == 9) validChoice = true;
            else scanner.nextLine();

        }while(!validChoice);

        if ( read == 9){
            // if the user types 9 -> end of powerUp phase
            view.doAction(new SkipAction());

        }else if(read == 8){
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
                .filter( x ->  x.getType().equals(PowerUpType.TAG_BACK_GRENADE))
                .collect(Collectors.toList());

        do {

            valid = false;

            System.out.println("Ti hanno sparato: vuoi usare una granata ? \n hai queste granate: ");

            for (int i = 0; i < grenades.size(); i++) {

                System.out.println( i + grenades.get(i).toString());

            }

            System.out.println("Digita il numero della carta che vuoi usare o '9' per non usarne nessuna: ");

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e){
                System.out.println("Non è un numero! Riprova: ");
                scanner.nextLine();
            }

            if (choice == 9 || (choice >= 0 && choice < 3 )) valid = true;


        }while (!valid);

        // if the player choose not to use grenades a grenadeAction will be also sent, but will have color null

        if (choice == 9) view.doAction(new GrenadeAction(null,view.getPlayerId()));
        else view.doAction(new GrenadeAction(grenades.get(choice).getColor(),view.getPlayerId()));



    }

    private void usePowerUp(CachedPowerUp powerUp){

        switch (powerUp.getType()){

            case NEWTON:
                useNewton(powerUp);
                break;

            case TELEPORTER:
                useTeleporter(powerUp);
                break;

            case TAG_BACK_GRENADE:
                break;

            case TARGETING_SCOPE:
                break;

            default:
                break;

        }

    }

    private void useNewton(CachedPowerUp newton){

        String retryMessage = "Scelta non valida. Riprova.";

        Boolean validChoice = false;
        int player;
        String direction;
        int amount;


        do {

            validChoice = false;

            System.out.println("Su quale giocatore vuoi usare Newton? >>> ");
            player = scanner.nextInt();
            scanner.nextLine();

            if(player >= 0 && player <= view.getCacheModel().getCachedPlayers().size())
                validChoice = true;
            else
                System.out.println(retryMessage);
        }while(!validChoice);



        do{

            validChoice = false;

            System.out.println("In quale direzione vuoi spostare il player selezionato? (Nord, Sud, Ovest, Est) >>> ");
            direction = scanner.next().toUpperCase();
            scanner.nextLine();

            if(direction.equals("NORD") || direction.equals("SUD")
                    || direction.equals("EST") || direction.equals("OVEST"))
                validChoice = true;
            else
                System.out.println(retryMessage);
        }while(!validChoice);


        do{
            validChoice = false;

            System.out.println("Di quanto vuoi muovere il player? (1,2) >>> ");
            amount = scanner.nextInt();
            scanner.nextLine();

            if(amount == 1 || amount == 2 )

                validChoice = true;

            else

                System.out.println(retryMessage);

        }while(!validChoice);

        // Create a NewtonAction object

        NewtonAction newtonAction = new NewtonAction(newton.getColor(),player,amount,directionTranslator(direction));

        // sends it to the Virtual view

        view.doAction(newtonAction);
    }

    private void useTeleporter(CachedPowerUp teleporter){

        Boolean validChoice;
        int r;
        int c;

        do{

            validChoice = false;

            System.out.println("In quale cella vuoi andare ? >>> ");

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

            if(view.getCacheModel().getCachedMap().getCachedCell(r,c) == null){

                System.out.println("Cella non esistente. Riprova: ");

            } else {

                validChoice = true;

            }

        }while(!validChoice);



        JsonAction jsonAction = new TeleporterAction(teleporter.getColor(),new Point(c,r));

        view.doAction(jsonAction);


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAction() {
        //TODO consume scanner buffer if user type random numbers when waiting for its tur

        Boolean valid;
        int choice = -1;

        List<String> actions =   new ArrayList<>(Arrays.asList("MUOVI", "MUOVI E RACCOGLI", "SPARA", "SKIP"));

            do {

                valid = false;

                //NOTE: startAction uses show instead of system.out.println because there's a chance that
                //when the controller reply back with a message the showBattlefield method and the startAction method
                //and the map won't be displayed correctly

                show("SELEZIONA UN AZIONE:");

                for (int i = 0; i < actions.size(); i++) {
                    System.out.println(i + ": " + actions.get(i));
                }

                show("7: mostra mappa");
                show("8: mostra info sui giocatori");
                show("9: mostra armi nelle celle di spawn");

                try {
                    scanner.reset();
                    choice = scanner.nextInt();
                    scanner.nextLine();

                } catch (InputMismatchException e) {
                    System.out.println("Non è un numero: Riprova!");
                    scanner.nextLine();
                }


                if ((choice >= 0 && choice < actions.size()) || choice == 7 || choice == 8 || choice == 9) {
                    valid = true;

                } else {
                    System.out.println(" Scelta non valida: Riprova");
                }

            } while (!valid);

            switch (choice) {

                case 0:
                    startMove();
                    break;

                case 1:
                    startGrab();
                    break;

                case 2:
                    startShoot();
                    break;

                case 3:
                    view.doAction(new SkipAction());
                    break;

                case 7:
                    FileRead.showBattlefield();
                    startAction();
                    break;

                case 8:
                    showInfo();
                    startAction();
                    break;

                case 9:
                    showWeapInSpawnCells();
                    startAction();
                    break;

                default:

                    System.out.println("Azione non esistente");
                    break;
                }
    }

    private List <Directions> handleMove(int maxMoves){

        int moves = 0;
        List<Directions> directionsList = new ArrayList<>();
        boolean valid = false;
        String choice;
        Point temp_moves;
        int x;
        int y;
        List <Directions> previous = new ArrayList<>();

        do{

            System.out.println("In che direzione ti vuoi muovere? Ti restano " + (maxMoves-moves) + " movimenti.");
            System.out.println("Inserisci una direzione (Nord, Sud, Ovest, Est, Stop per fermarti qui) >>> ");

            do{
                choice = scanner.nextLine();
                choice = choice.toUpperCase();

                x = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosX();
                y = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosY();
                temp_moves = new Point(x,y);

                //TODO check for walls ecc.
                if (choice.equals("STOP")) {
                    return directionsList;
                }


                if((choice.equals("NORD") || choice.equals("SUD") || choice.equals("EST") || choice.equals("OVEST"))){

                    //validMove = -1;
                    //p =view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosition();

                    //System.out.println("[CLI] ask to server if move: " + directionTranslator(choice) + " from pos: " +  x + ", "  + y);

                    //System.out.println( "[CLI] cell: " + view.getCacheModel().getCachedMap().getCachedCell( p.x, p.y).getCellType() );

                    Directions d = directionTranslator(choice);

                    Point start = new Point(x,y);
                    Point finalPos = genPointFromDirections(previous, start);

                    validMove = -1;
                    view.askMoveValid(finalPos.x, finalPos.y, d);

                    while(validMove == -1){
                        try
                        {
                            synchronized(this) {
                                //System.out.println("Waiting to receive validMove reply...");
                                this.wait();
                            }

                        } catch (InterruptedException e) {

                        }
                        //System.out.println("Received validMove reply!");
                    }

                    //System.out.println("[DEBUG] controllo valid Move...");
                    if(validMove == 1) {
                        System.out.println("Direzione valida!");
                        valid = true;
                        previous.add(d);
                        directionsList.add(directionTranslator(choice));
                        moves++;

                        //update map view with single movements
                        temp_moves = genPointFromDirections(directionsList, temp_moves);
                        FileRead.removePlayer(view.getPlayerId());
                        FileRead.insertPlayer(temp_moves.x, temp_moves.y, Character.forDigit(view.getPlayerId(), 10));
                        FileRead.showBattlefield();

                    }else if (validMove == 0){

                        System.out.println("Direzione non valida! Riprova >>> ");
                        valid = false;
                    }

                } else{
                    System.out.println("Scrivi correttamente la direzione dei punti cardinali! Riprova");
                }

            } while(!valid);

        } while(moves < maxMoves);

        return directionsList;
    }




    public void startMove(){

        int x = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosX();
        int y = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosY();
        Point startingPoint = new Point(x,y);


        //move -> always max 3 movements
        List<Directions> directionsList = handleMove(DEFAULT_MAX_NORMAL_MOVES);
        Point finalPos = genPointFromDirections(directionsList, startingPoint);

        //System.out.println("[DEBUG] MOVE Preso. chiamo doACTION per inoltrare l'azione al controllelr");
        view.doAction(new Move(directionsList, finalPos));
    }

    private void startGrab(){

        int x = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosX();
        int y = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosY();
        Point startingPoint = new Point(x,y);

        List<Directions> directionsList;

        //grab -> if player has more than 2 dmg -> 2 moves else -> only 1 move
        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getDmgTaken().size() >= DEFAULT_DMG_TO_UNLOCK_ENHANCED_GRAB){
            directionsList = handleMove(DEFAULT_ENHANCED_MOVES_WITH_GRAB);
        } else {
            directionsList = handleMove(DEFAULT_MOVES_WITH_GRAB);
        }

        Point finalPos = genPointFromDirections(directionsList, startingPoint);

        CellType cellType = view.getCacheModel().getCachedMap().getCachedCell(finalPos.x, finalPos.y).getCellType();

        switch (cellType){

            case AMMO:
                view.doAction(new GrabAction(directionsList));
                break;

            case SPAWN:
                List<String> weapons = handleWeaponGrab(finalPos);
                view.doAction(new GrabAction(directionsList, weapons.get(0), weapons.get(1)));
                break;
        }
    }

    /**
     * Handle weapons grab: if player has already 3 weapons he has to choose 1 to discard
     * @param pos current spawn cell from where he wants to buy the weapon
     * @return a List of String representing the name of the weapon to buy and, if needed, the one to discard
     */
    //TODO i need to check that he buy weap when i have more ammo and that it let me choose one to discard when i have already 3
    private List<String> handleWeaponGrab(Point pos){

        CachedSpawnCell cell = (CachedSpawnCell) view.getCacheModel().getCachedMap().getCachedCell(pos.x, pos.y);

        List<String> weapons = cell.getWeaponNames();
        List<String> choice = new ArrayList<>();

        System.out.println("ARMI IN VENDITA: ");
        for (int i = 0; i < cell.getWeaponNames().size(); i++) {
            System.out.println("Arma " + i + " : " + UiHelpers.weaponTranslator(cell.getWeaponNames().get(i)));
        }

        System.out.println("Digita il numero dell'arma che vuoi acquistare >>> ");
        int buy = -1, discard = -1;
        boolean valid = false;
        scanner.reset();

        do {

            scanner.reset();

            try {
                buy = scanner.nextInt();

                if(buy >= 0 && buy <= weapons.size()) {
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
        List <String> currWeap = new ArrayList<>();
        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag() != null){
            currWeap = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag().getWeapons();
        }

        if(currWeap.size() >= 3){
            System.out.println("Hai già 3 armi. Scegli un arma da scartare >>>");

            showCurrWeapons();

            System.out.println("Seleziona il numero dell'arma che vuoi scartare >>> ");
            valid = false;

            do {

                scanner.nextLine();
                scanner.reset();

                try {
                    discard = scanner.nextInt();

                    if(discard >= 0 && discard <= currWeap.size()){
                        valid = true;
                    } else {
                        System.out.println("Numero non valido! Riprova >>> ");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Non è un numero! Riprova >>> ");
                }

            }while (!valid);

            switch (discard){

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


        switch (buy){

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

    private void showCurrWeapons(){

        List<String> currWeap = new ArrayList<>();

        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag() != null){
            currWeap = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag().getWeapons();
        }

        for (int i = 0; i < currWeap.size(); i++) {
            System.out.println("Arma " + i + " " + UiHelpers.weaponTranslator(currWeap.get(i)));
        }
    }


    private void startShoot(){

        boolean valid = false;
        int choice = -1;
        List<Directions> directionsList = new ArrayList<>();
        CachedFullWeapon weapon = null;

        //SHOOT ACTION requirements
        List<List<Integer>> targetList;
        List<Integer> effects = new ArrayList<>();
        List<Point> cells = new ArrayList<>();

        //weapon checks
        int targets = -1; //number of targets needed by the weapons (reads this from json CachedFullWeapons)
        boolean needCell = false; //if this weapon needs a Cell for movements related to the shoot
        //second and third effect type -> ECLUSIVE/CONCATENABLE need an enum class for this

        int playerDmg = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getDmgTaken().size();

        //if player has > 5 dmg he can do one movement, otherwise no moves
        if(playerDmg >= DEFAULT_DMG_TO_UNLOCK_ENHANCED_SHOOT){
            directionsList = handleMove(DEFAULT_ENHANCED_MOVES_WITH_SHOOT);
        }

        Point startingPoint = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosition();
        Point finalPos = genPointFromDirections(directionsList, startingPoint);

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
                if(choice >= 0 && choice <= weaponBagSize){
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

        //System.out.println(ANSI_BLUE.escape() + "[DEBUG] ARMA SCELTA: " + weapon.getName() + ANSI_RESET.escape());

        // PRE-SHOOT PHASE choose wep effects, checks if he can pay
        // choose target/s and additional info needed to shoot with a particular weapon
        //TODO user needs to choose weapons effect to use (check if he can pay w/ ammo/powerups

        //TODO local checks with the attributes read from json (i.e. numtarget, samecell, ...)

        //TODO method which takes effect type as parameter (or CachedFullWeapon) and returns List<Int> with effects chosen
        //TODO also checks if he can pay this effects (w/ ammo/powerUps)
        //effects = chooseEffects();
        effects.add(0);
        targetList = chooseTargets(1,1);


        //forward the shoot action to the controller -> if shoot fails it won't do the shoot and
        //let the user retry the shoot specifiying why shoot has failed

        //just to test a basic shoot

        view.doAction(new ShootAction("LOCK RIFLE", targetList, effects, cells));

    }

    /**
     * Helper method needed by startShoot to collect target/s to be shot
     * @return a List of List<Integer> representing the targets for each of the weapon effect
     * (index 0 -> base effect, index 1 -> second effect, index 2 -> third effect)
     */
    private List<List<Integer>> chooseTargets(int targetsNum, int effectsNum){

        List<List<Integer>> targetsList = new ArrayList<>();
        boolean valid = false;

        for (int i = 0; i < effectsNum; i++) {

            List<Integer> tempTargetList = new ArrayList<>();

            System.out.println("Effetto: " + i);

            for (int j = 0; j < targetsNum; j++) {

                do {

                    int read = -1;
                    System.out.println("Seleziona un bersaglio (ID) >>> ");

                    try {
                        read = scanner.nextInt();

                        if(read >= 0 && read <= view.getCacheModel().getCachedPlayers().size()){
                            valid = true;
                            tempTargetList.add(read);
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

        List<Integer> effects = new ArrayList<>();

        //TODO ask the player to choose which weapon effect to activate
        //TODO check if effects are exclusive/concatenable
        //TODO check if player can pay for the effects chosen w/ ammo/powerups

        return effects;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void startReload() {
        boolean validChoice = false;
        int read = -1;

        List<String> weapons = new ArrayList<>();

        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag() != null){
            weapons = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag().getWeapons();
        }

        //TODO user can reload every weapons he can in a single reload phase!

        do{
            System.out.println("Vuoi ricaricare? Le tue armi sono: ");
            showCurrWeapons();
            System.out.println("9 -> non ricaricare.");

            System.out.println("Seleziona l'arma che vuoi ricaricare: >>> ");
            try {
                read = scanner.nextInt();

            } catch (InputMismatchException e){
                scanner.nextLine();
                System.out.println("Non è un numero! Riprova >>> ");
            }

            if((read >= 0 && read <= weapons.size()) || read == 9) validChoice = true;

        }while(!validChoice);

        if(read == 9){
            view.doAction(new SkipAction());
        }

        //TODO forward RELOAD action to the view
    }

    /**
     * Show informations needed by the user to play his turn i.e. other players stats, weapons...
     */
    private void showInfo(){

        System.out.println("[INFO PARTITA]");

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
                         c = (CachedSpawnCell) c;

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
                        for (int k = 0; k < ((CachedSpawnCell) c).getWeaponNames().size(); k++) {
                            System.out.println("Arma " + k + " : " + UiHelpers.weaponTranslator(((CachedSpawnCell) c).getWeaponNames().get(k)));
                        }
                    }
                }
            }
        }
    }
}
