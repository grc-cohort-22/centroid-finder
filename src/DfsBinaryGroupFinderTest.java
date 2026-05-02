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
}
