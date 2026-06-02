import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

public class DistanceImageBinarizerTest {

    @Test
    public void testUsesFakeBinarizer() {
        ImageBinarizer fake = new FakeImageBinarizer();

        int[][] result = fake.toBinaryArray(null);

        assertEquals(1, result[0][0]);
    }

    private static class FakeImageBinarizer implements ImageBinarizer {

        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            return new int[][] {
                    { 1, 0 },
                    { 0, 1 }
            };
        }

        @Override
        public BufferedImage toBufferedImage(int[][] image) {
            return null;
        }
    }

    @Test
    public void testToBinaryArraySameAsTargetBecomesWhite() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xFF0000);

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), 0xFF0000, 10);

        int[][] expected = {
                { 1 }
        };

        assertArrayEquals(expected, binarizer.toBinaryArray(image));
    }

    @Test
    public void testToBinaryArrayDifferentFromTargetBecomesBlack() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x0000FF);

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), 0xFF0000, 10);

        int[][] expected = {
                { 0 }
        };

        assertArrayEquals(expected, binarizer.toBinaryArray(image));
    }

    @Test
    public void testToBinaryArrayMultiplePixels() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        image.setRGB(0, 0, 0xFF0000); // red
        image.setRGB(1, 0, 0xFE0000); // close red
        image.setRGB(0, 1, 0x0000FF); // blue
        image.setRGB(1, 1, 0x00FF00); // green

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), 0xFF0000, 5);

        int[][] expected = {
                { 1, 1 },
                { 0, 0 }
        };

        assertArrayEquals(expected, binarizer.toBinaryArray(image));
    }

    @Test
    public void testToBufferedImageConvertsOnesToWhiteAndZerosToBlack() {
        int[][] binary = {
                { 1, 0 },
                { 0, 1 }
        };

        ImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), 0xFFFFFF, 10);

        BufferedImage result = binarizer.toBufferedImage(binary);

        assertEquals(0xFFFFFF, result.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(0x000000, result.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(0x000000, result.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(0xFFFFFF, result.getRGB(1, 1) & 0xFFFFFF);
    }

    @Test
    public void testToBufferedImageWidthAndHeight() {
        int[][] binary = {
                { 1, 0, 1 },
                { 0, 1, 0 }
        };

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), 0xFF0000, 10);

        BufferedImage result = binarizer.toBufferedImage(binary);

        assertEquals(3, result.getWidth());
        assertEquals(2, result.getHeight());
    }


    @Test
    public void testToBinaryArrayNullThrowsException() {
        ImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), 0xFFFFFF, 10);

        assertThrows(NullPointerException.class, () -> {
            binarizer.toBinaryArray(null);
        });
    }


    @Test
    public void testToBufferedImageNullThrowsException() {
        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new EuclideanColorDistance(), 0xFF0000, 10);

        assertThrows(NullPointerException.class, () -> {
            binarizer.toBufferedImage(null);
        });
    }
}
