import static org.junit.Assert.*;
import org.junit.Test;

public class EuclideanColorDistanceTest {

  private static final double EPSILON = 0.0001;

  private final ColorDistanceFinder finder = new EuclideanColorDistance();

  @Test
  public void testSameColorBlackDistanceIsZero() {
    assertEquals(0.0, finder.distance(0x000000, 0x000000), EPSILON);
  }

  @Test
  public void testSameColorWhiteDistanceIsZero() {
    assertEquals(0.0, finder.distance(0xFFFFFF, 0xFFFFFF), EPSILON);
  }

  @Test
  public void testBlackToWhite() {
    double expected = Math.sqrt(255 * 255 + 255 * 255 + 255 * 255);

    assertEquals(expected, finder.distance(0x000000, 0xFFFFFF), EPSILON);
  }

  @Test
  public void testBlackToRed() {
    assertEquals(255.0, finder.distance(0x000000, 0xFF0000), EPSILON);
  }

  @Test
  public void testBlackToGreen() {
    assertEquals(255.0, finder.distance(0x000000, 0x00FF00), EPSILON);
  }

  @Test
  public void testBlackToBlue() {
    assertEquals(255.0, finder.distance(0x000000, 0x0000FF), EPSILON);
  }

  @Test
  public void testRedToGreen() {
    double expected = Math.sqrt(255 * 255 + 255 * 255);

    assertEquals(expected, finder.distance(0xFF0000, 0x00FF00), EPSILON);
  }

  @Test
  public void testRedToBlue() {
    double expected = Math.sqrt(255 * 255 + 255 * 255);


    assertEquals(expected, finder.distance(0xFF0000, 0x0000FF), EPSILON);
  }    

}
