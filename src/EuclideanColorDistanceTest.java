import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EuclideanColorDistanceTest {
    
    @Test
    public void testSameColorDistanceIsZero() {
        EuclideanColorDistance dist = new EuclideanColorDistance();
        assertEquals(0.0, dist.distance(0x112233, 0x112233), 0.0001);
    }

    @Test
    public void testBlackToWhite() {
        EuclideanColorDistance dist = new EuclideanColorDistance();
        double result = dist.distance(0x000000, 0xFFFFFF);
        assertEquals(441.67, result, 0.01);
    }

    @Test
    public void testRedChannelDifference() {
        EuclideanColorDistance dist = new EuclideanColorDistance();
        double result = dist.distance(0x000000, 0xFF0000);
        assertEquals(255.0, result, 0.0001);
    }

    @Test
    public void testSymmetry() {
        EuclideanColorDistance dist = new EuclideanColorDistance();
        double d1 = dist.distance(0x123456, 0xABCDEF);
        double d2 = dist.distance(0xABCDEF, 0x123456);
        assertEquals(d1, d2, 0.0001);
    }

    @Test
    public void testMixedChannelDifference() {
        EuclideanColorDistance dist = new EuclideanColorDistance();

        // (10, 20, 30) vs (13, 24, 37)
        // differences: (3, 4, 7)
        // distance = sqrt(9 + 16 + 49) = sqrt(74)
        double expected = Math.sqrt(74);

        int colorA = 0x0A141E;
        int colorB = 0x0D1825;

        assertEquals(expected, dist.distance(colorA, colorB), 0.0001);
    }

    @Test
    public void testMaxChannelDifference() {
        EuclideanColorDistance dist = new EuclideanColorDistance();

        double result = dist.distance(0x00FF00, 0x000000); // green vs black
        assertEquals(255.0, result, 0.0001);
    }

    @Test
    public void testColorExtractionAccuracy() {
        EuclideanColorDistance dist = new EuclideanColorDistance();

        // (255, 0, 0) vs (0, 255, 0)
        // distance = sqrt(255^2 + 255^2) = sqrt(130050)
        double expected = Math.sqrt(255 * 255 + 255 * 255);

        assertEquals(expected, dist.distance(0xFF0000, 0x00FF00), 0.0001);
    }

    @Test
    public void testDistanceNonNegative() {
        EuclideanColorDistance dist = new EuclideanColorDistance();
        double result = dist.distance(0x123456, 0x654321);
        assertTrue(result >= 0);
    }
}
