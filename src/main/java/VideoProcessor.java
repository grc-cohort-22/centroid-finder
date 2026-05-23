import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Optional;

import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

/*
 * This class processes a video and writes centroid data to a CSV file.
 */

public class VideoProcessor {

    private VideoProcessorOptions options;

    /*
     * Constructor
     * Stores the command-line options.
     */
    public VideoProcessor(VideoProcessorOptions options) {
        this.options = options;
    }

    /*
     * Processes the video frame by frame.
     */
    public void process() {

        try {

            // Create the CSV writer.
            CsvWriter writer =
                new CsvWriter(options.getOutputCsvPath());

            // Open the video file.
            FrameGrab grab = FrameGrab.createFrameGrab(
                NIOUtils.readableChannel(
                    new File(options.getInputPath())
                )
            );

            Picture picture;
            int frameNumber = 0;

            ImageBinarizer binarizer = new DistanceImageBinarizer(
            new EuclideanColorDistance(),
            options.getTargetColor(),
            options.getThreshold()
            );

            ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(
                binarizer,
                new DfsBinaryGroupFinder()
            );

            LargestGroupSelector selector = new LargestGroupSelector();

            // Read frames from the video.
            while ((picture = grab.getNativeFrame()) != null
                    && frameNumber < 5) {

                // Convert the frame into BufferedImage.
                BufferedImage image =
                    AWTUtil.toBufferedImage(picture);

                System.out.println(
                    "Processing frame " + frameNumber
                );

                /*
                 * Temporary centroid values.
                 * Later we will replace these with real centroid coordinates.
                 */
                double seconds = frameNumber;

             // List<Group> groups = groupFinder.findConnectedGroups(image);
             BufferedImage smallImage = new BufferedImage(
                image.getWidth() / 10,
                image.getHeight() / 10,
                BufferedImage.TYPE_INT_RGB
            );

            smallImage.getGraphics().drawImage(
                image,
                0,
                0,
                smallImage.getWidth(),
                smallImage.getHeight(),
                null
            );

            List<Group> groups = groupFinder.findConnectedGroups(smallImage);

                Optional<Group> largestGroup = selector.selectLargest(groups);

                int x = -1;
                int y = -1;

                if (largestGroup.isPresent()) {
                    Coordinate centroid = largestGroup.get().centroid();
                    x = centroid.x();
                    y = centroid.y();
                }

                // Write one row into the CSV file.
                writer.writeRow(seconds, x, y);

                frameNumber++;
            }

            // Close the CSV file.
            writer.close();

            System.out.println(
                "CSV file created successfully."
            );

        } catch (Exception e) {

            System.out.println(
                "Error processing video."
            );

            e.printStackTrace();
        }
    }
}
