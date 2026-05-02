import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EuclideanColorDistanceTest {

    @Test
    public void testSameColorDistanceIsZero() {
        EuclideanColorDistance finder = new EuclideanColorDistance();

        double actual = finder.distance(0xFF0000, 0xFF0000);
        System.out.println("actual = " + actual);

        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void testBlackToWhiteDistance() {
        ColorDistanceFinder finder = new EuclideanColorDistance();

        double actual = finder.distance(0x000000, 0xFFFFFF);
        double expected = Math.sqrt((255 * 255) + (255 * 255) + (255 * 255));

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testRedToGreen() {
        EuclideanColorDistance finder = new EuclideanColorDistance();

        double actual = finder.distance(0xFF0000, 0x00FF00);

        assertEquals(360.624, actual, 0.001);
    }

    @Test
    public void testRedToBlueDistance() {
        ColorDistanceFinder finder = new EuclideanColorDistance();

        double actual = finder.distance(0xFF0000, 0x0000FF);
        double expected = Math.sqrt((255 * 255) + (0 * 0) + (255 * 255));

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testGreenToBlue() {
        EuclideanColorDistance finder = new EuclideanColorDistance();

        double actual = finder.distance(0x00FF00, 0x0000FF);

        assertEquals(360.624, actual, 0.001);
    }

    @Test
    public void testSmallDifference() {
        ColorDistanceFinder finder = new EuclideanColorDistance();

        double actual = finder.distance(0x010203, 0x040607);

        double expected = Math.sqrt(
                (1 - 4) * (1 - 4)
                        + (2 - 6) * (2 - 6)
                        + (3 - 7) * (3 - 7));

        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testCustomColors() {
        EuclideanColorDistance finder = new EuclideanColorDistance();

        double actual = finder.distance(0x123456, 0x654321);

        assertEquals(99.614, actual, 0.001);
    }

    @Test
    public void testDistanceWorksBothDirections() {
        EuclideanColorDistance finder = new EuclideanColorDistance();

        double first = finder.distance(0x123456, 0x654321);
        double second = finder.distance(0x654321, 0x123456);

        assertEquals(first, second, 0.001);
    }

}
