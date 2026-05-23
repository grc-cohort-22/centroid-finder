import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class LargestGroupSelectorTest {

 @Test
 public void selectLargestReturnEmptyListIsNull() {

  LargestGroupSelector selector = new LargestGroupSelector();
  Optional<Group> result = selector.selectLargest(null);
  assertTrue(result.isEmpty());
 }

 @Test
 public void selectLargestReturnsEmptyWhenListIsEmpty() {
  LargestGroupSelector selector = new LargestGroupSelector();

  Optional<Group> result = selector.selectLargest(new ArrayList<>());

  assertTrue(result.isEmpty());
 }

 @Test
 public void selectLargestReturnsOnlyGroupWhenListHasOneGroup() {
  LargestGroupSelector selector = new LargestGroupSelector();

  Group group = new Group(1, new Coordinate(1, 1));

  Optional<Group> result = selector.selectLargest(List.of(group));

  assertTrue(result.isPresent());
  assertSame(group, result.get());
 }

 @Test
 public void selectLargestReturnsLargestGroup() {
  LargestGroupSelector selector = new LargestGroupSelector();

  Group small = new Group(1, new Coordinate(1, 1));

  Group medium = new Group(2, new Coordinate(2, 2));

  Group large = new Group(3, new Coordinate(3, 3));

  Optional<Group> result = selector.selectLargest(List.of(small, large, medium));

  assertTrue(result.isPresent());
  assertSame(large, result.get());
 }

}
