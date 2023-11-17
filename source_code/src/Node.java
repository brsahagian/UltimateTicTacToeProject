import java.util.ArrayList;
import java.util.List;

    public class Node {
        public int player;
        public int spot;
        public int cBoard;
        public int[] boardConfig;
        public List<Node> children;
        public Node(int player, int board, int spot, int[] boardConfig) {
            this.player = player;
            this.cBoard = board;
            this.spot = spot;
            children = new ArrayList<Node>();
            this.boardConfig = boardConfig;

        }

        /**
         * Adds a child to children with a new boardConfig where the given player occupies the given spot
         * @param n the node to add the child to
         * @param spot the spot to replace on the board
         * @param player the player occupying that spot on the board
         */
        public void addChild(Node n, int spot, int player){
            int[] newConfig = new int[9];
            for (int i = 0; i < n.boardConfig.length; i++){
                newConfig[i] = n.boardConfig[i];
            }
            newConfig[spot] = player;
            Node c1 = new Node(player, n.cBoard, spot, newConfig);
            n.children.add(c1);
            if (n.player == 1 || n.player == 2){
            }else{
            }
        }

    }
