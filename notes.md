ImageSummaryApp: have three command-line arguments that are imageinputpath, hex target color and threshold. Threshold decides how close does a pixel color need to be to the target color.

ColorDistanceFinder: Computes the distance between two colors Each color is represented as a 24-bit integer (0xRRGGBB) where the red, green, and blue components can be xtracted using bit shifting and masking. The interface has two parameteres int colorA (the first color as a 24-bit hex RGB integer)and int colorB(the second color as a 24-bit hex RGB integer)

EuclideanColorDistance:The Euclidean color distance is calculated by treating each color as a point, in 3D space (red, green, blue) and applying the Euclidean distance formula: sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2). This gives a measure of how visually different the two colors are.

ImageBinarizer: Converts the given BufferedImage into a binary 2D array or Converts a binary 2D array into a BufferedImage. Each entry in the returned array is either 0 or 1, representing a black or white pixel. Black pixels should be represented as x000000 and white pixels should be represented as xFFFFFF.

DistanceImageBinarizer: Converts the given BufferedImage into a binary 2D array using color distance and a threshold. Each entry in the returned array is either 0 or 1, representing a black or white pixel. Converts a binary 2D array into a BufferedImage.return BufferedImage.

ImageGroupFinder:
BinarizingImageGroupFinder:
DfsBinaryGroupFinder:
Group:
