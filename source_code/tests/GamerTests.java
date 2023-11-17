import org.junit.Assert;
import org.junit.Test;

public class GamerTests {
    @Test
    public void testTestsWorking() {
        Gamer play = new Gamer("Walrus");
        Assert.assertNotEquals(3,2);
        Assert.assertEquals(3,3);
    }
    @Test
    public void testFullBoard(){
        Gamer play = new Gamer("walrus");
        for (int i = 0; i < 9; i++){
            play.ultimateBoard[0][i] = 1;
        }
        Assert.assertEquals(0,play.countRemainingMoves(play.getUltimateBoard()[0]));

    }
}
