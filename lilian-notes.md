## ImageSummaryApp.java:
- this file creates a summary that will write to a cvs file using the command line
- the summary will include the parsed target color (24-bit integer), the image path, and threshold
- parses the target color from hex string to 24-bit integer
- binarizes the input image to find the connected groups by finding the connected white pixels which would be the centroid
- the pixels are connected vertically and horizontally 

## ImageGroupFinder.java:
- an interface that finds the connected groups in an image
- takes in an image and will find the connected groups 
- the groups are sorted in DESC order 

## ImageBinarizer.java:
- an interface that converts between RGB images and binary 
- binary images are black and white 
- the binary image is represented as a 2D array of integers 
- each pixel is either 0 or 1 
- 0 = black
- 1 = white 

## Group.java:
- Group is a record instead of a class 
- implements Comparable
- contiguous pixels in an image is represented as a group
- the centroid of the group is computed by taking the average of the pixel coordinates dimensions and dividing it
- the groups are going to be compared to each other to find the largest 

## EuclideanColorDistance.java:
- returns the Euclidean color distance between two hex RGB colors 
- each color is represented as 24-bit integer (0xRRGGBB)
- the Euclidean color distance is calculated by this formula (sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)) 

## DistanceImageBinary.java:
- this class implements the ImageBinarizer interface
- this file will find the color distance using the threshold and targetColor 
- creates a buffered image 

## DfsBinaryGroupFinder.java
- a class that implements BinaryGroupFinder interface
- this file will take the groups of pixels and sort them 

## Coordinate.java
- a record that represents a location in an image or array 
- For example, (row:4, column:7) corresponds to (x:7, y:4)

## ColorDistance.java:
- an interface that computes the distance between two colors 

## BinaryGroupFinder.java
- this file returns a list of sorted groups 

## BinarizingImageGroupFinder.java:
- this class implements the ImageGroupFinder interface
- this file finds the binarized groups 

## sampleInput:
- image of a whiteboard with 4 orange sticky notes on it

## sampleOutput:
- binarized image of the input image
- the white board is now the color black with the sticky notes being the color white