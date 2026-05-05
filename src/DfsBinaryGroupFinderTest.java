import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DfsBinaryGroupFinderTest {

    private final DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

    @Test
    void nullImage_throwsException() {
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(null));
    }

    @Test
    void nullRow_throwsException() {
        int[][] image = {
            {1, 0},
            null
        };
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(image));
    }

    @Test
    void nonRectangularArray_throwsException() {
        int[][] image = {
            {1, 0},
            {1}
        };
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(image));
    }

    @Test
    void invalidValues_throwsException() {
        int[][] image = {
            {1, 2},
            {0, 1}
        };
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(image));
    }


    @Test
    void singlePixelGroup() {
        int[][] image = {
            {0, 0},
            {0, 1}
        };

        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(1, result.size());
        Group g = result.get(0);

        assertEquals(1, g.size());
        assertEquals(1, g.centroid().x());
        assertEquals(1, g.centroid().y());
    }

    @Test
    void noGroups_returnsEmptyList() {
        int[][] image = {
            {0, 0},
            {0, 0}
        };

        List<Group> result = finder.findConnectedGroups(image);

        assertTrue(result.isEmpty());
    }

    // -------------------------
    // 3. CONNECTIVITY (NO DIAGONALS)
    // -------------------------

    @Test
    void diagonalOnes_areSeparateGroups() {
        int[][] image = {
            {1, 0},
            {0, 1}
        };

        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(1, result.get(1).size());
    }

    @Test
    void horizontalConnection_formsSingleGroup() {
        int[][] image = {
            {1, 1}
        };

        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).size());
    }

    @Test
    void verticalConnection_formsSingleGroup() {
        int[][] image = {
            {1},
            {1}
        };

        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).size());
    }

    // -------------------------
    // 4. CENTROID CALCULATION
    // -------------------------

    @Test
    void centroid_isIntegerDivision() {
        int[][] image = {
            {1, 1}
        };
        // pixels: (0,0) and (1,0)
        // centroid x = (0+1)/2 = 0 (integer division)
        // centroid y = (0+0)/2 = 0

        List<Group> result = finder.findConnectedGroups(image);
        Group g = result.get(0);

        assertEquals(0, g.centroid().x());
        assertEquals(0, g.centroid().y());
    }

    @Test
    void centroid_multiplePixels() {
        int[][] image = {
            {1, 1},
            {1, 0}
        };
        // pixels: (0,0), (1,0), (0,1)
        // x = (0+1+0)/3 = 0
        // y = (0+0+1)/3 = 0

        List<Group> result = finder.findConnectedGroups(image);
        Group g = result.get(0);

        assertEquals(3, g.size());
        assertEquals(0, g.centroid().x());
        assertEquals(0, g.centroid().y());
    }

    // -------------------------
    // 5. SORTING (DESCENDING)
    // -------------------------

    @Test
    void groups_sortedBySizeDescending() {
        int[][] image = {
            {1, 1, 0},
            {0, 0, 1}
        };

        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(2, result.size());

        // first group should be larger
        assertTrue(result.get(0).size() >= result.get(1).size());
    }

    @Test
    void sorting_usesCentroidWhenSizesEqual() {
        int[][] image = {
            {1, 0, 1}
        };

        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(2, result.size());

        Group g1 = result.get(0);
        Group g2 = result.get(1);

        // same size → compare by centroid.x DESC
        assertTrue(g1.centroid().x() >= g2.centroid().x());
    }

    // -------------------------
    // 6. COMPLEX CASE
    // -------------------------

    @Test
    void multipleGroups_complexLayout() {
        int[][] image = {
            {1, 1, 0, 0},
            {1, 0, 0, 1},
            {0, 0, 1, 1}
        };

        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(2, result.size());

        // Largest group should come first
        assertTrue(result.get(0).size() >= result.get(1).size());
    }
}