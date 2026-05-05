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
}
