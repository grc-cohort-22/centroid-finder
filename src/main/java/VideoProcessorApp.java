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

            // Print values to confirm they were read correctly.
            // System.out.println("Input video: "
            //     + options.getInputPath());

            // System.out.println("Output CSV: "
            //     + options.getOutputCsvPath());

            // System.out.println("Target color: "
            //     + Integer.toHexString(options.getTargetColor()));

            // System.out.println("Threshold: "
            //     + options.getThreshold());

            // System.out.println("Video processor started.");

        } catch (Exception e) {

            // Print error message if arguments are invalid.
            System.out.println(e.getMessage());
        }
    }
}
