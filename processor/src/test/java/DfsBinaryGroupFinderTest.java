import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class DfsBinaryGroupFinderTest {

    @Test
    public void testSinglePixelGroup() {
        int[][] image = {
                { 1 }
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        List<Group> expected = List.of(
                new Group(1, new Coordinate(0, 0)));

        assertEquals(expected, finder.findConnectedGroups(image));
    }

    @Test
    public void testDfsBinaryGroupFinderSingleGroup() {
        int[][] image = {
                { 1, 1, 0 },
                { 0, 1, 0 },
                { 0, 0, 0 }
        };

        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(1, groups.size());

        Group group = groups.get(0);

        assertEquals(3, group.size());
        assertEquals(0, group.centroid().x());
        assertEquals(0, group.centroid().y());
    }

    @Test
    public void testTwoSeparateGroupsSortedDescending() {
        int[][] image = {
                { 1, 1, 0, 0 },
                { 1, 0, 0, 1 },
                { 0, 0, 0, 1 }
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        List<Group> expected = List.of(
                new Group(3, new Coordinate(0, 0)),
                new Group(2, new Coordinate(3, 1)));

        assertEquals(expected, finder.findConnectedGroups(image));
    }

    @Test
    public void testDfsBinaryGroupFinderMultipleGroupsSortedDescending() {
        int[][] image = {
                { 1, 1, 0, 1 },
                { 0, 1, 0, 1 },
                { 0, 0, 0, 1 }
        };

        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(2, groups.size());

        Group first = groups.get(0);
        Group second = groups.get(1);

        assertEquals(3, first.size());
        assertEquals(3, first.centroid().x());
        assertEquals(1, first.centroid().y());

        assertEquals(3, second.size());
        assertEquals(0, second.centroid().x());
        assertEquals(0, second.centroid().y());
    }
}
