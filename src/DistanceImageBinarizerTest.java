import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceImageBinarizerTest {

    private static class SpyColorDistanceFinder implements ColorDistanceFinder {
        private final List<int[]> calls = new ArrayList<>();
        private final List<Double> scriptedResults = new ArrayList<>();

        public void scriptResult(double value) {
            scriptedResults.add(value);
        }

        public List<int[]> getCalls() {
            return calls;
        }

        @Override
        public double distance(int colorA, int colorB) {
            calls.add(new int[] {colorA, colorB});
            if (scriptedResults.isEmpty()) {
                throw new IllegalStateException("No scripted distance available for call " + calls.size());
            }
            return scriptedResults.remove(0);
        }
    }

    @Test
    public void testToBinaryArrayUsesDistanceFinderForEachPixel() {
        SpyColorDistanceFinder spy = new SpyColorDistanceFinder();
        spy.scriptResult(10.0);
        spy.scriptResult(100.0);
        spy.scriptResult(1.0);
        spy.scriptResult(99.0);

        int targetColor = 0x112233;
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(spy, targetColor, 50);

        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x00111111);
        image.setRGB(1, 0, 0x00222222);
        image.setRGB(0, 1, 0x00333333);
        image.setRGB(1, 1, 0x00444444);

        int[][] binary = binarizer.toBinaryArray(image);

        assertNotNull(binary);
        assertEquals(2, binary.length);
        assertEquals(2, binary[0].length);
        assertEquals(4, spy.getCalls().size());

        // Expected threshold behavior (< threshold => 1, otherwise 0).
        int[][] expected = {
            {1, 0},
            {1, 0}
        };
        assertArrayEquals(expected, binary);

        // Verify target color is passed for every call.
        for (int[] call : spy.getCalls()) {
            assertEquals(targetColor, call[1]);
        }
    }

    @Test
    public void testToBinaryArrayMasksOutAlphaBitsBeforeDistanceCalculation() {
        SpyColorDistanceFinder spy = new SpyColorDistanceFinder();
        spy.scriptResult(0.0);

        int targetColor = 0xABCDEF;
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(spy, targetColor, 10);

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, 0xFF123456);

        binarizer.toBinaryArray(image);

        assertEquals(1, spy.getCalls().size());
        int[] firstCall = spy.getCalls().get(0);
        assertEquals(0x123456, firstCall[0]);
        assertEquals(targetColor, firstCall[1]);
    }

    @Test
    public void testToBufferedImageConvertsZeroToBlackAndOneToWhite() {
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer((a, b) -> 0.0, 0x000000, 1);

        int[][] binary = {
            {0, 1},
            {1, 0}
        };

        BufferedImage image = binarizer.toBufferedImage(binary);

        assertNotNull(image);
        assertEquals(2, image.getWidth());
        assertEquals(2, image.getHeight());

        assertEquals(0x000000, image.getRGB(0, 0) & 0x00FFFFFF);
        assertEquals(0x00FFFFFF, image.getRGB(1, 0) & 0x00FFFFFF);
        assertEquals(0x00FFFFFF, image.getRGB(0, 1) & 0x00FFFFFF);
        assertEquals(0x000000, image.getRGB(1, 1) & 0x00FFFFFF);
    }

    @Test
    public void testToBinaryArrayUsesStrictlyLessThanThreshold() {
        SpyColorDistanceFinder spy = new SpyColorDistanceFinder();
        spy.scriptResult(49.999);
        spy.scriptResult(50.0);
        spy.scriptResult(50.001);

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(spy, 0x000000, 50);

        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x00010101);
        image.setRGB(1, 0, 0x00020202);
        image.setRGB(2, 0, 0x00030303);

        int[][] binary = binarizer.toBinaryArray(image);
        int[][] expected = {{1, 0, 0}};
        assertArrayEquals(expected, binary);
    }
}