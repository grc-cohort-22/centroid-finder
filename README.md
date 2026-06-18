# Centroid Finder

## What is it?
Centroid finder is a project developed by Jmakho01 and I that focuses on tracking groups of color within a given image or video. It works by awaiting a user input of an image/video, a target color and a numerical threshold that determines whether a pixel within an input is eligible to be tracked. The data is then logged into a CSV which gives the coordinate of the largest collective found within a single frame.

## How can it be used?
Centroid finder is primarily developed in Java and has a .jar file that allows it to run with inputs determined by the user.

1. Clone the repository
2. cd into the repository then into /processor
3. Run mvn package and let it run
4. Under /target there should be a filed called centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar.
5. Feel free to move this into a different directory along with a video to test on.
6. Open a terminal in the path with the .jar and execute the file with this parameter structure:
    ```
    java -jar {JAR_NAME.jar} {VIDEO_INPUT.ext} {PATH_TO_DIRECTORY} {COLOR_IN_HEX} {INTEGER_BETWEEN_0_200}
    ```
    Example:
    ```
    java -jar centroid.jar input.mp4 C:\Users\f00d\test FF0000 128
    ```

