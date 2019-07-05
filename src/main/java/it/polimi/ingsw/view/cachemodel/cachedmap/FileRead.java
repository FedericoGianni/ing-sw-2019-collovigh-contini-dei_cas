package it.polimi.ingsw.view.cachemodel.cachedmap;

import it.polimi.ingsw.customsexceptions.InvalidMapTypeException;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoCell;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.view.cachemodel.cachedmap.AsciiColor.*;

/**
 * Helpers Class to read the CLI map from a file using a defined character convetion
 * The Map is read from a file and populates a matrix of char, which can then be manipulated to update the players and other objects
 * inside the map to have an interactive version of the CLI map
 */
public class FileRead {

    /**
     * Max row for the map matrix of chars
     */
    public static final int R = 19;

    /**
     * Max cols for the map matrix of chars
     */
    public static final int C = 49;

    /**
     * Max row for the matrix of chars to store welcome cli message
     */
    public static final int R_W = 4;


    /**
     * Max cols for the matrix of chars to store welcome cli message
     */
    public static final int C_W = 52;

    /**
     * matrix of chars representing the cli map
     */
    private static char[][] battelfield = new char[R][C];

    /**
     * matrix of chars to represent the cli welcome screen
     */
    private static char[][] welcome = new char[R_W][C_W];

    /**
     * list of ascii color to change the player colors accordingly to the color they have chosen at game start
     */
    private static List<AsciiColor> playerColorList = new ArrayList<>();

