import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DfsBinaryGroupFinderTest {

    private final DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

    @Test
    void singlePixelGroup() {
        int[][] image = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(1, groups.size());
        Group g = groups.get(0);

        assertEquals(1, g.size());
        assertEquals(1, g.centroid().x());
        assertEquals(1, g.centroid().y());
    }

    @Test
    void oneLargeGroup() {
        int[][] image = {
                {1, 1},
                {1, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(1, groups.size());
        Group g = groups.get(0);

        assertEquals(4, g.size());
        // coordinates: (0,0), (1,0), (0,1), (1,1)
        // centroid: (2/4=0, 2/4=0)
        assertEquals(0, g.centroid().x());
        assertEquals(0, g.centroid().y());
    }

    @Test
    void multipleGroups() {
        int[][] image = {
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {0, 0, 1, 0}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(3, groups.size());

        // Expected groups:
        // Left vertical (size 2, centroid (0,0))
        // Right vertical (size 2, centroid (3,0))
        // Single pixel (size 1, centroid (2,2))

        assertTrue(groups.stream().anyMatch(g ->
                g.size() == 2 && g.centroid().x() == 0 && g.centroid().y() == 0));

        assertTrue(groups.stream().anyMatch(g ->
                g.size() == 2 && g.centroid().x() == 3 && g.centroid().y() == 0));

        assertTrue(groups.stream().anyMatch(g ->
                g.size() == 1 && g.centroid().x() == 2 && g.centroid().y() == 2));
    }

    @Test
    void diagonalNotConnected() {
        int[][] image = {
                {1, 0},
                {0, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(2, groups.size());

        for (Group g : groups) {
            assertEquals(1, g.size());
        }
    }

    @Test
    void sortingDescendingOrder() {
        int[][] image = {
                {1, 1, 0, 0},
                {0, 0, 0, 1},
                {0, 1, 0, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(3, groups.size());

        // sizes should be in descending order
        assertTrue(groups.get(0).size() >= groups.get(1).size());
        assertTrue(groups.get(1).size() >= groups.get(2).size());
    }

    @Test
    void sortingTieBreakerByCentroid() {
        int[][] image = {
                {1, 0, 1},
                {0, 0, 0},
                {1, 0, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(4, groups.size());

        // All size 1, sorted by x then y DESCENDING (since overall is descending order)
        for (int i = 0; i < groups.size() - 1; i++) {
            Group a = groups.get(i);
            Group b = groups.get(i + 1);

            int cmp = a.compareTo(b);
            assertTrue(cmp >= 0); // descending order means earlier >= later
        }
    }

    @Test
    void unsortedInputStillProducesSortedOutput() {
        int[][] image = {
                {0, 1, 0, 1},
                {1, 1, 0, 0},
                {0, 0, 1, 0}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        // Verify result is sorted regardless of traversal order
        for (int i = 0; i < groups.size() - 1; i++) {
            assertTrue(groups.get(i).compareTo(groups.get(i + 1)) >= 0);
        }
    }

    @Test
    void emptyLikeNoOnes() {
        int[][] image = {
                {0, 0},
                {0, 0}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertTrue(groups.isEmpty());
    }

    @Test
    void nullImageThrows() {
        assertThrows(NullPointerException.class, () ->
                finder.findConnectedGroups(null));
    }

    @Test
    void nullRowThrows() {
        int[][] image = new int[2][];
        image[0] = new int[]{1, 0};
        image[1] = null;

        assertThrows(NullPointerException.class, () ->
                finder.findConnectedGroups(image));
    }

    @Test
    void nonRectangularThrows() {
        int[][] image = {
                {1, 0},
                {1}
        };

        assertThrows(IllegalArgumentException.class, () ->
                finder.findConnectedGroups(image));
    }
}