/**
 * Created by Chris on 10/8/15.
 *
 * GameModel with AI Player
 *
 * NOTE: AI FUNCTIONALITY DOES NOT WORK
 */

import java.util.Observable;

public class GameModel extends Observable {
    private static Board board;
    private final static int w = 7;
    private final static int h = 6;

    AI computer1 = null;
    AI computer2 = null;

    private static int currPlayer = 0;
    private static int player1 = 1;
    private static int player2 = 2;
    private static int winner = 3;
    private static boolean whoseTurn;

    private static int winningPlayer;


    /**
     * Constructor
     */
    public GameModel() {
        board = new Board(h, w);
        currPlayer = player1;
        whoseTurn = true;
    }

    /**
     * Changes who's turn it is
     */
    public void flip(){
        if (this.whoseTurn){
            this.whoseTurn = false;
            currPlayer = player2;
        } else {
            this.whoseTurn = true;
            currPlayer = player1;
        }
    }

    /**
     * @return the player who's turn it is
     */
    public int getPlayer(){
        return currPlayer;
    }

    /**
     * places a piece in the column selected
     * @param player the player selecting
     * @param col the column they select
     */
    public void selectColumn(int player, int col){
        boolean placed = false;
        boolean valid = true;

        while (valid) {
            for (int i = 0; i < h; i++) {
                if (!board.isOccupied(i, col - 1)) {
                    board.place(player, i, col - 1);
                    flip();
                    placed = true;
                    break;
                }
            }

            if (placed) {
                valid = false;
            } else {
                //System.out.println("COLUMN IS FULL. PLEASE TRY AGAIN.");
                GUI.colFull = true;
                valid = false;
            }
        }

        checkWin(player);

        setChanged();
        notifyObservers();
    }

    /**
     * Clears the board
     * Resets the game
     */
    public void clearBoard(){
        board.clear();
        currPlayer = 1;
        whoseTurn = true;
        setChanged();
        notifyObservers();
    }

    /**
     * @return the board
     */
    public Board getBoard(){
        return board;
    }

    /**
     * @return the winning player
     */
    public int getWinningPlayer(){ return winningPlayer; }

    /**
     * Creates an AI player
     * @param player player Id
     */
    public void initializeAI(int player){
        if (player == 1) {
            computer1 = new AI(this, board, player);
        } else {
            computer2 = new AI(this, board, player);
        }
    }

    public void checkWin(int player){
        if (board.gameOver()) {
            winningPlayer = player;
            for (int i = 0; i < 4; i++) {
                board.place(winner, board.winners.get(i)[0], board.winners.get(i)[1]);
            }
        } else if (board.fullCheck()) {
            GUI.full = true;
        }
    }

    public boolean computerMove(int player){
        if (player == 1 && computer1 != null) {
            return true;
        } else if (player == 2 && computer2 != null){
            return true;
        } else {
            return false;
        }
    }

}
