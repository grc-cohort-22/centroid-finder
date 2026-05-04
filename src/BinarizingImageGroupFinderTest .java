import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BinarizingImageGroupFinderTest {

    @Test
    public void testReturnsGroups() {

        ImageBinarizer binarizer = new ImageBinarizer() {
            public int[][] toBinaryArray(BufferedImage image) {
                return new int[][] {{1,0},{1,1}};
            }
            public BufferedImage toBufferedImage(int[][] image) {
                return null;
            }
        };

        BinaryGroupFinder groupFinder = img ->
            List.of(new Group(3, new Coordinate(0,0)));

        BinarizingImageGroupFinder finder =
            new BinarizingImageGroupFinder(binarizer, groupFinder);

        BufferedImage img =
            new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        List<Group> result = finder.findConnectedGroups(img);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
    }

    @Test
    public void testEmptyGroups() {

        ImageBinarizer bin = image -> new int[][] {{0,0},{0,0}};
        BinaryGroupFinder gf = img -> List.of();

        BinarizingImageGroupFinder finder =
            new BinarizingImageGroupFinder(bin, gf);

        BufferedImage img =
            new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        assertTrue(finder.findConnectedGroups(img).isEmpty());
    }
}