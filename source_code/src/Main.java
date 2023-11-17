import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //Setting up our player
        Gamer play = new Gamer("Walrus");
        //Add the moves from first_four_moves
        play.addFirstMoves();

        //run until the game ends
        while (true) {
            //Check if game is over
            if (play.isGameWon() > 0 || play.bigBoardIsFull()){
                break;
            }

            //Taking the opponent move
            int[] move = play.updateBoard();

            if(play.legalMove(move[0],move[1])){ //check if the move is legal
                //If the opponent move is not legal then -> we won the game
                break;
            }

            int bestSpot = -1;
            int depth = 1;
            //Max depth is the numbers of possible moves on the board that we're going to move on
            int maxDepth = play.countRemainingMoves(play.getUltimateBoard()[move[1]]);
            Strategy strategy = new Strategy();

            System.out.println("win state " + play.isBoardWon(play.getUltimateBoard()[move[1]]));
            //If the board at the opponent's spot is won
            if (play.isBoardWon(play.getUltimateBoard()[move[1]]) > 0 ||
                    play.countRemainingMoves(play.getUltimateBoard()[move[1]]) == 0) { // Board is won or full
                // Pick another board
                play.updateBoardWinState();

                        //Run the for loop to do determine which board currently has the best points for us
                        //Then choose the best board
                        int[] bestBoard =  {-1,-1}; //hValue - Spot
                        for (int i = 0; i < 9; i ++){
                            System.out.println("Board won state at" + i + "is" + play.isBoardWon(play.getUltimateBoard()[i]));
                            System.out.println("Board remaining moves at" + i + "is" + play.countRemainingMoves(play.getUltimateBoard()[i]));

                            if (play.isBoardWon(play.getUltimateBoard()[i]) == 0
                                    && play.countRemainingMoves(play.getUltimateBoard()[i]) > 0) {
                                    int evalBoard = strategy.evaluate(1, play.getUltimateBoard()[i]);
                                    if (evalBoard < 0) {evalBoard = 0;}
                                    if (evalBoard > bestBoard[0]) {
                                        bestBoard[0] = evalBoard;
                                        bestBoard[1] = i;
                                    }
                                }
                        }

                        if(bestBoard[1] < 0){
                            bestBoard[1] = play.returnAPossibleBoard();
                        }
                        strategy.currentBoard = bestBoard[1];
                        System.out.println("Time to choose another board, at " + strategy.currentBoard);

                        //After choosing the best board, the AI use minimax to determine the best spot on the board
                        //Then make a move
                        while (play.recMove.elapsedTime() < 7 && depth != maxDepth) { // 7 is tentative
                            Node rootNode = new Node(2, strategy.currentBoard, 0, play.getUltimateBoard()[strategy.currentBoard]);
                            bestSpot = strategy.minimax(rootNode, false, -1000000, 1000000, depth)[0];
                            depth++;
                        }

                    System.out.println("Board choose is at " + strategy.currentBoard);
                System.out.println("---------------Our board to move in: " + strategy.currentBoard);

                //Make the move
                play.ourMove(strategy.currentBoard, bestSpot);

            } else {

                //Run the for loop to do iterative minimax alpha-beta pruning
                strategy.currentBoard = move[1];
                //While the stopWatch is less than 7 seconds or depth have not reached maxDepth yet
                while (play.recMove.elapsedTime() < 7 && depth != maxDepth) { // 7 is tentative
                    Node rootNode = new Node(2, move[1], 0, play.getUltimateBoard()[move[1]]);
                    //Using minimax to determine the best spot on the board
                    bestSpot = strategy.minimax(rootNode, false, -1000000, 1000000, depth)[0];
                    depth++;
                }
                System.out.println("---------------Our board to move in: " + move[1]);
                //Make the move on the board that is the spot of the previous player + best move the AI just generated
                if(bestSpot < 0){
                    bestSpot = play.returnAPossibleMove(move[1]);
                }
                play.ourMove(move[1], bestSpot);
            }
            while(play.playerfile.exists()){}
        }
    }
}
