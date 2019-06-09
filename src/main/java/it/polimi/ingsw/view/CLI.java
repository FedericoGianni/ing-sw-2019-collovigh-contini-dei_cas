package it.polimi.ingsw.view;

import it.polimi.ingsw.customsexceptions.InvalidMapTypeException;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.PowerUpType;
import it.polimi.ingsw.network.ProtocolType;
import it.polimi.ingsw.utils.Protocol;
import it.polimi.ingsw.view.actions.GrabAction;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.actions.Move;
import it.polimi.ingsw.view.actions.SkipAction;
import it.polimi.ingsw.view.actions.usepowerup.GrenadeAction;
import it.polimi.ingsw.view.actions.usepowerup.NewtonAction;
import it.polimi.ingsw.view.actions.usepowerup.TeleporterAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.cachemodel.Player;
import it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;
import it.polimi.ingsw.view.cachemodel.cachedmap.FileRead;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoCell;
import it.polimi.ingsw.view.cachemodel.sendables.CachedSpawnCell;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.updates.otherplayerturn.TurnUpdate;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.map.JsonMap.MAP_C;
import static it.polimi.ingsw.model.map.JsonMap.MAP_R;
import static java.lang.Thread.sleep;

public class CLI implements UserInterface {

    private Scanner scanner = new Scanner(System.in);
    private final View view;
    private int validMove = -1;

    /**
     * Default constructor
     * @param view is the view
     */
    public CLI(View view) {
        this.view = view;
        //this.socketClientWriter = s.getScw();
    }

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
     * This function starts the ui and ask the user which protocol wants to use
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