    /**
     * Read the map matrix from file
     * @param mapType maptype to be read 1,2,3
     * @throws InvalidMapTypeException
     */
    public static void populateMatrixFromFile(int mapType) throws InvalidMapTypeException {
        try {
            // Open the file that is the first
            // command line parameter
            String path;
            switch(mapType){

                case 1:
                    path = new File("/map/map1.txt").getAbsolutePath();
                    break;
                case 2:
                    path = new File("/map/map2.txt").getAbsolutePath();
                    break;
                case 3:
                    path = "/map/map3.txt";
                    break;
                default:
                    throw new InvalidMapTypeException();
            }
            //FileInputStream fstream = new FileInputStream(path);
            InputStream inputStream = FileRead.class.getResourceAsStream(path);
            // Get the object of DataInputStream
            //DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String strLine;
            //Read File Line By Line
            int r = 0;
            int cl = 0;
            while ((strLine = br.readLine()) != null) {

                // Print the content on the console
                for (int i = 0; i < C-1; i++) {
                    if(strLine.charAt(i) == '\n'){
                        battelfield[r][i] = '\n';
                    } else
                        battelfield[r][i] = strLine.charAt(i);
                }
                r++;
            }

            //Close the input stream
            //in.close();
            inputStream.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * generate a random int with a lower and upper bound to place the player inside a random position in one cell
     * of the char matrix
     * @param lower lower bound of rand int returned
     * @param upper upper bound of the rand int returned
     * @return an integer between lower and upper bound
     */
    private static int genRandInt(int lower, int upper){
        Random rand = new Random();
        return rand.nextInt(upper-lower) + lower;
    }

    /**
     * insert an ammo card in the specified cell
     * @param x row of the cell in the map matrix
     * @param y col of the cell in the map matrix
     * @param c cached ammo cell to put inside the map
     */
    //call this function from UPDATE of type AmmoCell
    public static void insertAmmoCard(int x, int y, CachedAmmoCell c){

        List<Character> v = generateAmmoCard(c.getAmmoList());
        insertAmmoCard(x, y, v);
    }


    private static void insertAmmoCard(int x, int y, List<Character> v){

        if(x < 0 || x > 2 || y < 0 || y > 3)
            return;


        int r = -1;
        int c = -1;
        Boolean validPos = false;

        do {
            //rows
            switch (x) {
                case 0:
                    r = genRandInt(0, 5);
                    break;

                case 1:
                    r = genRandInt(8, 11);
                    break;

                case 2:
                    r = genRandInt(14, 17);
                    break;
            }

            //cols
            switch (y) {
                case 0:
                    c = genRandInt(1, 10);
                    break;
                case 1:
                    c = genRandInt(13, 22);
                    break;
                case 2:
                    c = genRandInt(23, 32);
                    break;
                case 3:
                    c = genRandInt(35, 44);
                    break;
            }

            int validCount = 0;

            for (int i = 0; i < v.size(); i++) {
                if (battelfield[r][c+i] == ' ') {
                    validCount++;
                }
            }

            if(validCount == v.size()){
                validPos = true;
            }

        }while(!validPos);

        for (int i = 0; i < v.size(); i++) {
            battelfield[r][c + i] = v.get(i);
        }

    }

    public static void removeAmmoCard(int x, int y, CachedAmmoCell c){
        List<Character> v = generateAmmoCard(c.getAmmoList());
        removeAmmoCard(x, y, v);
    }

    public static void removeAmmoCard(int x, int y){
        List<Character> v = new ArrayList<>();
        v.add('à');
        v.add('è');
        v.add('ò');
        removeAmmoCard(x,y,v);
    }

    private static void removeAmmoCard(int x, int y, List<Character> v){

        int r_min = -1, r_max = -1;
        int c_min = -1, c_max = -1;

        switch (x) {
            case 0:
                r_min = 0;
                r_max = 5;
                break;

            case 1:
                r_min = 8;
                r_max = 11;
                break;

            case 2:
                r_min = 14;
                r_max = 17;
                break;
        }

        //cols
        switch (y) {
            case 0:
                c_min = 1;
                c_max = 10;
                break;
            case 1:
                c_min = 13;
                c_max = 22;
                break;
            case 2:
                c_min = 23;
                c_max = 34;
                break;
            case 3:
                c_min = 35;
                c_max = 45;
                break;
        }

        for (int i = r_min; i <= r_max; i++) {
            for (int j = c_min; j <= c_max; j++) {
                if(battelfield[i][j] == v.get(0) || battelfield[i][j] == v.get(1) || battelfield[i][j] == v.get(2) || battelfield[i][j] == '+'){
                    battelfield[i][j] = ' ';
                }
            }
        }
    }

    private static List<Character> generateAmmoCard(List <Color> ammoCubeList) {

        List<Character> charList = new ArrayList<>();

        for (int i = 0; i < ammoCubeList.size(); i++) {

            switch (ammoCubeList.get(i)) {
                case BLUE:
                    charList.add('à');
                    break;

                case RED:
                    charList.add('ò');
                    break;

                case YELLOW:
                    charList.add('è');
                    break;
            }
        }

        //add PowerUp to the ammoCard
        if (ammoCubeList.size() == 2) {
            charList.add('+');
        }

        return charList;
    }

    public static void insertPlayer(int x, int y, char id){

        if(x < 0 || x > 2 || y < 0 || y > 3)
           return;


        int r = -1;
        int c = -1;

        do {
            //rows
            switch (x) {
                case 0:
                    r = genRandInt(0, 5);
                    break;

                case 1:
                    r = genRandInt(8, 11);
                    break;

                case 2:
                    r = genRandInt(14, 17);
                    break;
            }

            //cols
            switch (y) {
                case 0:
                    c = genRandInt(1, 10);
                    break;
                case 1:
                    c = genRandInt(13, 22);
                    break;
                case 2:
                    c = genRandInt(23, 32);
                    break;
                case 3:
                    c = genRandInt(35, 44);
                    break;
            }

        }while(battelfield[r][c] != ' ');


        battelfield[r][c] = id;
    }

    public static void removePlayer(int id){

        char c = Character.forDigit(id, 10);
        //System.out.println("id: " + Character.forDigit(id, 10));

        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                if(battelfield[i][j] == c){
                    battelfield[i][j] = ' ';
                }
            }
        }
    }

