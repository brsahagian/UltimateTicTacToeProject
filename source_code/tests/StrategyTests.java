import org.junit.Assert;
import org.junit.Test;

import java.lang.*;

public class StrategyTests {

    @Test
    public void testEvaluate() {
        Strategy strat = new Strategy();
        // 0 1 2
        // 1 1 2
        // 1 0 0
        int[] board = {0,1,2,1,1,2,1,0,0};
        System.out.println(strat.evaluate(1, board));
        System.out.println(strat.evaluate(2, board));

        // 0 0 2
        // 1 1 2
        // 1 0 0
        int[] board2 = {0,0,2,1,1,2,1,0,0};
        System.out.println(strat.evaluate(1, board2));
        System.out.println(strat.evaluate(2, board2));

        // 0 0 0
        // 0 1 0
        // 0 0 0
        int[] board3 = {0,0,0,0,1,0,0,0,0};
        System.out.println("3 p1 " + strat.evaluate(1, board3));
        System.out.println("3 p2 " + strat.evaluate(2, board3));

        // 0 0 0
        // 0 2 0
        // 0 0 0
        int[] board3_1 = {0,0,0,0,2,0,0,0,0};
        System.out.println("3_1 p1 " + strat.evaluate(1, board3_1));
        System.out.println("3_1 p2 " + strat.evaluate(2, board3_1));

        // 0 0 0
        // 1 0 0
        // 0 0 0
        int[] board4 = {0,0,0,1,0,0,0,0,0};
        System.out.println(strat.evaluate(1, board4));
        System.out.println(strat.evaluate(2, board4));

        // 0 0 0
        // 0 0 0
        // 0 0 0
        int[] board5 = {0,0,0,0,0,0,0,0,0};
        Assert.assertEquals(0, strat.evaluate(1, board5));
        Assert.assertEquals(0, strat.evaluate(2, board5));

        // 1 1 0
        // 1 0 2
        // 0 2 2
        int[] board6 = {1,1,0,1,0,2,0,2,2};
        Assert.assertEquals(strat.evaluate(1, board6), -strat.evaluate(2, board6));

        // 1 2 0
        // 0 0 0
        // 2 1 0
        int[] board7 = {1,2,0,0,0,0,2,1,0};
        Assert.assertEquals(0, strat.evaluate(1, board7));
        Assert.assertEquals(0, strat.evaluate(2, board7));

        // 1 2 0
        // 0 0 0
        // 0 2 0
        int[] board8 = {1,2,0,0,0,0,0,2,0};
        System.out.println("8 p1" + strat.evaluate(1, board8));
        System.out.println("8 p2" + strat.evaluate(2, board8));
    }
}
