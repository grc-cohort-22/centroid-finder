import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
 * Finds connected groups of 1s using BFS.
 * This avoids deep recursion problems from DFS.
 */
public class BfsBinaryGroupFinder implements BinaryGroupFinder {

    @Override
    public List<Group> findConnectedGroups(int[][] image) {

        List<Group> groups = new ArrayList<>();
        boolean[][] visited =
            new boolean[image.length][image[0].length];

        for (int row = 0; row < image.length; row++) {
            for (int col = 0; col < image[0].length; col++) {

                if (image[row][col] == 1 && !visited[row][col]) {

                    Group group =
                        exploreGroup(image, visited, row, col);

                    groups.add(group);
                }
            }
        }

        groups.sort(Collections.reverseOrder());

        return groups;
    }

    private Group exploreGroup(
            int[][] image,
            boolean[][] visited,
            int startRow,
            int startCol) {

        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[] { startRow, startCol });
        visited[startRow][startCol] = true;

        int size = 0;
        int sumX = 0;
        int sumY = 0;

        while (!queue.isEmpty()) {

            int[] current = queue.remove();

            int row = current[0];
            int col = current[1];

            size++;
            sumX += col;
            sumY += row;

            addNeighbor(image, visited, queue, row - 1, col);
            addNeighbor(image, visited, queue, row + 1, col);
            addNeighbor(image, visited, queue, row, col - 1);
            addNeighbor(image, visited, queue, row, col + 1);
        }

        int centroidX = sumX / size;
        int centroidY = sumY / size;

        return new Group(
            size,
            new Coordinate(centroidX, centroidY)
        );
    }

    private void addNeighbor(
            int[][] image,
            boolean[][] visited,
            Queue<int[]> queue,
            int row,
            int col) {

        if (row < 0 || row >= image.length) {
            return;
        }

        if (col < 0 || col >= image[0].length) {
            return;
        }

        if (image[row][col] == 0) {
            return;
        }

        if (visited[row][col]) {
            return;
        }

        visited[row][col] = true;
        queue.add(new int[] { row, col });
    }
}
