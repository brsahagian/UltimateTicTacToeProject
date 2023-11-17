import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Gamer {
    private File movefile = new File("move_file");
    public File playerfile = new File("Walrus.go");
    private File first_four_moves = new File("first_four_moves");

    private String ourName;
    public int[][] ultimateBoard;
    private int[] boardWinState; //1x9 matrix that indicates the win state of the big board
    public Stopwatch recMove;
    private int[] finalFirstMove;

    /**
     * Gamer initialization function
     * @param ourName the name that will be used and stored on the move file
     */
    public Gamer(String ourName) {
        this.ourName = ourName;
        this.ultimateBoard = new int[9][9];
        this.boardWinState = new int[9];
        this.finalFirstMove = new int[2];
    }

    /**
     * Function used to get the game board of current game
     * @return the Game Board
     */
    public int[][] getUltimateBoard(){
        return  ultimateBoard;
    }

    /**
     * Function return the board win states of the small boards on the big ultimate board
     * @return a 1x9 array that each element represent the win state of the board with that index on the ultimate board
     */
    public int[] getBoardWinState(){
        return boardWinState;
    }

    /**
     * Updates the board based on the most recent move received
     * @return an array containing the received move in the form of {player, moveBoard, moveSpot}
     */
    public int[] updateBoard() {
        //Retrieve the opponent move from move_file
        int[] move = getMove();
        if(move == null) { return finalFirstMove; }
        int player = move[0];
        int board = move[1];
        int spot = move[2];

        //Update the move on our board
        ultimateBoard[board][spot] = player;
        displayBoard();
        return new int[]{move[1], move[2]};
    }

    /**
     * Determines the legality of a given move based on the opponent's move
     * @param board the board our move will take place on
     * @param spot the exact spot on that board
     * @return true if the move can be made on the game board
     */
    public boolean legalMove(int board, int spot) {
        //First check if the board is not won already
        if (isBoardWon(ultimateBoard[board]) == 0) {
            // True if the AI goes first and a legal board is chosen. Else true if the board spot is not occupied by a player.
            if (getMove() == null && board == finalFirstMove[1]) {
                return true;
            } else if(ultimateBoard[board][spot] == 0 ){ //&& board == getMove()[2]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a given board has been won
     * @param board a 1x9 array representing a board
     * @return an int representing the player that won this board, or 0 if the board is not won
     */
    public int isBoardWon(int[] board) {
        //Check all row won
        // {0,1,2}, {3,4,5}, {6,7,8}
        for(int i = 0; i < 8; i+=3) {
            if(board[i] == board[i+1] && board[i] == board[i+2] && board[i] != 0){
                return board[i];
            }
        }

        //Check all column won
        // {0,3,6}, {1,4,7}, {3,5,8}
        for(int i = 0; i < 3; i++) {
            if(board[i] == board[i+3] && board[i+3] == board[i+6] && board[i] != 0){
                return board[i];
            }
        }

        //Check diagonal won
        // {0,4,8}, {2,4,6}
        if(board[4] == board[0] && board[4] == board[8] && board[4] != 0){
            return board[4];
        } else if (board[4] == board[2] && board[4] == board[6] && board[4] != 0){
            return board[4];
        }
        return 0;
    }

    /**
     * Determines the win state of each board, then determines whether a player has won the entire game
     * @return the player number that won the game, or 0 if no player has won
     */
    public int isGameWon(){
        int[] boardState = new int[9];
        for(int i = 0; i < 9; i++){
            int boardWonState = isBoardWon(getUltimateBoard()[i]);
            if( boardWonState> 0){
                boardState[i] = boardWonState;
            }
        }
        return isBoardWon(boardState);
    }

    /**
     * Function loop through all the boards on the Ultimate board and store the win state value for that board according to its index on the big board
     */
    public void updateBoardWinState(){
        for(int i = 0; i < 9; i++){
            if (isBoardWon(ultimateBoard[i]) > 0){
                boardWinState[i] = isBoardWon(ultimateBoard[i]);
            }
        }
    }

    /**
     *  Reads the first_four_moves file and places those moves on the gameBoard
     */
    public void addFirstMoves(){
        try {
            while (!first_four_moves.exists()){}
            Scanner move = new Scanner(first_four_moves);
            String[] moves = new String[4];
            for (int i = 0; i < moves.length; i++){
                String[] arr = move.nextLine().split(" ");
                ultimateBoard[Integer.parseInt(arr[1])][Integer.parseInt(arr[2])] = arr[0].equals(ourName) ? 1 : 2;
                if (i == 3){
                    finalFirstMove[0] = Integer.parseInt(arr[1]);
                    finalFirstMove[1] = Integer.parseInt(arr[2]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + first_four_moves + "not found");
        }
    }

    /**
     * Retrieves the opponent's move from movefile
     * @return an array of format {player_number, board, spot}
     */
    public int[] getMove() {
        try {
            // wait for the opponent's move and the AI's go file to arrive.
            while (!movefile.exists() || !playerfile.exists()) {
            }
            recMove = new Stopwatch(); // 10 sec timer starts

            Scanner move = new Scanner(movefile);
            if (move.hasNextLine()) {
                String[] arr = move.nextLine().split(" ");
                if (arr[0].equals(ourName)) {
                    return new int[]{1, Integer.parseInt(arr[1]), Integer.parseInt(arr[2])};
                } else {
                    return new int[]{2, Integer.parseInt(arr[1]), Integer.parseInt(arr[2])};
                }
            }
            return null;
        } catch(FileNotFoundException e) {
            System.out.println("File " + movefile + "not found");
            return null;
        }
    }

    /**
     * Writes a given move to movefile
     * @param playerName the name of the player that made the given move
     * @param board the board our move will take place on
     * @param spot the exact spot on that board
     * @return a string representing the move, or a message detailing any errors
     */
    public String sendMove(String playerName, int board, int spot) {
        String aMove = String.format("%s %s %s", playerName, board, spot);
        if (legalMove(board, spot)) {
            aMove = String.format("%s %s %s", playerName, board, spot);
            try {
                PrintWriter writeMove = new PrintWriter(movefile);
                writeMove.println(aMove);
                writeMove.close();

            } catch (FileNotFoundException e) {
                return "Move not sent";
            }
            if (playerName.equals(ourName)) {
                ultimateBoard[board][spot] = 1;
            } else {
                ultimateBoard[board][spot] = 2;
            }
        } else {
            System.out.println("No possible move");
        }
        return aMove;
    }

    /**
     * Prints the current state of gameBoard
     */
    public void displayBoard() {
        //Printing the original 9x9 board with annotation 1 as our move and 2 as our player's move
        String border = "---------------------\n";
        String row0 = String.format("%s %s %s | %s %s %s | %s %s %s\n",ultimateBoard[0][0],ultimateBoard[0][1],ultimateBoard[0][2],ultimateBoard[1][0],ultimateBoard[1][1],ultimateBoard[1][2],ultimateBoard[2][0],ultimateBoard[2][1],ultimateBoard[2][2]);
        String row1 = String.format("%s %s %s | %s %s %s | %s %s %s\n",ultimateBoard[0][3],ultimateBoard[0][4],ultimateBoard[0][5],ultimateBoard[1][3],ultimateBoard[1][4],ultimateBoard[1][5],ultimateBoard[2][3],ultimateBoard[2][4],ultimateBoard[2][5]);
        String row2 = String.format("%s %s %s | %s %s %s | %s %s %s\n",ultimateBoard[0][6],ultimateBoard[0][7],ultimateBoard[0][8],ultimateBoard[1][6],ultimateBoard[1][7],ultimateBoard[1][8],ultimateBoard[2][6],ultimateBoard[2][7],ultimateBoard[2][8]);
        String row3 = String.format("%s %s %s | %s %s %s | %s %s %s\n",ultimateBoard[3][0],ultimateBoard[3][1],ultimateBoard[3][2],ultimateBoard[4][0],ultimateBoard[4][1],ultimateBoard[4][2],ultimateBoard[5][0],ultimateBoard[5][1],ultimateBoard[5][2]);
        String row4 = String.format("%s %s %s | %s %s %s | %s %s %s\n",ultimateBoard[3][3],ultimateBoard[3][4],ultimateBoard[3][5],ultimateBoard[4][3],ultimateBoard[4][4],ultimateBoard[4][5],ultimateBoard[5][3],ultimateBoard[5][4],ultimateBoard[5][5]);
        String row5 = String.format("%s %s %s | %s %s %s | %s %s %s\n",ultimateBoard[3][6],ultimateBoard[3][7],ultimateBoard[3][8],ultimateBoard[4][6],ultimateBoard[4][7],ultimateBoard[4][8],ultimateBoard[5][6],ultimateBoard[5][7],ultimateBoard[5][8]);
        String row6 = String.format("%s %s %s | %s %s %s | %s %s %s\n",ultimateBoard[6][0],ultimateBoard[6][1],ultimateBoard[6][2],ultimateBoard[7][0],ultimateBoard[7][1],ultimateBoard[7][2],ultimateBoard[8][0],ultimateBoard[8][1],ultimateBoard[8][2]);
        String row7 = String.format("%s %s %s | %s %s %s | %s %s %s\n",ultimateBoard[6][3],ultimateBoard[6][4],ultimateBoard[6][5],ultimateBoard[7][3],ultimateBoard[7][4],ultimateBoard[7][5],ultimateBoard[8][3],ultimateBoard[8][4],ultimateBoard[8][5]);
        String row8 = String.format("%s %s %s | %s %s %s | %s %s %s\n",ultimateBoard[6][6],ultimateBoard[6][7],ultimateBoard[6][8],ultimateBoard[7][6],ultimateBoard[7][7],ultimateBoard[7][8],ultimateBoard[8][6],ultimateBoard[8][7], ultimateBoard[8][8]);

        System.out.println(row0 + row1 + row2 + border +
                row3 + row4 + row5 + border +
                row6 + row7 + row8);
    }

    /**
     * Completes the program's move
     * @param board the board the move takes place on
     * @param spot the exact spot on that board
     */
    public void ourMove(int board, int spot) {
        if (isGameWon() == 0){
            sendMove(ourName, board, spot);
            System.out.println(recMove.elapsedTime());
        }
    }

    /**
     * Function count and return the remaining moves by counting the empty spots on the board
     * @param board the board input
     * @return numbers of empty spots on the board that player can move on
     */
    public int countRemainingMoves(int[] board) {
        int count = 0;
        for(int i = 0; i < 9; i++) {
            if(board[i] == 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Loops through the boardWinStates and determines if the overall board is full or not
     * @return true if all spots on the board are fulled
     */
    public boolean bigBoardIsFull(){
        updateBoardWinState();
        if (countRemainingMoves(boardWinState) == 0){
            return true;
        }
        return false;
    }

    public int returnAPossibleMove( int board){
        int possibleMove = 0;
        for (int i = 0; i <9; i++){
            if(ultimateBoard[board][i] == 0){
                possibleMove = i;
                break;
            }
        }
        return possibleMove;
    }

    public int returnAPossibleBoard(){
        int possibleBoard = 0;
        for (int i = 0; i <9; i++){
            if(isBoardWon(ultimateBoard[i]) > 0){
                possibleBoard = i;
                break;
            }
        }
        return possibleBoard;
    }
}
