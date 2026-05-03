import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                {1, 0},
                {0, 1}
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

        DistanceImageBinarizer binarizer =
            new DistanceImageBinarizer(new EuclideanColorDistance(), 0xFF0000, 10);

        int[][] expected = {
            {1}
        };

        assertArrayEquals(expected, binarizer.toBinaryArray(image));
    }  

}
