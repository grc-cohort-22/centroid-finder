import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BinarizingImageGroupFinderTest {

 @Test
 public void testFindConnectedGroupsWithRealClasses() {
  BufferedImage image = new BufferedImage(3, 2, BufferedImage.TYPE_INT_RGB);

  image.setRGB(0, 0, 0xFFFFFF);
  image.setRGB(1, 0, 0xFFFFFF);
  image.setRGB(2, 0, 0x000000);

  image.setRGB(0, 1, 0x000000);
  image.setRGB(1, 1, 0xFFFFFF);
  image.setRGB(2, 1, 0x000000);

  ImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), 0xFFFFFF, 10);

  BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();

  ImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

  List<Group> groups = finder.findConnectedGroups(image);

  assertEquals(1, groups.size());
  assertEquals(new Group(3, new Coordinate(0, 0)), groups.get(0));
 }

}
