# Goals

## Theme

For our final project, we want to improve the centroid finder by making the CSV tracking results easier to view and understand. Right now, the backend can process a salamander video and create a CSV file with the centroid position over time. Our goal is to build on that by displaying the tracking data in a more useful way.

## Easy

Display the generated CSV data in the frontend as a table.

This is small but useful because users can see the seconds, x coordinate, and y coordinate without opening the CSV file manually. The backend already creates the CSV, so this goal connects directly to the existing project.

## Goal

Create a movement graph from the CSV data.

The graph will show how the salamander moves over time using the x and y centroid values. This would make the project more impressive because users can visually understand the movement instead of only reading numbers from a CSV file.

## Stretch

Add CSV download functionality and support processing more than one video with different CSV result files.

This would make the app feel more complete because users could process different videos, view their results, and download the CSV file for each processed video.

## Impossible

Track multiple objects at the same time and show each object with a different color on the graph.

This would be very cool because the app could track more than one salamander or object, but it would be much harder because the current processor mostly focuses on finding the largest centroid. It would require changing the tracking logic to identify and preserve multiple objects across frames.
