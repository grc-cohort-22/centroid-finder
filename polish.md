# Salamander Movement Tracking 

This project process MP4 videos frame by frame, identifies a target color using threshold, and calculates the centroid position of the largest detected bject.

The application records the object's location over time and exports the result to a CSV file containing the timestap and coordinates for each frame. These results can be used to analyze movement patterns and visualize how the salamander moves throughout the video.


## Technology Used

- Java
- Maven
- JCodec
- Git/GitHub
- CSV File Processing

## Key Features

- Process MP4 videos
- Detect objects based on color matching
- Calculate centroids of detected objects
- Export movement data to CSV files


## Challenges

- Reading video frames using JCodec
- Converting frames into images for processing


## Future Improvements

- Track multiple objects simultaneously
- Display movement paths on a graph
- Download CSV reports through a web interface
- Visualize movement using different colors

## Demo

![Salamander Tracking Demo](demo.gif)