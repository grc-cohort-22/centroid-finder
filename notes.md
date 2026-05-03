Rough flow of how program runs:
- determine distance between two colors (wave 3)
- take distance to determine if color is 1 or 0 (wave 4)
- binarize image (wave 5)
- find groups of white pixels in image (wave 2)
- write found groups of white pixels into csv file

ImageSummaryApp connections:
- DfsBinaryGroupFinder -> BinaryGroupFinder
- EuclideanColorDistance -> ColorDistanceFinder
- DistanceImageBinarizer -> ImageBinarizer
- BinarizingImageGroupFinder -> ImageGroupFinder

ImageSummaryApp - Has 3 parameters; image path, target color in hex, threshold for binarization.

Implement order:

DfsBinaryGroupFinder -> EuclideanColorDistance -> DistanceImageBinarizer -> BinarizingImageGroupFinder

1. DfsBinaryGroupFinder

- Uses Groups (size of image/array and position of centroid as Coordinate)
- Uses Coordinates (int x and int y representing positions in the image array)
 
2. EuclideanColorDistance

- Each color int in method is represented as a 24-bit hex integer
- Need to extract seperate RGB colors from original color integer