import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Chris on 5/7/2015.
 *
 * The Board used in either the Terminal Model(Game) or GameModel
 *
 * NOTE: AI FUNCTIONALITY DOES NOT WORK
 */
public class Board {
    private int h;
    private int w;
    int[][] board;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    int[] values = new int[42];

    ArrayList<Integer[]> winners = new ArrayList<Integer[]>();

    /**
     * Creates a board
     * @param h height
     * @param w width
     */
    public Board(int h, int w){
        int count = 0;
        this.h = h;
        this.w = w;
        this.board = new int[h][w];

        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                board[i][j] = 0;
                values[count] = board[i][j];
                count += 1;
            }
        }
    }

    /**
     * @return the board
     */
    public int[][] getBoard(){
        return board;
    }

    /**
     * Creates a string representation of the board
     * @return string representing the board
     */
    public String toString(){
        String board = "";

        for (int c = 1; c <= w; c++){
            board += " " + c + "  ";
        }

        board += "\n";

        for (int i = h - 1; i >= 0; i--){
            for (int j = 0; j < w; j++){
                if (this.board[i][j] == 0){
                    board += "[ ] ";
                } else {
                    board += "[" + this.board[i][j] + "] ";
                }
            }
            board += "\n";
        }
        return board;
    }

    /**
     * places a piece
     * @param player the player placing the piece
     * @param r row
     * @param c column
     */
    public void place(int player, int r, int c){
        board[r][c] = player;

        int idx = (7 * r) + c;
        values[idx] = player;
    }

    /**
     * checks if a location is occupied
     * @param r row
     * @param c column
     * @return true if occupied
     */
    public boolean isOccupied(int r, int c){
        if (board[r][c] != 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears the board
     * All values = 0
     */
    public void clear(){
        int count = 0;

        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                board[i][j] = 0;
                values[count] = 0;
                count +=1;
            }
        }
    }

    /**
     * Checks to see if every spot on the board is occupied
     * @return true if full
     */
    public boolean fullCheck(){
        for (int i: values){
            if (i == 0){
                return false;
            }
        }

        return true;
    }

    /**
     * generates the winning game state as a string
     * @return a string representation of the winning game state
     */
    public String winningString(){
        String board = "";

        for (int c = 1; c <= w; c++){
            board += " " + c + "  ";
        }

        board += "\n";

        int r1 = winners.get(0)[0];
        int c1 = winners.get(0)[1];
        int r2 = winners.get(1)[0];
        int c2 = winners.get(1)[1];
        int r3 = winners.get(2)[0];
        int c3 = winners.get(2)[1];
        int r4 = winners.get(3)[0];
        int c4 = winners.get(3)[1];

//        System.out.println(r1 + "," + c1);
//        System.out.println(r2 + "," + c2);
//        System.out.println(r3 + "," + c3);
//        System.out.println(r4 + "," + c4);

        for (int i = h - 1; i >= 0; i--){
            for (int j = 0; j < w; j++){
                if (i == r1 && j == c1) {
                    board += ANSI_PURPLE + "[" + this.board[i][j] + "] " + ANSI_RESET;
                } else if (i == r2 && j == c2) {
                    board += ANSI_PURPLE + "[" + this.board[i][j] + "] " + ANSI_RESET;
                } else if (i == r3 && j == c3) {
                    board += ANSI_PURPLE + "[" + this.board[i][j] + "] " + ANSI_RESET;
                } else if (i == r4 && j == c4){
                    board += ANSI_PURPLE + "[" + this.board[i][j] + "] " + ANSI_RESET;
                } else if (this.board[i][j] == 0){
                    board += "[ ] ";
                } else {
                    board += "[" + this.board[i][j] + "] ";
                }
            }
            board += "\n";
        }
        return board;
    }

    /**
     * checks if there are four of the same pieces in a row
     * @return true if someone has won
     */
    public boolean gameOver(){
        LinkedList<Integer> check = new LinkedList<Integer>();
        Integer[] loc1 = new Integer[2];
        Integer[] loc2 = new Integer[2];
        Integer[] loc3 = new Integer[2];
        Integer[] loc4 = new Integer[2];


        //check row by row
        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                if (board[i][j] == 0){
                    //System.out.println("HIT ZERO");
                    check.clear();
                } else if (board[i][j] != 0){
                    if (check.isEmpty()){
                        check.add(board[i][j]);
                        //System.out.println("IN CHECK: " + board[i][j]);
                    } else if (board[i][j] == check.peekFirst()){
                        check.add(board[i][j]);
                        //System.out.println("ADDING ANOTHER " + board[i][j]);
                    } else if (board[i][j] != check.peekFirst()){
                        //System.out.println("NOT EQUAL");
                        //System.out.println("CLEARING");
                        check.clear();
                        //System.out.println("IN CHECK: " + board[i][j]);
                        check.add(board[i][j]);
                    }

                    if (check.size() == 4){
                        //System.out.println("4 IN A ROW");
                        loc1[0] = i;
                        loc1[1] = j;
                        winners.add(0,loc1);
                        loc2[0] = i;
                        loc2[1] = j-1;
                        winners.add(1,loc2);
                        loc3[0] = i;
                        loc3[1] = j-2;
                        winners.add(2,loc3);
                        loc4[0] = i;
                        loc4[1] = j-3;
                        winners.add(3,loc4);

                        return true;
                    }
                }
                //System.out.println("NEXT COL: " + (j + 1));
            }
            //System.out.println("NEXT ROW, CLEARING");
            check.clear();
        }

        check.clear();

        //check column by column
        for (int j = 0; j < w; j++){
            for (int i = 0; i < h; i++){
                if (board[i][j] == 0){
                    //System.out.println("HIT ZERO");
                    check.clear();
                    break;
                } else if (board[i][j] != 0){
                    if (check.isEmpty()){
                        check.add(board[i][j]);
                        //System.out.println("IN CHECK: " + board[i][j]);
                    } else if (board[i][j] == check.peekFirst()){
                        check.add(board[i][j]);
                        //System.out.println("ADDING ANOTHER " + board[i][j]);
                    } else if (board[i][j] != check.peekFirst()){
                        //System.out.println("NOT EQUAL");
                        //System.out.println("CLEARING");
                        check.clear();
                        //System.out.println("IN CHECK: " + board[i][j]);
                        check.add(board[i][j]);
                    }

                    if (check.size() == 4){
                        //System.out.println("4 IN A ROW");
                        loc1[0] = i;
                        loc1[1] = j;
                        winners.add(0,loc1);
                        loc2[0] = i-1;
                        loc2[1] = j;
                        winners.add(1,loc2);
                        loc3[0] = i-2;
                        loc3[1] = j;
                        winners.add(2,loc3);
                        loc4[0] = i-3;
                        loc4[1] = j;
                        winners.add(3,loc4);

//                        for(int c = 0; c < 4; c++){
//                            System.out.println(winners.get(c)[0] + "," + winners.get(c)[1]);
//                        }

                        return true;
                    }
                }
                //System.out.println("NEXT COL: " + (j + 1));
            }
            //System.out.println("NEXT ROW, CLEARING");
            check.clear();
        }

        check.clear();

        //diagonal check left-right
        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                if (board[i][j] == 0) {
                    //DO NOTHING
                } else {
                    check.add(board[i][j]);
                    if ( ((j+1) < w) && ((i+1) < h) && board[i+1][j+1] == check.peekFirst()){
                        if ( ((j+2) < w) && ((i+2) < h) && board[i+2][j+2] == check.peekFirst()){
                            if ( ((j+3) < w) && ((i+3) < h) && board[i+3][j+3] == check.peekFirst()){
                                loc1[0] = i;
                                loc1[1] = j;
                                winners.add(0,loc1);
                                loc2[0] = i+1;
                                loc2[1] = j+1;
                                winners.add(1,loc2);
                                loc3[0] = i+2;
                                loc3[1] = j+2;
                                winners.add(2,loc3);
                                loc4[0] = i+3;
                                loc4[1] = j+3;
                                winners.add(3,loc4);

//                                for(int c = 0; c < 4; c++){
//                                    System.out.println(winners.get(c)[0] + "," + winners.get(c)[1]);
//                                }

                                return true;
                            } else {
                                check.clear();
                            }
                        } else {
                            check.clear();
                        }
                    } else {
                        check.clear();
                    }
                }
            }

            //diagonal check right-left
            for (int j = 6; j >= 0; j--){
                if (board[i][j] == 0) {
                    //DO NOTHING
                } else {
                    check.add(board[i][j]);
                    if ( ((j-1) > 0) && ((i+1) < h) && board[i+1][j-1] == check.peekFirst()){
                        if ( ((j-2) > 0) && ((i+2) < h) && board[i+2][j-2] == check.peekFirst()){
                            if ( ((j-3) > 0) && ((i+3) < h) && board[i+3][j-3] == check.peekFirst()){
                                loc1[0] = i;
                                loc1[1] = j;
                                winners.add(0,loc1);
                                loc2[0] = i+1;
                                loc2[1] = j-1;
                                winners.add(1,loc2);
                                loc3[0] = i+2;
                                loc3[1] = j-2;
                                winners.add(2,loc3);
                                loc4[0] = i+3;
                                loc4[1] = j-3;
                                winners.add(3,loc4);

//                                for(int c = 0; c < 4; c++){
//                                    System.out.println(winners.get(c)[0] + "," + winners.get(c)[1]);
//                                }

                                return true;
                            } else {
                                check.clear();
                            }
                        } else {
                            check.clear();
                        }
                    } else {
                        check.clear();
                    }
                }
            }
        }

        return false;
    }
}