    @Override
    public void gameSelection() {

        int choice = -1;

        do {

            System.out.println("Digita: \n 1 -> Nuova Partita \n 2 -> Riconnessione a partita già iniziata \n 3 -> Carica una partita salvata");

            choice = scanner.nextInt();
            scanner.nextLine();

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

    @Override
    public void login() {

        String playerName;
        String playerColor;
        boolean validColorChoice = false;

        System.out.println("Scegli un nome: ");
        playerName = scanner.nextLine();

        System.out.println("Seleziona un colore (GREEN, GREY, PURPLE, BLUE,YELLOW): ");

        do {
            playerColor = scanner.nextLine().toUpperCase();

            if(playerColor.equals("GREEN") || playerColor.equals("GREY") ||
                    playerColor.equals("YELLOW") || playerColor.equals("PURPLE") ||
                    playerColor.equals("BLUE")) {
                validColorChoice = true;
            } else {
                System.out.println("Please enter a valid color choice: ");
            }

        }while(!validColorChoice);

        //System.out.println("[DEBUG] PlayerName:  " + playerName);
        //System.out.println("[DEBUG] PlayerColor: " + playerColor);


        view.joinGame(playerName, PlayerColor.valueOf(playerColor.toUpperCase()));
    }

    @Override
    public void retryLogin(String error) {
        switch (error){
            case Protocol.DEFAULT_NAME_ALREADY_TAKEN_REPLY:
                System.out.println("Nome già preso.");
                break;
            case Protocol.DEFAULT_COLOR_ALREADY_TAKEN_REPLY:
                System.out.println("Colore già preso.");
                break;
            case Protocol.DEFAULT_GAME_ALREADY_STARTED_REPLY:
                System.out.println("Gioco già avviato!");
                break;
            case Protocol.DEFAULT_MAX_PLAYER_READCHED:
                System.out.println("Massimo numero di giocatori raggiunto!");
                break;
            default:
                System.out.println(" Qualcosa è andato storto");
        }
        System.out.println("Login fallito. Riprova");
        login();
    }

    @Override
    public void retryLogin(Exception e) {

        System.out.println(e.getMessage());

        System.out.println("Login fallito. Riprova");
        login();

    }

    public  void show(String s){
        /*
        new Thread(() ->
            System.out.println(s)
        ).start();*/
        System.out.println(s);
    }

    @Override
    public void notifyUpdate(UpdateType updateType, int playerId) {

        switch (updateType){

            case LOBBY:

                List<String> names = view.getCacheModel()
                        .getCachedPlayers()
                        .stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());

                System.out.println("[LOBBY] Player connessi : " + names );

                break;

            case INITIAL:
                FileRead.loadMap(view.getCacheModel().getMapType());
                System.out.println("[NOTIFICA] Ho scelto casualmente la mappa di tipo: " + view.getCacheModel().getMapType());

                break;

            case STATS:
                //TODO mostrare i cambiamenti di danni subiti e disconnessioni
                //System.out.println("[DEBUG] Ricevuto STATS update!");

                if(view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosition() != null) {
                    //new positions
                    System.out.println("[NOTIFICA] Il giocatore: " + playerId + " si è spostato!");
                    int x = view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosX();
                    int y = view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosY();

                    FileRead.removePlayer(playerId);
                    FileRead.insertPlayer(x, y, Character.forDigit(playerId, 10));
                    FileRead.showBattlefield();
                }

                break;

            case POWERUP_BAG:
                System.out.println("[NOTIFIC] POWEUPBAG update ricevuto!");
                break;

            case WEAPON_BAG:
                System.out.println("[NOTIFICA] WEAPON_BAG update ricevuto!");
                System.out.println(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag());
                break;

            case AMMO_BAG:
                System.out.println("[NOTIFICA] AMMO_BAG update ricevuto!");
                System.out.println(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getAmmoBag());
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

                                //TODO check if it works -> it should not let see ammoCard if they are picked up
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
            read = scanner.nextInt();
            scanner.nextLine();

            if (read >= 0 && read < powerUps.size()) validChoice = true;


        }while(!validChoice);

        view.spawn(powerUps.get(read));

    }

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
                    scanner.nextLine();
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

    @Override
    public void askGrenade() {

        Boolean valid;
        Integer choice;

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

            valid =false;

            System.out.println("Ti hanno sparato: vuoi usare una granata ? \n hai queste granate: ");

            for (int i = 0; i < grenades.size(); i++) {

                System.out.println( i + grenades.get(i).toString());

            }

            System.out.println("Digita il numero della carta che vuoi usare o '9' per non usarne nessuna: ");

            choice = scanner.nextInt();
            scanner.nextLine();

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

    @Override
    public void startAction() {
        //TODO consume scanner buffer if user type random numbers when waiting for its tur

        Boolean valid;
        int choice = -1;

        List<String> actions =   new ArrayList<>(Arrays.asList("MUOVI", "MUOVI E RACCOGLI", "SPARA", "SKIP"));

        do {

            valid = false;


            System.out.println("SELEZIONA UN AZIONE:");

            for (int i = 0; i < actions.size(); i++) {
                System.out.println( i + ": " + actions.get(i));
            }

            System.out.println("7: mostra mappa");
            System.out.println("8: mostra info sui giocatori");
            System.out.println("9: mostra armi nelle celle di spawn");

            try {

                choice = scanner.nextInt();
                scanner.nextLine();

            } catch (InputMismatchException e){
                System.out.println("Non è un numero: Riprova!");
                scanner.nextLine();
            }


            if ((choice >=0 && choice < actions.size()) || choice==7 || choice==8 || choice==9){
                valid = true;

            }else {
                System.out.println(" Scelta non valida: Riprova");
            }

        }while (!valid);

        switch (choice){

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

    /**
     *
     * @param s a String which is already in the right form (cardinal point to upper case)
     * @return the enum in Directions linked to the String
     */
    private Directions directionTranslator(String s){
        Directions direction;

        switch (s){
            case "NORD":
                direction = Directions.NORTH;
                break;

            case "SUD":
                direction = Directions.SOUTH;
                break;

            case "EST":
                direction = Directions.EAST;
                break;

            case "OVEST":
                direction = Directions.WEST;
                break;

            default:
                //this can never happen
                System.out.println("[DEBUG] direzione non valida!");
                direction = Directions.NORTH;
                break;
        }

        return direction;
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

    private Point genPointFromDirections(List<Directions> directions, Point start){

        Point finalPos = new Point(start);
        //generate final point destination to forward to the server
        for (Directions direction : directions) {
            switch (direction) {
                case NORTH:
                    if (finalPos.x > 0)
                        finalPos.x--;
                    break;

                case SOUTH:
                    if (finalPos.x < MAP_R-1)
                        finalPos.x++;
                    break;

                case WEST:
                    if (finalPos.y > 0)
                        finalPos.y--;
                    break;

                case EAST:
                    if (finalPos.y < MAP_C-1)
                        finalPos.y++;
                    break;
            }
        }

        return finalPos;
    }


    public void startMove(){

        int x = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosX();
        int y = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosY();
        Point startingPoint = new Point(x,y);


        //move -> always max 3 movements
        List<Directions> directionsList = handleMove(3);
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
        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getDmgTaken().size() > 2){
            directionsList = handleMove(2);
        } else {
            directionsList = handleMove(1);
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
        List<String> choice = new ArrayList<>(Collections.nCopies(2, null));

        System.out.println("ARMI IN VENDITA: ");
        for (int i = 0; i < cell.getWeaponNames().size(); i++) {
            System.out.println("Arma " + i + " : " + cell.getWeaponNames().get(i));
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
                    choice.set(1, currWeap.get(0));
                    break;

                case 1:
                    choice.set(1, currWeap.get(1));
                    break;

                case 2:
                    choice.set(1, currWeap.get(2));
                    break;

                default:
                    //this can't happen since we do valid checks before switch case
                    System.out.println("Scelta arma da scartare non valida!");
            }
        }

        switch (buy){

            case 0:
                choice.set(0, weapons.get(0));
                break;

            case 1:
                choice.set(0, weapons.get(1));
                break;

            case 2:
                choice.set(0, weapons.get(2));
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
            System.out.println("Arma " + i + " " + currWeap.get(i));
        }
    }





    public void startShoot(){
        //shoot -> se hai più di 5 danni un movimento / altrimento 0 movimenti
        //TODO Shoot
    }



    @Override
    public void startReload() {
        Boolean validChoice = false;
        int read = -1;

        List<String> weapons = new ArrayList<>();

        if(view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag() != null){
            weapons = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getWeaponbag().getWeapons();
        }

        do{
            System.out.println("Vuoi ricaricare? Le tue armi sono: ");
            showCurrWeapons();

            System.out.println("Seleziona l'arma che vuoi ricaricare: >>> ");
            try {
                read = scanner.nextInt();

            } catch (InputMismatchException e){
                scanner.nextLine();
                System.out.println("Non è un numero! Riprova >>> ");
            }

            if(read >= 0 && read <= weapons.size()) validChoice = true;

        }while(!validChoice);

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

        /*
        for (int i = 0; i < view.getCacheModel().getCachedPlayers().size(); i++) {
            System.out.print("\n" + view.getCacheModel().getCachedPlayers().get(i).getPlayerId());
            System.out.print(" :" + view.getCacheModel().getCachedPlayers().get(i).getName() + "\n");
            if(view.getCacheModel().getCachedPlayers().get(i).getStats() != null) {
                System.out.println("Danni: " + view.getCacheModel().getCachedPlayers().get(i).getStats().getDmgTaken().toString());
                System.out.println("Marchi: " + view.getCacheModel().getCachedPlayers().get(i).getStats().getMarks());
                System.out.println("Morti: " + view.getCacheModel().getCachedPlayers().get(i).getStats().getDeaths());
                System.out.println("Online: " + view.getCacheModel().getCachedPlayers().get(i).getStats().getOnline());
            }
            else {
                System.out.println("Danni: " + "[]");
                System.out.println("Marchi: " + "[]");
                System.out.println("Morti: " + " 0");
            }

            if(view.getCacheModel().getCachedPlayers().get(i).getWeaponbag() != null)
                System.out.println("Armi: " + view.getCacheModel().getCachedPlayers().get(i).getWeaponbag().getWeapons().toString());
            else
                System.out.println("Armi: " + " nessuna.\n");
        }*/
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
                            System.out.println("Arma " + k + " : " + ((CachedSpawnCell) c).getWeaponNames().get(k));
                        }


                    }
                }
            }
        }
    }

}
