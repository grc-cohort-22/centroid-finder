Create video process main class:

        Class 1 - ExtractVideoData
        - Takes the path of the video
        - Validates it is a video
        - Extract metadata

    {Loop:

        Class 2 - ExtractFrame
        - Take video meta data & path
        - Take the needed frame by 
        - Outputs a bufferedimage

        Class 3 - ProcessFrame
        - Take an a buffered image.
        - Processing every framerate/sec frame, 
        - Output group

        Add to groups array, If video is not done, go back}

        Class 4
        - Accepts array of groups
        - Reads the array, writes to a csv

targetColor: 53020A
threshold: 50

mvn exec:java -Dexec.args="res/testInput/sample.mp4 FFA200 164"

StreamsAPI