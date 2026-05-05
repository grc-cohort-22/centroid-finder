import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DistanceImageBinarizerTest {

    @Test
    void testCoordinateAccuracy50x30() {
        int expectedWidth = 50;
        int expectedHeight = 30;

        BufferedImage image = new BufferedImage(
            expectedWidth, expectedHeight, BufferedImage.TYPE_INT_ARGB
        );

        int[][] matrix = toBinaryArray(image);

        // Rows must equal image height
        assertTrue(matrix.length == expectedHeight,
            "Matrix row count should equal image height");

        // Each row's column count must equal image width
        assertTrue(matrix[0].length == expectedWidth,
            "Matrix column count should equal image width");
    }
}
