import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
   /**
    * Finds connected pixel groups of 1s in an integer array representing a binary image.
    * 
    * The input is a non-empty rectangular 2D array containing only 1s and 0s.
    * If the array or any of its subarrays are null, a NullPointerException
    * is thrown. If the array is otherwise invalid, an IllegalArgumentException
    * is thrown.
    *
    * Pixels are considered connected vertically and horizontally, NOT diagonally.
    * The top-left cell of the array (row:0, column:0) is considered to be coordinate
    * (x:0, y:0). Y increases downward and X increases to the right. For example,
    * (row:4, column:7) corresponds to (x:7, y:4).
    *
    * The method returns a list of sorted groups. The group's size is the number 
    * of pixels in the group. The centroid of the group
    * is computed as the average of each of the pixel locations across each dimension.
    * For example, the x coordinate of the centroid is the sum of all the x
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * Similarly, the y coordinate of the centroid is the sum of all the y
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * The division should be done as INTEGER DIVISION.
    *
    * The groups are sorted in DESCENDING order according to Group's compareTo method.
    * 
    * @param image a rectangular 2D array containing only 1s and 0s
    * @return the found groups of connected pixels in descending order
    */
    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        if(image == null) throw new NullPointerException();
        for(int[] row : image) {
            if(row == null) throw new NullPointerException();
            if(row.length != image[0].length) throw new IllegalArgumentException();
            for(int pixel : row) { 
                if(pixel != 0 && pixel != 1) throw new IllegalArgumentException(); 
            }
        }

        boolean[][] visited = new boolean[image.length][image[0].length];
        List<Group> groups = findConnectedGroupsHelper(image, visited);
        Collections.sort(groups, Collections.reverseOrder());
        return groups;
    }

    private List<Group> findConnectedGroupsHelper(int[][] image, boolean[][] seen) {
        List<Group> groups = new ArrayList<>();
        for(int r = 0; r < image.length; r++) { // row is actually the y coordinate 
            for(int c = 0; c < image[r].length; c++) { // column is actualy the x coordinate
                if(image[r][c] == 1 && !seen[r][c]) { 
                    Group group = dfs(image, seen, c, r);
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    private static Group dfs(int[][] image, boolean[][] visited, int x, int y) { 
        Queue<int[]> imageQueue = new LinkedList<>();
        imageQueue.add(new int[]{x, y});

        int xTotal = 0; // used for calculating centroid x position 
        int yTotal = 0; // used for calulating centroid y position
        int size = 0; // used as amount to determine centroid
        
        while(!imageQueue.isEmpty()){
            int[] current = imageQueue.poll();
            int curX = current[0];
            int curY = current[1];

            if(visited[curY][curX]) continue;
            visited[curY][curX] = true;

            xTotal += curX;
            yTotal += curY;
            size++;

            for(int[] move: possibleMoves(image, curX, curY)) {
                if(!visited[move[1]][move[0]]) imageQueue.add(move);
            }
        }

        return new Group(size, new Coordinate(xTotal / size, yTotal / size));
    }

    private static List<int[]> possibleMoves(int[][] image, int x, int y) {
        List<int[]> possibleMoves = new ArrayList<>();
        int[][] moves = new int[][]{ 
            {-1, 0}, 
            {1, 0}, 
            {0, -1}, 
            {0, 1} 
        };

        for(int[] move : moves){
            int newX = x + move[0];
            int newY = y + move[1];

            if(newY >= 0 && newY < image.length &&
               newX >= 0 && newX < image[0].length &&
               image[newY][newX] == 1){
                    possibleMoves.add(new int[]{newX, newY});
               }
        }

        return possibleMoves;
    }
}
