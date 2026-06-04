ImageSummaryApp: have three command-line arguments that are imageinputpath, hex target color and threshold. Threshold decides how close does a pixel color need to be to the target color.

ColorDistanceFinder: Computes the distance between two colors Each color is represented as a 24-bit integer (0xRRGGBB) where the red, green, and blue components can be xtracted using bit shifting and masking. The interface has two parameteres int colorA (the first color as a 24-bit hex RGB integer)and int colorB(the second color as a 24-bit hex RGB integer)

EuclideanColorDistance:The Euclidean color distance is calculated by treating each color as a point, in 3D space (red, green, blue) and applying the Euclidean distance formula: sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2). This gives a measure of how visually different the two colors are.

ImageBinarizer: Converts the given BufferedImage into a binary 2D array or Converts a binary 2D array into a BufferedImage. Each entry in the returned array is either 0 or 1, representing a black or white pixel. Black pixels should be represented as x000000 and white pixels should be represented as xFFFFFF.

DistanceImageBinarizer: Converts the given BufferedImage into a binary 2D array using color distance and a threshold. Each entry in the returned array is either 0 or 1, representing a black or white pixel. Converts a binary 2D array into a BufferedImage.return BufferedImage.

ImageGroupFinder:Finds connected groups in an image. The groups are sorted in DESCENDING order according to Group's compareTo method.It returns connected groups in an image sorted in descending order

BinarizingImageGroupFinder: Finds connected groups of white pixels in the given image. using ImageBinarizer.

DfsBinaryGroupFinder:Finds connected pixel groups of 1s in an integer array representing a binary image. The input is a non-empty rectangular 2D array containing only 1s and 0s The input is a non-empty rectangular 2D array containing only 1s and 0s The top-left cell of the array (row:0, column:0) is considered to be coordinate(x:0, y:0). Y increases downward and X increases to the right. For example,(row:4, column:7) corresponds to (x:7, y:4). The method returns a list of sorted groups. The group's size is the number of pixels in the group. The centroid of the group,is computed as the average of each of the pixel locations across each dimension.The groups are sorted in DESCENDING order according to Group's compareTo method

Group:Groups are naturally comparable. The comparison is done first by the group's size, then by the x coordinate of the centroid, and finally by the y coordinate. In a method that returns groups, they should be sorted in this natural order.
