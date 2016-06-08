package puf.example.biggerquento;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void divisionTest() throws Exception {
        assertEquals(5, 3 * 3 / 2 + 1);
    }

    @Test
    public void StringReaderTest() throws Exception {
        assertEquals(275, (int) GameView.eval("((- 4 - 2^3 + 1) * - sqrt(3*3+4*4)) / 2 * 10 "));
    }

    @Test
    public void isNotInTest() throws Exception {
        Coordinates c1 = new Coordinates(2,1);
        Coordinates c2 = new Coordinates(1,1);
        Coordinates c3 = new Coordinates(3,1);
        Coordinates c4 = new Coordinates(2,0);
        Coordinates c5 = new Coordinates(2,2);

        ArrayList<Coordinates> c = new ArrayList<>();
        c.add(c1);
        c.add(c2);
        c.add(c3);

        assertEquals(false,c3.isNotIn(c));
        assertEquals(true,c5.isNotIn(c));
    }
}