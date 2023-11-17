import static java.lang.Math.max;

public class Strategy {
    public int currentBoard; //The current board that AI will make a move on
    final int MIN_VALUE = -2147483647;
    final int MAX_VALUE = 2147483647;
    int bestHVal,bestSpot = -1;

    /**
     * Strategy initialization
     */
    public Strategy() {}


    /**
     * Determines whether a board is won in ourPlayer's favor
     * @param board the board being checked for its win state
     * @return 1 if ourPlayer won the board, -1 if the other player won the board, 0 if no one won
     */
    public int utility(int player, int[] board) {
        int winner = new Gamer("").isBoardWon(board);
        if(winner == player) {
           return 1;
        } else if (winner == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Evaluates a given board
     * @param player the player number representing the AI
     * @param board the board being evaluated
     * @return a heuristic denoting the value of the board for the given player
     */
    public int evaluate(int player, int[] board) {
        int otherPlayer = 0;
        if(player == 1) { otherPlayer = 2; }
        else { otherPlayer = 1; }

        int heuristic = 0;
        //all rows
        for(int i = 0; i <= 6; i+=3) {
            int ourp = 0, otherp = 0;
            //checks each spot in a row
            for(int j = i; j < i+3; j++) {
                if(board[j] == player) { ourp++; }
                else if(board[j] == otherPlayer) { otherp++; }
            }
            heuristic = getHeuristic(heuristic, ourp, otherp);
        }

        //all columns
        for(int i = 0; i < 3; i++) {
            int ourp = 0; int otherp = 0;
            //checks each spot in a column
            for(int j = i; j <= 8; j+=3) {
                if(board[j] == player) { ourp++; }
                else if(board[j] == otherPlayer) { otherp++; }
            }
            heuristic = getHeuristic(heuristic, ourp, otherp);
        }

        //diagonal 1
        int ourp = 0; int otherp = 0;
        for(int i = 0; i <=8; i+=4) {
            if(board[i] == player) { ourp++; }
            else if(board[i] == otherPlayer) { otherp++; }
        }
        heuristic = getHeuristic(heuristic, ourp, otherp);

        //diagonal 2
        ourp = 0; otherp = 0;
        for(int i = 2; i <= 6; i+=2) {
            if(board[i] == player) { ourp++; }
            else if(board[i] == otherPlayer) { otherp++; }
        }
        heuristic = getHeuristic(heuristic, ourp, otherp);

        return heuristic;
    }

    /**
     * Function determines the heuristic points based on the configuration on single row/column/diagonal
     * @param heuristic the heuristic value
     * @param ourp our poses, the number of spots that we made on that single configuration
     * @param otherp opponent poses, the number of spots that the opponent made on the single configuration
     * @return the heuristic value of based on the single configuration that contains our move
     */
    private int getHeuristic(int heuristic, int ourp, int otherp) {
        if(ourp + otherp == 3 && ourp > 0 && otherp > 0) {
            // if the 3 spots examined are all full, do not adjust heuristic
            return heuristic;
        } else if(ourp == 3) {
            // if our player has three in a row
            return 500;
        } else if(otherp == 3) {
            // if the other player has three in a row
            return -500;
        }

        if(ourp - otherp == 2) { heuristic += 50; }
        else if(otherp - ourp == 2) { heuristic -= 50; }
        else if(ourp - otherp == 1) { heuristic += 5; }
        else if(otherp - ourp == 1) { heuristic -= 5; }

        return heuristic;
    }


    //Rule-based strategy
    // If have a wining move -> take the move
    // Opponent has a winning move -> block move
    // A move that can create (1 or 2) possible winning moves <- do the same to block the opponent
    // Create a forking move that creates two win paths (double trap)
    // Prevent the opponent from creating a forking move
    //Heuristic board evaluation function
    //Compute the scores for each of the 8 possible winning (3 rows/col + 2 diagonal)

    // 1 in a row + empty boxes
    // 2 in a row + empty boxes
    // 3 in a row (WIN)

    /**
     * Expanding the children of the given node
     * Put all the legal moves (spots) in the list, search for all the empty cells on the board (not the big board) and put it on the list
     * @param n Given node
     * @param player if 1 then AI, if 2 then the opponent
     * @return The original node with expanded children
     */

    public Node addNewLevels(Node n, int player){
        for (int spot = 0; spot < 9; spot++){
            if(n.boardConfig[spot] == 0){
                n.addChild(n,spot,player);
            }
        }
        return n;
    }

    /**
     *Function prints out the whole board, mostly use for debugging
     * @param board input board to print out
     */
    public void printIndBoard(int[] board) {
        System.out.printf("%d %d %d\n%d %d %d\n%d %d %d\n", board[0],board[1],board[2],board[3],board[4],board[5],board[6],board[7],board[8]);
    }

    /**
     * Function employs the capabilities of evaluate() on a recursive level. We decided to implement the second minimax
     * as the first one return Node value sometimes is not what we expected. Hence, this minimax is easier to manage and debug.
     * @param n current Node in the search tree
     * @param isMax the boolean state to start evaluation in (Max or Min)
     * @param alpha the int maximum value that can be extracted from the search tree
     * @param beta the int minimum value that can be extracted from the search tree
     * @param depth  the int depth of the tree to explore (where depth is equal to number of new moves simulated on one board)
     * @return an array that contains the best score and best spot to make the move
     */
    public int[] minimax(Node n, boolean isMax, int alpha, int beta, int depth) {
        //Determine bestHValue based on the boolean isMax
         bestHVal = isMax ? MIN_VALUE : MAX_VALUE;
        //If base case then return the heuristic value
        if (depth == 0){
            bestHVal = evaluate(1, n.boardConfig);

        } else {
            //If Max
            if (isMax){
                addNewLevels(n,1); //Expand the children from the node input
                for (int i = 0; i < n.children.size(); i++){         //Traversing list to deal with all children
                    int iterativeEval = evaluate(1,n.children.get(i).boardConfig);

                    if (utility(1, n.children.get(i).boardConfig) == 1){ //If found a won move
                        bestSpot = n.children.get(i).spot;
                        bestHVal = iterativeEval + depth*100; //Iterative evaluation -> the higher level the better score
                        int[] bestMove = {bestSpot,bestHVal};
                        //End the for loop and returns the best Move immediately
                        return bestMove;
                    }
                    int[] current = minimax(n.children.get(i),false,alpha,beta,depth-1); //Putting move on potential board and recursing
                    //Pruning
                    if (current[1] > alpha){       //Comparing newly returned heuristic value to the previous heuristic value
                        bestSpot = n.children.get(i).spot;  //Setting best move to current best if the heuristic is LARGER
                        bestHVal = current[1];
                        alpha = bestHVal;
                    }
                    if (alpha >= beta) break;
                }
                //Returns in case where Alpha beta pruning is not necessary

            }else{
                //add children line (+1 depth)
                //check for vacant spots
                //put these spots on the tree
                addNewLevels(n,2); // expand the children as the view of the second player as it is their moves

                for (int i = 0; i < n.children.size(); i++){
                    int iterativeEval = evaluate(1,n.children.get(i).boardConfig);

                    if (utility(1, n.children.get(i).boardConfig) == -1){ //If the player 1 lose to this opponent move
                        bestSpot = n.children.get(i).spot;
                        bestHVal = iterativeEval - depth*100; //Iterative evaluation
                        int[] bestMove = {bestSpot,bestHVal};
                        //The AI get that opponent move and turns it to ourMove instead
                        return bestMove;
                    }
                    //Recursion
                    int[] current = minimax(n.children.get(i),true,alpha,beta,depth-1);

                    //Pruning
                    if (current[1] < beta){
                        bestSpot = n.children.get(i).spot;//Setting best move to current best
                        bestHVal = current[1];
                        beta = bestHVal;
                    }
                    if (alpha >= beta) break;
                }
            }
        }


        int[] bestMove = {bestSpot,bestHVal};
        return bestMove;
    }

}
