import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Created by Chris on 5/7/2015.
 *
 * Terminal Model of the Connect 4
 */
public class Game {
    static Scanner scan = new Scanner(System.in);
    private static Board board;
    private final static int w = 7;
    private final static int h = 6;
    private static int player1 = 1;
    private static int player2 = 2;
    private static boolean answer = true;
    private static boolean whoseTurn = true;

    /**
     * Creates a new instance of the game
     * Creates a new board to play on
     */
    public Game() {
        board = new Board(h, w);
    }

    /**
     * A player's turn
     * @param player the player whose turn it is
     */
    public void turn(int player){
        int c = 0;
        boolean valid1 = true;
        boolean valid2 = true;
        boolean placed = false;

        while (valid1) {
            while (valid2) {
                System.out.println("\nPLAYER " + player + ", WHAT COLUMN WOULD YOU LIKE TO SELECT?");
                c = scan.nextInt();
                if (c <= 0 || c > 7) {
                    System.out.println("INVALID COLUMN. PLEASE TRY AGAIN.");
                } else {
                    valid2 = false;
                }
            }

            for (int i = 0; i < h; i++) {
                if (!board.isOccupied(i, c - 1)) {
                    board.place(player, i, c - 1);
                    placed = true;
                    break;
                }
            }

            if (placed) {
                valid1 = false;
            } else {
                System.out.println("COLUMN IS FULL. PLEASE TRY AGAIN.");
                valid2 = true;
            }
        }

        System.out.println("\n" + board.toString());
    }

    /**
     * flips whose turn it is
     */
    public void flip(){
        if (this.whoseTurn){
            this.whoseTurn = false;
        } else {
            this.whoseTurn = true;
        }
    }

    /**
     * Creates a new game and runs it
     * @param args
     */
    public static void main(String[] args){

        while (answer == true) {
            System.out.println("TIME TO PLAY CONNECT FOUR!\n");
            Game game = new Game();
            boolean cont = true;
            int winner = 0;
            System.out.println("THIS IS THE BOARD:");
            System.out.println(board.toString());

            while (cont) {
                if (game.whoseTurn) {
                    game.turn(player1);
                    game.flip();
                    if (board.gameOver()){
                        winner = player1;
                        cont = false;
                        break;
                    }
                }
                if (!game.whoseTurn) {
                    game.turn(player2);
                    game.flip();
                    if (board.gameOver()){
                        winner = player2;
                        cont = false;
                        break;
                    }
                }
            }

            System.out.println("THE WINNER IS PLAYER " + winner + "!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(board.winningString() + "\n");
            System.out.println("WOULD YOU LIKE TO PLAY ANOTHER GAME (Y/N)? ");
            String newGame = scan.next().toUpperCase();
            if (newGame.equals("N")) {
                answer = false;
            } else {
                System.out.println("WOULD YOU LIKE TO ALTERNATE THE PLAYER WHO STARTS (Y/N)?");
                String alt = scan.next().toUpperCase();
                if (alt.equals("N")){
                    game.flip();
                }
            }
        }
    }
}
