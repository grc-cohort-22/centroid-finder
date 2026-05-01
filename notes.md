# Centroid Finder Notes

## ImageSummaryApp

The app takes in three arguments:
1. A path to the image file
2. An RGB color
3. A threshold value that determines the degree of binarization.

The application first parses the image from a hexadecimal string to a 24-bit integer. This integer is then used in a Euclidean Distance Calculation that assigns white (1) to pixels below the threshold value and black (0) to those above. After binarization completes, the formed groups of white pixels are computed to find the centroid. Lastly, data is written to a CSV file.

---

## ImageBinarizer

This interface contains two methods: `toBinaryArray()` and `toBufferedImage()`.

- `toBinaryArray()` converts a BufferedImage into a 2D array of 0s and 1s.
- `toBufferedImage()` does the opposite, converting the 2D array back to a BufferedImage. 0s should be shown with `0x000000` (black) and 1s as `0xFFFFFF` (white).

---

## ColorDistanceFinder

An interface that outlines a method to calculate the color distance between two colors. Implementations should use bit shifting and masking. This interface is implemented by `EuclideanColorDistance`.

---

## EuclideanColorDistance

Calculates the distance between two hexadecimal colors. In Euclidean space, colors are computed based on how close they approximate the red, green, and blue channels. The algorithm takes in two 24-bit integers to determine their distance.

---

## BinarizingImageGroupFinder

`BinarizingImageGroupFinder` implements `ImageGroupFinder`. Its constructor takes:
- An `ImageBinarizer` (converts a `BufferedImage` to a 2D binary array of 0s and 1s)
- A `BinaryGroupFinder` (finds connected groups of 1s/white pixels in that array)

`findConnectedGroups()` returns a `List<Group>`, where each `Group` holds the pixel count (size) and the centroid's x and y coordinates, sorted in descending order.

---

## BinaryGroupFinder

Interface for finding connected groups of 1s in a 2D integer array.

- Input: a non-empty rectangular 2D array containing only 1s and 0s.
- Throws `NullPointerException` if the array or any subarray is null.
- Throws `IllegalArgumentException` if the array is otherwise invalid.
- Pixels are connected horizontally and vertically -- NOT diagonally.
- Coordinate system: top-left cell is (x:0, y:0). Y increases downward, X increases to the right. E.g., (row:4, column:7) -> (x:7, y:4).
- Centroid uses INTEGER DIVISION:
  - x centroid = sum of all x coordinates / number of pixels in group
  - y centroid = sum of all y coordinates / number of pixels in group
- Groups sorted in DESCENDING order by `Group.compareTo()`: size first, then descending y, then descending x.

---

## DfsBinaryGroupFinder

`findConnectedGroups` finds groups of 1s in a 2D integer array and returns:
- `size()` -- the number of pixels in the blob
- Centroid (x, y) -- sum of coordinates divided by group size using INTEGER DIVISION

Blobs are connected horizontally and vertically, NOT diagonally.
