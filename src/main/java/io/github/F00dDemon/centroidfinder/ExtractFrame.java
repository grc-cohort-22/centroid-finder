package io.github.F00dDemon.centroidfinder;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

public class ExtractFrame {
    public static BufferedImage extractRGBFrame(String videoPath, int frameNumber) throws Exception {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
                try {
                    grabber.start();
                    grabber.setFrameNumber(frameNumber);

                    Frame frame = grabber.grabImage();
                    if (frame == null) throw new IOException("Unable to grab frame " + frameNumber);
                    BufferedImage image = converter.convert(frame);

                    BufferedImage rgbImage = new BufferedImage(
                            image.getWidth(),
                            image.getHeight(),
                            BufferedImage.TYPE_INT_RGB
                    );

                    rgbImage.getGraphics().drawImage(image, 0, 0, null);
                    return rgbImage;

                } finally {
                    grabber.stop();
                    grabber.release();
                }
            }
        }
    }
}
