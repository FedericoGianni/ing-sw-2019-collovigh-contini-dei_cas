package it.polimi.ingsw.view;

import it.polimi.ingsw.customsexceptions.InvalidMapTypeException;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.powerup.PowerUpType;
import it.polimi.ingsw.network.ProtocolType;
import it.polimi.ingsw.view.actions.JsonAction;
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

import static java.lang.Thread.sleep;

public class CLI implements UserInterface {

    private final Scanner scanner = new Scanner(System.in);
    private final View view;
    public static final String DEFAULT_NAME_ALREADY_TAKEN = "NAME_ALREADY_TAKEN";
    public static final String DEFAULT_COLOR_ALREADY_TAKEN = "COLOR_ALREADY_TAKEN";

    /**
     * Default constructor
     * @param view is the view
     */
    public CLI(View view) {
        this.view = view;
        //this.socketClientWriter = s.getScw();
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
            playerColor = scanner.nextLine();

            if(playerColor.equalsIgnoreCase("GREEN") || playerColor.equalsIgnoreCase("GREY") ||
                    playerColor.equalsIgnoreCase("YELLOW") || playerColor.equalsIgnoreCase("PURPLE") ||
                    playerColor.equalsIgnoreCase("BLUE")) {
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
            case DEFAULT_NAME_ALREADY_TAKEN:
                System.out.println("Nome già preso.");
                break;
            case DEFAULT_COLOR_ALREADY_TAKEN:
                System.out.println("Colore già preso.");
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
        new Thread(() ->
            System.out.println(s)
        ).start();
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
                //TODO mostrare quali colori hanno preso i giocatori?
                FileRead.loadMap(view.getCacheModel().getMapType());
                System.out.println("Ho scelto casualmente la mappa di tipo: " + view.getCacheModel().getMapType());

                break;

            case STATS:
                //TODO mostrare i cambiamenti nelle posizioni sulla mappa, danni subiti e disconnessioni
                System.out.println("[DEBUG] Ricevuto STATS update!");

                //new positions
                System.out.println("Il giocatore: " + playerId + " si è spostato!");
                int x = view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosX();
                int y = view.getCacheModel().getCachedPlayers().get(playerId).getStats().getCurrentPosY();

                FileRead.removePlayer(playerId);
                FileRead.insertPlayer(x, y, Character.forDigit(playerId, 10));
                FileRead.showBattlefield();
                break;

            case POWERUP_BAG:

                break;

            case WEAPON_BAG:

                break;

            case AMMO_BAG:

                break;

            case GAME:

                break;

            case SPAWN_CELL:

                break;

            case AMMO_CELL:
                //insert AmmoCard in FileRead
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (view.getCacheModel().getCachedMap().getCachedCell(i, j) != null) {
                            if (view.getCacheModel().getCachedMap().getCachedCell(i, j).getCellType().equals(CellType.AMMO)) {
                                FileRead.removeAmmoCard(i,j, (CachedAmmoCell) view.getCacheModel().getCachedMap().getCachedCell(i,j));
                                FileRead.insertAmmoCard(i, j, (CachedAmmoCell) view.getCacheModel().getCachedMap().getCachedCell(i, j));
                                //TODO check if ammo has been grabbed by some player
                                //} else if(view.getCacheModel().getCachedMap().getCachedCell(i,j).)
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
        //System.out.println("[DEBUG] startPowerUp");
        System.out.println("POWERUP PHASE");


        List<CachedPowerUp> powerUps;
        List<CachedPowerUp> usablePowerUps;
        Boolean validChoice = false;
        int read;
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
            read = scanner.nextInt();
            scanner.nextLine();

            if ((read >= 0 && read < usablePowerUps.size()) || read == 9) validChoice = true;

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


        Boolean valid;
        int choice = -1;

        List<String> actions =   new ArrayList<>(Arrays.asList("MUOVI", "MUOVI E PRENDI ", "SPARA", "SKIP"));



        do {

            valid = false;


            System.out.println("AZIONE:");
            System.out.println("Puoi:");

            for (int i = 0; i < actions.size(); i++) {

                System.out.println( i + ": " + actions.get(i));

            }


            System.out.println("8: info giocatori");
            System.out.println("9: mostra armi nelle celle di spawn");

            System.out.println("Digita il numero dell'azione che vuoi fare: ");

            try {

                choice = scanner.nextInt();
                scanner.nextLine();

            }catch (InputMismatchException e){

                System.out.println("non è un numero: Riprova!");

            }


            if ((choice >=0 && choice < actions.size()) || choice==8 || choice==9){

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

    private boolean checkValidDirection(List<Directions> d){
        //TODO check if the direction is valid (no walls, out of map.. ecc)

        switch (d.size()){

            case 0:
                //just check from player position
                int currX, currY;
                Point p = view.getCacheModel().getCachedPlayers().get(view.getPlayerId()).getStats().getCurrentPosition();
                currX = (int) p.getX();
                currY = (int) p.getY();

                //TODO need getNorth, getSouth, getEast, getWest for cachhedMap? or send every direction to server to check
                //view.getCacheModel().getCachedMap().getCachedCell(currX, currY);
                break;

            case 1:
                //case 0 check + case 1

                break;

            case 2:
                //case 0 check + case 1 check + case 2 check

                break;
        }

        return true;
    }


    public void startMove(){
        //move ->  sono sempre max 3
        int moves = 0, maxMoves = 3;
        List<Directions> directionsList = new ArrayList<>();
        boolean valid = false;
        String choice;

        do{

            scanner.reset();
            System.out.println("In che direzione ti vuoi muovere? Ti restano " + (maxMoves-moves) + " movimenti.");
            System.out.println("Inserisci una direzione (Nord, Sud, Ovest, Est) >>> ");

            do{

                choice = scanner.nextLine();
                choice = choice.toUpperCase();

                //TODO check for walls ecc.

                if((choice.equals("NORD") || choice.equals("SUD") || choice.equals("EST") || choice.equals("OVEST"))){
                    //&& checkValidDirection(directionTranslator(choice))){
                    valid = true;
                    directionsList.add(directionTranslator(choice));
                    moves++;
                } else{
                    System.out.println("Direzione non valida! Riprova");
                }

            } while(!valid);

        } while(moves < maxMoves);

        //TODO @Dav why i need to send him final position from client?
        System.out.println("[DEBUG] MOVE Preso. chiamo doACTION per inoltrare l'azione al controllelr");
        //view.doAction(new Move(directionsList));
    }

    public void startGrab(){
        //grab -> se hai più di 2 danni 2 movimenti + grab / altrimenti 1
        //TODO Grab
    }

    public void startShoot(){
        //shoot -> se hai più di 5 danni un movimento / altrimento 0 movimenti
        //TODO Shoot
    }



    @Override
    public void startReload() {
        Boolean validChoice = false;
        int read;

        do{
            System.out.println("Vuoi ricaricare? Le tue armi sono: ");
            //TODO show weapons -> need cachedWeapons
            System.out.println("Seleziona l'arma che vuoi ricaricare: >>> ");
            read = scanner.nextInt();

            //TODO check if weapon index is ok
            //if(read) validChoice = true;

        }while(!validChoice);
    }

    /**
     * Show informations needed by the user to play his turn i.e. other players stats, weapons...
     */
    private void showInfo(){

        System.out.println("[INFO PARTITA]");

        System.out.println("Giocatori: ");
        for (int i = 0; i < view.getCacheModel().getCachedPlayers().size(); i++) {
            System.out.print("\n" + view.getCacheModel().getCachedPlayers().get(i).getPlayerId());
            System.out.print(" :" + view.getCacheModel().getCachedPlayers().get(i).getName() + "\n");
            if(view.getCacheModel().getCachedPlayers().get(i).getStats() != null) {
                System.out.println("Danni: " + view.getCacheModel().getCachedPlayers().get(i).getStats().getDmgTaken().toString());
                System.out.println("Marchi: " + view.getCacheModel().getCachedPlayers().get(i).getStats().getMarks());
                System.out.println("Morti: " + view.getCacheModel().getCachedPlayers().get(i).getStats().getDeaths());
                System.out.println("Stato: " + view.getCacheModel().getCachedPlayers().get(i).getStats().getOnline());
            }
            else {
                System.out.println("Danni: " + "[]");
                System.out.println("Marchi: " + "[]");
                System.out.println("Morti: " + " 0");
            }

            if(view.getCacheModel().getCachedPlayers().get(i).getWeaponbag() != null)
                System.out.println("Armi: " + view.getCacheModel().getCachedPlayers().get(i).getWeaponbag().getWeapons().toString());
            else
                System.out.println("Armi: " + " nessuna.");
        }
    }

    /**
     * Show info needed by the user to see which weapons are avaiable in spawn cells to be bought
     */
    private void showWeapInSpawnCells(){
        System.out.println("[ARMI - SPAWN CELLS]");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
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
