import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EuclideanColorDistanceTest {

    @Test
    public void testSameColorDistanceIsZero() {
        EuclideanColorDistance finder = new EuclideanColorDistance();

        double actual = finder.distance(0xFF0000, 0xFF0000);

        assertEquals(0.0, actual, 0.001);
    }
}
