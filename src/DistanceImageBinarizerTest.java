import java.awt.Color;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DistanceImageBinarizerTest {
    ColorDistanceFinder finder = new EuclideanColorDistance();
    ImageBinarizer binarizer = new DistanceImageBinarizer(finder, 0, 0);

    @Test
    void testCoordinateAccuracy50x30() {
        int expectedWidth = 50;
        int expectedHeight = 30;
        

        BufferedImage image = new BufferedImage(
            expectedWidth, expectedHeight, BufferedImage.TYPE_INT_ARGB
        );

        int[][] matrix = binarizer.toBinaryArray(image);

        // Rows must equal image height
        assertTrue(matrix.length == expectedHeight,
            "Matrix row count should equal image height");

        // Each row's column count must equal image width
        assertTrue(matrix[0].length == expectedWidth,
            "Matrix column count should equal image width");
    }
    @Test
    void testCoordinateAccuracy1x1() {
        int expectedWidth = 1;
        int expectedHeight = 1;

        BufferedImage image = new BufferedImage(
            expectedWidth, expectedHeight, BufferedImage.TYPE_INT_ARGB
        );

        int[][] matrix = binarizer.toBinaryArray(image);

        assertTrue(matrix.length == expectedHeight,
            "Matrix row count should equal image height for 1x1 image");

        assertTrue(matrix[0].length == expectedWidth,
            "Matrix column count should equal image width for 1x1 image");
    }
    @Test
    void testCoordinateAccuracyTallImage10x200() {
        int expectedWidth = 10;
        int expectedHeight = 200;

        BufferedImage image = new BufferedImage(
            expectedWidth, expectedHeight, BufferedImage.TYPE_INT_ARGB
        );

        int[][] matrix = binarizer.toBinaryArray(image);

        assertTrue(matrix.length == expectedHeight,
            "Matrix row count should equal image height for tall image");

        assertTrue(matrix[0].length == expectedWidth,
            "Matrix column count should equal image width for tall image");
    }
    @Test
    void testBinaryValuesAreAccurate() {
        int width = 3;
        int height = 3;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Set pixel (0,0) to pure white — far from black (targetColor=0), distance > 0
        // With threshold=0, distance >= 0 is always true, so this pixel should be 1
        image.setRGB(0, 0, new Color(255, 255, 255).getRGB());

        // Set pixel (1,1) to pure black — matches targetColor exactly, distance = 0
        // With threshold=0, distance >= 0 is still true, so this should also be 1
        image.setRGB(1, 1, new Color(0, 0, 0).getRGB());

        int[][] matrix = binarizer.toBinaryArray(image);

        // White pixel at (col=0, row=0) → matrix[0][0] should be 1
        assertTrue(matrix[0][0] == 1,
            "White pixel far from target color should return 1 when threshold is 0");

        // Black pixel at (col=1, row=1) → matrix[1][1] should be 1
        assertTrue(matrix[1][1] == 1,
            "Black pixel at distance 0 from target should still return 1 when threshold is 0");
    }
    @Test
    void testToBufferedImage(){

        int[][] image = {
            {1, 0},
            {0, 1}
        };

        BufferedImage result = binarizer.toBufferedImage(image);

        assertEquals(result.getRGB(0, 0), -1);
        assertEquals(result.getRGB(1, 0), -16777216);
        assertEquals(result.getRGB(0, 1), -16777216);
        assertEquals(result.getRGB(1, 1), -1);

    }
}
