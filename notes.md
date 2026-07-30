## Wave 1 Notes

ImageSummaryApp is like the main file that runs the whole program. It takes in the image file, the target color, and the threshold number. Then it creates the color distance finder, binarizer, and group finder to process the image.

EuclideanColorDistance is for comparing two colors. It breaks the hex color into red, green, and blue parts, then uses the distance formula to see how far apart the colors are

DistanceImageBinarizer is for turning the real image into a binary image. It like checks every single pixel and compares it to the target color. If the pixel is close enough, it becomes 1. If it is too far away, it becomes 0.

ImageBinarizer is an interface. It tells the program that any binarizer class needs a method to turn a BufferedImage into a 2D array and a method to turn a 2D array back into a BufferedImage

ColorDistanceFinder looks to be also an interface. It tells the program that any color distance class needs a distance method.

BinaryGroupFinder is used after the image becomes 0s and 1s. It finds groups of connected 1s

DfsBinaryGroupFinder uses DFS to find connected groups. It checks up, down, left, and right, but not diagonal pixels its like exactly DFS.

Group stores the size of a group and its center point the center point is the thing that is called the centroid

Coordinate just stores x and y values for a point in the image.

BinarizingImageGroupFinder connects two steps together. First it binarizes the image, then it uses the group finder to find connected groups.