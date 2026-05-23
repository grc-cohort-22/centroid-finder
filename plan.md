Plan
We plan to add video processing by creating new classes that work with the existing centroid finder classes. We will not rewrite the current image-processing logic. Instead, we will use JCodec to read an mp4 video and turn each video frame into a BufferedImage.

After each frame becomes a BufferedImage, we will send it through the existing centroid finder process. The existing classes will binarize the image, find connected groups, and return a list of Group objects. Then a new class will select the largest group from that list. If a largest group exists, we will get its centroid coordinates. If no group exists, we will use -1,-1.

The final output will be a CSV file. Each row will show the time in seconds from the beginning of the video and the x and y coordinates of the largest centroid at that time.

Architecture Plan
User runs command
|
v
VideoProcessorApp
Reads command-line arguments
|
v
VideoProcessorOptions
Stores input video, output CSV, target color, threshold
|
v
VideoProcessor
Controls the full video processing workflow
|
+----------------------+
| |
v v
VideoFrameReader CsvWriter
Uses JCodec Writes rows to CSV
to read mp4
|
v
BufferedImage frame
|
v
Existing Centroid Finder Classes
DistanceImageBinarizer
BinarizingImageGroupFinder
DfsBinaryGroupFinder
|
v
List<Group>
|
v
LargestGroupSelector
Finds biggest group
|
v
Centroid result
seconds, x, y
|
v
CSV output file

// VideoFrameReader only handles reading video frames. VideoProcessor coordinates the process. LargestGroupSelector only chooses the biggest group. CsvWriter only writes the output file.

## Validation Testing wave 4

We tested different target colors and thresholds to improve salamander tracking.

The salamander was very dark while the background was bright pink, so using a black target color (000000) worked better than orange colors.

We tested threshold values and found that 80 reduced background detection while still detecting the salamander body.

The generated CSV coordinates stayed close together and changed slightly between frames, which matched the salamander’s slow movement in the video.
