import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
    /**
     * Finds connected pixel groups of 1s in an integer array representing a binary
     * image.
     * 
     * The input is a non-empty rectangular 2D array containing only 1s and 0s.
     * If the array or any of its subarrays are null, a NullPointerException
     * is thrown. If the array is otherwise invalid, an IllegalArgumentException
     * is thrown.
     *
     * Pixels are considered connected vertically and horizontally, NOT diagonally.
     * The top-left cell of the array (row:0, column:0) is considered to be
     * coordinate
     * (x:0, y:0). Y increases downward and X increases to the right. For example,
     * (row:4, column:7) corresponds to (x:7, y:4).
     *
     * The method returns a list of sorted groups. The group's size is the number
     * of pixels in the group. The centroid of the group
     * is computed as the average of each of the pixel locations across each
     * dimension.
     * For example, the x coordinate of the centroid is the sum of all the x
     * coordinates of the pixels in the group divided by the number of pixels in
     * that group.
     * Similarly, the y coordinate of the centroid is the sum of all the y
     * coordinates of the pixels in the group divided by the number of pixels in
     * that group.
     * The division should be done as INTEGER DIVISION.
     *
     * The groups are sorted in DESCENDING order according to Group's compareTo
     * method.
     * 
     * @param image a rectangular 2D array containing only 1s and 0s
     * @return the found groups of connected pixels in descending order
     */
    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        List<Group> groups = new ArrayList<>();
        boolean[][] visited = new boolean[image.length][image[0].length];

        for (int row = 0; row < image.length; row++) {
            for (int col = 0; col < image[0].length; col++) {
                if (image[row][col] == 1 && !visited[row][col]) {
                    int[] result = exploreGroup(new int[] { row, col }, image, visited);

                    int size = result[0];
                    int sumX = result[1];
                    int sumY = result[2];

                    int groupX = sumX / size;
                    int groupY = sumY / size;

                    groups.add(new Group(size, new Coordinate(groupX, groupY)));
                }
            }
        }
        groups.sort(Collections.reverseOrder());

        return groups;
    }

    private static int[] exploreGroup(int[] currentLoc, int[][] image, boolean[][] visited) {

        int curR = currentLoc[0];
        int curC = currentLoc[1];

        if (curR < 0 || curR >= image.length ||
                curC < 0 || curC >= image[0].length ||
                image[curR][curC] == 0 ||
                visited[curR][curC]) {
            return new int[] { 0, 0, 0 };
        }

        visited[curR][curC] = true;

        int size = 1;
        int sumX = curC;
        int sumY = curR;

        for (int[] move : possibleMoves(image, currentLoc)) {
            int[] result = exploreGroup(move, image, visited);

            size += result[0];
            sumX += result[1];
            sumY += result[2];
        }

        return new int[] { size, sumX, sumY };

    }

    private static List<int[]> possibleMoves(int[][] image, int[] current) {
        List<int[]> moves = new ArrayList<>();

        int curR = current[0];
        int curC = current[1];

        int[][] directions = {
                { 1, 0 }, // down
                { -1, 0 }, // up
                { 0, 1 }, // right
                { 0, -1 } // left
        };

        for (int[] direction : directions) {
            int newR = curR + direction[0];
            int newC = curC + direction[1];

            if (newR >= 0 && newR < image.length && newC >= 0 && newC < image[0].length) {
                moves.add(new int[] { newR, newC });
            }
        }

        return moves;

    }

}
