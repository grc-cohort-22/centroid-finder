
package app;

import video.VideoProcessor;
import video.VideoProcessorOptions;
/**
 * Main entry point for the video processor program.
 *
 * Expected command:
 *
 * java -jar videoprocessor.jar inputPath outputCsv targetColor threshold
 */



public class VideoProcessorApp {

    public static void main(String[] args) {

        try {

            // Convert command-line arguments into a VideoProcessorOptions object.
            VideoProcessorOptions options =
                VideoProcessorOptions.fromArgs(args);

             
                // Create the VideoProcessor object.
            VideoProcessor processor = new VideoProcessor(options);

            // Start processing the video.
            processor.process();

            
     } catch (IllegalArgumentException e) {

    System.out.println("Invalid input: " + e.getMessage());

}
    }
}
