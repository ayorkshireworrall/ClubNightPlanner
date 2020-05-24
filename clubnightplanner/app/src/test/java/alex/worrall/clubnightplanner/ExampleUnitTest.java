package alex.worrall.clubnightplanner;

import org.junit.Test;

import alex.worrall.clubnightplanner.utils.Status;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void enumstuff() {
        Status status = Status.COMPLETED;
        Status[] values = status.values();
        for (Status value: values) {
            System.out.println(value);
        }
        Status newStatus = Status.valueOf(status.toString());
        System.out.println(newStatus.toString());
    }
}