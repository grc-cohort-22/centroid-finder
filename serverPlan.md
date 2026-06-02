

The goal is to build an Express server for the Salamander API. The server will let a user view available videos, start a video processing job, and check the status of that job.

The server will not do centroid finding itself. All video processing, color matching, DFS/group finding, and centroid calculations will stay inside the Java video processor JAR.



The project will have two main parts:

1.server  
   This is the Express server. It handles API requests, job IDs, paths, and job status.

2.processor 
   This is the Java Maven project. It contains the video processor JAR and the centroid finder logic.

When the user asks to process a video, Express will call the Java JAR using child_processor.

Example command:

java -jar videoprocessor.jar inputPath outputCsv targetColor threshold

User / Frontend
        |
        v
Express Server
        |
        +-----------------------------+
        |                             |
        v                             v
GET /api/videos              POST /process/:filename
List video files             Start processing job
                                    |
                                    v
                            Create jobId
                                    |
                                    v
                     Run Java JAR using child_process
                                    |
                                    v
                         Java Video Processor
                                    |
                                    v
                      Existing Centroid Finder Logic
                                    |
                                    v
                         CSV output file created
                                    |
                                    v
GET /process/:jobId/status
Return processing, done, or error


The server will:

Read the filename.
Read targetColor and threshold.
Create a unique jobId.
Build the input video path.
Build the output CSV path.
Start the Java JAR using child_process.
Return the job ID immediately.

Important Design Rule

The Express server will not calculate centroids. It will not search pixels, calculate color distance, run DFS, or find groups.

The server only manages requests and calls the Java JAR. The Java processor does the actual video processing.