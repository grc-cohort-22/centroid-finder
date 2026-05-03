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

 @Test
 public void testFindConnectedGroupsReturnsGroupsFromBinaryGroupFinder() {
  ImageBinarizer fakeBinarizer = new FakeImageBinarizer();
  BinaryGroupFinder fakeGroupFinder = new FakeBinaryGroupFinder();

  BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, fakeGroupFinder);

  BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

  List<Group> expected = List.of(
    new Group(2, new Coordinate(0, 0)));

  assertEquals(expected, finder.findConnectedGroups(image));
 }

 @Test
 public void testFakeBinarizerCreatesBinaryArrayUsedByGroupFinder() {
  ImageBinarizer fakeBinarizer = new FakeImageBinarizer();
  CheckingBinaryGroupFinder checkingGroupFinder = new CheckingBinaryGroupFinder();

  BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, checkingGroupFinder);

  BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

  finder.findConnectedGroups(image);

  assertTrue(checkingGroupFinder.receivedExpectedArray);
 }

 private static class FakeImageBinarizer implements ImageBinarizer {

  @Override
  public int[][] toBinaryArray(BufferedImage image) {
   return new int[][] {
     { 1, 0 },
     { 0, 1 }
   };
  }

  @Override
  public BufferedImage toBufferedImage(int[][] image) {
   return null;
  }
 }

 private static class FakeBinaryGroupFinder implements BinaryGroupFinder {

  @Override
  public List<Group> findConnectedGroups(int[][] image) {
   return List.of(
     new Group(2, new Coordinate(0, 0)));
  }
 }

 private static class CheckingBinaryGroupFinder implements BinaryGroupFinder {
  boolean receivedExpectedArray = false;

  @Override
  public List<Group> findConnectedGroups(int[][] image) {
   if (image[0][0] == 1 &&
     image[0][1] == 0 &&
     image[1][0] == 0 &&
     image[1][1] == 1) {
    receivedExpectedArray = true;
   }

   return List.of();
  }
 }

 @Test
 public void testFindConnectedGroupsNullImageThrowsException() {
  ImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), 0xFFFFFF, 10);

  BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();

  ImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

  assertThrows(NullPointerException.class, () -> {
   finder.findConnectedGroups(null);
  });
 }

}
