import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;

public class DfsBinaryGroupFinderTest {

    @Test
    public void DfsBinaryGroupFinderBasic() {
        int[][] image = {
            {1,1,1,0,1,1},
            {0,0,0,1,0,1},
            {1,1,1,1,0,0},
            {0,1,0,1,0,1},
            {1,1,1,0,1,1},
        };
        DfsBinaryGroupFinder test = new DfsBinaryGroupFinder();
        List<Group> actual = test.findConnectedGroups(image);

        assertEquals(19, actual.size());
    }

    @Test
    public void DfsBinaryGroupFinderWeirdBox() {
        int[][] image = {
            {1,1,1,0,1,1,0,1,0},
            {0,1,0,1,0,1,1,1,1,0},
            {1,1,1,1,0,0,0,0,1,1,1},
            {0,1,0,1},
            {1,1,1,0,1,1},
        };
        DfsBinaryGroupFinder test = new DfsBinaryGroupFinder();
        List<Group> actual = test.findConnectedGroups(image);

        assertEquals(26, actual.size());
    }

    @Test
    public void DfsBinaryGroupFinderWeirdRectangle() {
        int[][] image = {
            {1,1,},
            {0,1,0,1,0},
            {1,1,1,1,0},
            {0,1,0,1},
            {1,1,1,0,1,1},
            {1},
            {0,0,0},
            {1,0,0}
        };
        DfsBinaryGroupFinder test = new DfsBinaryGroupFinder();
        List<Group> actual = test.findConnectedGroups(image);

        assertEquals(26, actual.size());
    }
}