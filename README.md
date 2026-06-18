# Centroid Finder

## What is it?
centroid-finder is a project developed by jmakho01 and f00dDemon that focuses on tracking groups of color within a given image or video. It works by awaiting a user input of an image/video, a target color and a numerical threshold that determines whether a pixel within an input is eligible to be tracked. The data is then logged into a CSV which gives the coordinate of the largest collective found within a single frame.

## How can it be used?
centroid-finder is primarily developed in Java and has a .jar file that allows it to run with inputs determined by the user.

1. Clone the repository
2. ```cd``` into the repository then into ```/processor```
3. Run ```mvn package``` and let it run
4. Under ```/target``` there should be a filed called ```centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar```.
5. Feel free to move this into a different directory along with a video to test on.
6. Open a terminal in the path with the .jar and execute the file with this parameter structure:
    ```
    java -jar {PATH_TO_JAR_NAME.jar} {PATH_TO_VIDEO_INPUT.ext} {OUTPUT_DIRECTORY.csv} {COLOR_IN_HEX} {INTEGER_BETWEEN_0_200}
    ```
    Example:
    ```
    java -jar centroid.jar C:/Users/f00d/test/input.mp4 C:/Users/f00d/output.csv FF0000 128
    ```

## Server Folder

The server folder is used as the backend portion of salamander-project, which can be viewed [here](https://github.com/jmakho01/salamander-project). To set up the backend, follow these steps:

1. Clone the repository and navitage to it (if you haven't yet).
3. ```cd``` into the ```/server``` folder.
4. Run ```npm i``` to install the necessary node packages for the project.
5. Create a .env file with the following variables:

- ```VIDEOS_DIR``` - the directory where your videos are stored.
- ```PROCESSOR_JAR``` - the filepath to where your centroidfinder jar file is located.
- ```OUTPUT_DIR``` - the directory where the output csv files will be stored.
- ```VITE_API_URL``` - the URL where Vite will pull the videos from.
- ```PORT``` - the port number where the backend runs from.

   Example:
    ```
    VIDEOS_DIR=../processor/res/testInput
    PROCESSOR_JAR=../processor/target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar
    OUTPUT_DIR=../server/output
    VITE_API_URL=http://localhost:3000/
    PORT=3000
    ```

7. Run ```npm run dev``` to run the server. Make sure that your frontend is running as well!

## Frontend Example

<img width="1545" height="1226" alt="Frontend Preview page" src="https://github.com/user-attachments/assets/3e46a65d-fb09-4ae3-964b-4158b145fa96" />

