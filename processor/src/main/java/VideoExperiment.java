
//package main.java;
import java.awt.image.BufferedImage;
import java.io.File;
import org.jcodec.scale.AWTUtil;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;

public class VideoExperiment {
    public static void main(String[] args) {

        String videoPath = "sampleInput/sample.mp4";

        try {
            FrameGrab grab = FrameGrab.createFrameGrab(
                    NIOUtils.readableChannel(new File(videoPath)));

            Picture picture;
            int frameNumber = 0;

            while ((picture = grab.getNativeFrame()) != null && frameNumber < 5) {

                // BufferedImage image =
                // org.jcodec.scale.AWTUtil.toBufferedImage(picture);

                BufferedImage image = AWTUtil.toBufferedImage(picture);

                System.out.println(
                        "Frame " + frameNumber
                                + " width=" + image.getWidth()
                                + " height=" + image.getHeight());

                frameNumber++;
            }

            System.out.println("Finished reading video.");

        } catch (JCodecException e) {
            System.out.println("Video processing error.");
            e.printStackTrace();

        } catch (Exception e) {
            System.out.println("General error.");
            e.printStackTrace();
        }
    }
}