    public static void showBattlefield(){
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                switch(battelfield[i][j]) {

                    case 'H':
                        System.out.print(ANSI_RED.escape() + '╔' + ANSI_RESET.escape());
                        break;
                    case 'J':
                        System.out.print(ANSI_RED.escape() + '╗' + ANSI_RESET.escape());
                        break;
                    case 'K':
                        System.out.print(ANSI_RED.escape() + '╚' + ANSI_RESET.escape());
                        break;
                    case 'L':
                        System.out.print(ANSI_RED.escape() + '╝' + ANSI_RESET.escape());
                        break;
                    case 'h':
                        System.out.print(ANSI_BLUE.escape() + '╔' + ANSI_RESET.escape());
                        break;
                    case 'j':
                        System.out.print(ANSI_BLUE.escape() + '╗' + ANSI_RESET.escape());
                        break;
                    case 'k':
                        System.out.print(ANSI_BLUE.escape() + '╚' + ANSI_RESET.escape());
                        break;
                    case 'l':
                        System.out.print(ANSI_BLUE.escape() + '╝' + ANSI_RESET.escape());
                        break;
                    case 'Q':
                        System.out.print(ANSI_YELLOW.escape() + '╔' + ANSI_RESET.escape());
                        break;
                    case 'U':
                        System.out.print(ANSI_YELLOW.escape() + '╗' + ANSI_RESET.escape());
                        break;
                    case 'E':
                        System.out.print(ANSI_YELLOW.escape() + '╚' + ANSI_RESET.escape());
                        break;
                    case 'T':
                        System.out.print(ANSI_YELLOW.escape() + '╝' + ANSI_RESET.escape());
                        break;
                    case 'q':
                        System.out.print(ANSI_WHITE.escape() + '╔' + ANSI_RESET.escape());
                        break;
                    case 'u':
                        System.out.print(ANSI_WHITE.escape() + '╗' + ANSI_RESET.escape());
                        break;
                    case 'e':
                        System.out.print(ANSI_WHITE.escape() + '╚' + ANSI_RESET.escape());
                        break;
                    case 't':
                        System.out.print(ANSI_WHITE.escape() + '╝' + ANSI_RESET.escape());
                        break;
                    case 'A':
                        System.out.print(ANSI_GREEN.escape() + '╔' + ANSI_RESET.escape());
                        break;
                    case 'S':
                        System.out.print(ANSI_GREEN.escape() + '╗' + ANSI_RESET.escape());
                        break;
                    case 'D':
                        System.out.print(ANSI_GREEN.escape() + '╚' + ANSI_RESET.escape());
                        break;
                    case 'F':
                        System.out.print(ANSI_GREEN.escape() + '╝' + ANSI_RESET.escape());
                        break;
                    case 'a':
                        System.out.print(ANSI_PURPLE.escape() + '╔' + ANSI_RESET.escape());
                        break;
                    case 's':
                        System.out.print(ANSI_PURPLE.escape() + '╗' + ANSI_RESET.escape());
                        break;
                    case 'd':
                        System.out.print(ANSI_PURPLE.escape() + '╚' + ANSI_RESET.escape());
                        break;
                    case 'f':
                        System.out.print(ANSI_PURPLE.escape() + '╝' + ANSI_RESET.escape());
                        break;
                    case 'R':
                        System.out.print(ANSI_RED.escape() + '═' + ANSI_RESET.escape());
                        break;
                    case 'r':
                        System.out.print(ANSI_RED.escape() + '║' + ANSI_RESET.escape());
                        break;
                    case 'B':
                        System.out.print(ANSI_BLUE.escape() + '═' + ANSI_RESET.escape());
                        break;
                    case 'b':
                        System.out.print(ANSI_BLUE.escape() + '║' + ANSI_RESET.escape());
                        break;
                    case 'Y':
                        System.out.print(ANSI_YELLOW.escape() + '═' + ANSI_RESET.escape());
                        break;
                    case 'y':
                        System.out.print(ANSI_YELLOW.escape() + '║' + ANSI_RESET.escape());
                        break;
                    case 'W':
                        System.out.print(ANSI_WHITE.escape() + '═' + ANSI_RESET.escape());
                        break;
                    case 'w':
                        System.out.print(ANSI_WHITE.escape() + '║' + ANSI_RESET.escape());
                        break;
                    case 'V':
                        System.out.print(ANSI_GREEN.escape() + '═' + ANSI_RESET.escape());
                        break;
                    case 'v':
                        System.out.print(ANSI_GREEN.escape() + '║' + ANSI_RESET.escape());
                        break;
                    case 'P':
                        System.out.print(ANSI_PURPLE.escape() + '═' + ANSI_RESET.escape());
                        break;
                    case 'p':
                        System.out.print(ANSI_PURPLE.escape() + '║' + ANSI_RESET.escape());
                        break;
                    case 'ì':
                        System.out.print(" ");
                        break;
                    case '0':
                        System.out.print(playerColorList.get(0).escape() + '0' + ANSI_RESET.escape());
                        break;
                    case '1':
                        System.out.print(playerColorList.get(1).escape() + '1' + ANSI_RESET.escape());
                        break;
                    case '2':
                        System.out.print(playerColorList.get(2).escape() + '2' + ANSI_RESET.escape());
                        break;
                    case '3':
                        System.out.print(playerColorList.get(3).escape() + '3' + ANSI_RESET.escape());
                        break;
                    case '4':
                        System.out.print(playerColorList.get(4).escape() + '4' + ANSI_RESET.escape());
                        break;
                    case 'à':
                        System.out.print(ANSI_BLUE.escape() + '°' + ANSI_RESET.escape());
                        break;
                    case 'ò':
                        System.out.print(ANSI_RED.escape() + '°' + ANSI_RESET.escape());
                        break;
                    case 'è':
                        System.out.print(ANSI_YELLOW.escape() + '°' + ANSI_RESET.escape());
                        break;
                    case '+':
                        System.out.print(ANSI_WHITE.escape() + '^' + ANSI_RESET.escape());
                        break;
                    case 'ç':
                        System.out.print(ANSI_RED.escape() + '§' + ANSI_RESET.escape());
                        break;
                    case '§':
                        System.out.print(ANSI_BLUE.escape() + '§' + ANSI_RESET.escape());
                        break;
                    case 'ù':
                        System.out.print(ANSI_YELLOW.escape() + '§' + ANSI_RESET.escape());
                        break;
                    default:
                        System.out.print(battelfield[i][j]);
                }
            }
            System.out.println();
        }
    }

    public static void loadMap(int mapType){
        try {
            populateMatrixFromFile(mapType);
        }catch (Exception e){

        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void showMessage(String msg){
        System.out.println(msg);
    }


    public static void populateWelcome() {
        try {
            // Open the file that is the first
            // command line parameter

            // gets input stream

            InputStream inputStream = FileRead.class.getResourceAsStream("/map/welcome.txt" );

            // creates a reader for the file

            BufferedReader br = new BufferedReader( new InputStreamReader(inputStream));
            String strLine;
            //Read File Line By Line
            int r = 0;
            int cl = 0;
            while ((strLine = br.readLine()) != null) {

                // Print the content on the console
                for (int i = 0; i < C_W; i++) {
                    if(strLine.charAt(i) == '\n'){
                        welcome[r][i] = '\n';
                    } else
                        welcome[r][i] = strLine.charAt(i);
                }
                r++;
            }

            //Close the input stream
            inputStream.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void showWelcome(){
        populateWelcome();
        for (int i = 0; i < R_W; i++) {
            for (int j = 0; j < C_W; j++) {
                switch (welcome[i][j]){

                    case '*':
                        System.out.print(" ");
                        break;

                    default:
                        System.out.print(ANSI_YELLOW.escape() + welcome[i][j] + ANSI_RESET.escape());
                }

                }
            System.out.println();
        }
    }

    //need ordered calls by playerid
    public static void addPlayerColor(PlayerColor color){
        //System.out.println("[DEBUG] CALLED addPlayerColor from WaitingRoom");

        switch (color){
            case BLUE:
                playerColorList.add(ANSI_BLUE);
                break;

            case GREY:
                playerColorList.add(ANSI_WHITE);
                break;

            case PURPLE:
                playerColorList.add(ANSI_PURPLE);
                break;

            case YELLOW:
                playerColorList.add(ANSI_YELLOW);
                break;

            case GREEN:
                playerColorList.add(ANSI_GREEN);
                break;
        }
    }

}