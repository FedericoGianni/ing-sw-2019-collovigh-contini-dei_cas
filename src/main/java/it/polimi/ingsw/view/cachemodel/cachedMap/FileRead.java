package it.polimi.ingsw.view.cachemodel.cachedMap;

import it.polimi.ingsw.customsexceptions.InvalidMapTypeException;

import java.io.*;
import java.util.Random;

import static it.polimi.ingsw.view.cachemodel.cachedMap.AsciiColor.*;

class FileRead {

    public static final int R = 19;
    public static final int C = 49;
    private static char battelfield[][] = new char[R][C];


    public static void main(String args[]) {
        try {
            populateMatrixFromFile(3);
        }catch (Exception e){
            e.getMessage();
        }

        insertPlayer(0,0,'1');
        insertPlayer(0,1,'2');
        insertPlayer(0,2,'3');
        insertPlayer(0,3,'4');
        insertPlayer(1,0,'5');
        insertPlayer(1,1,'6');
        insertPlayer(1,2,'7');
        insertPlayer(1,3,'8');
        insertPlayer(2,1,'9');
        insertPlayer(2,2,'@');
        insertPlayer(2,3, '#');
        showBattlefield(battelfield);
    }


    public static void populateMatrixFromFile(int mapType) throws InvalidMapTypeException {
        try {
            // Open the file that is the first
            // command line parameter
            String path;
            switch(mapType){

                case 1:
                    path = new File("resources/map/map1.txt").getAbsolutePath();
                    break;
                case 2:
                    path = new File("resources/map/map2.txt").getAbsolutePath();
                    break;
                case 3:
                    path = new File("resources/map/map3.txt").getAbsolutePath();
                    break;
                default:
                    throw new InvalidMapTypeException();
            }
            FileInputStream fstream = new FileInputStream(path);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
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
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static int genRandInt(int lower, int upper){
        Random rand = new Random();
        return rand.nextInt(upper-lower) + lower;
    }


    public static void insertPlayer(int x, int y, char id){

        if(x < 0 || x > 2 || y < 0 || y > 3)
           return;


        int r = 0;
        int c = 0;

        do{
            r = genRandInt(1 + 7*x, 5 + 7*x);
            c = genRandInt(1+ 12*y, 10 + 12*y);
        }while(battelfield[r][c] != ' ');

        battelfield[r][c] = id;

    }

    public static void showBattlefield(char[][] b){
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                switch(b[i][j]) {

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
                    default:
                        System.out.print(b[i][j]);
                }
            }
            System.out.println();
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void showMessage(String msg){
        System.out.println(msg);
    }
}