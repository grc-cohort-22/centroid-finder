package io.github.F00dDemon.centroidfinder;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for reading metadata and extracting RGB frames from a video
 * using JavaCV.
 */
public class VideoTest {
    /**
     * Holds extracted video metadata.
     */
    public static class VideoMetadata {
        public int width;
        public int height;
        public double frameRate;
        public long frameCount;
        public long durationMicros;
        public String videoCodec;
        public String format;

        @Override
        public String toString() {
            return "VideoMetadata{" +
                    "width=" + width +
                    ", height=" + height +
                    ", frameRate=" + frameRate +
                    ", frameCount=" + frameCount +
                    ", durationMicros=" + durationMicros +
                    ", videoCodec='" + videoCodec + '\'' +
                    ", format='" + format + '\'' +
                    '}';
        }
    }

    /**
     * Reads metadata from the video file.
     */
    public static VideoMetadata extractMetadata(String videoPath) throws Exception {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            try {
                grabber.start();

                VideoMetadata metadata = new VideoMetadata();
                metadata.width = grabber.getImageWidth();
                metadata.height = grabber.getImageHeight();
                metadata.frameRate = grabber.getFrameRate();
                metadata.frameCount = grabber.getLengthInFrames();
                metadata.durationMicros = grabber.getLengthInTime();
                metadata.videoCodec = avutil.av_get_pix_fmt_name(grabber.getPixelFormat()) != null
                        ? avutil.av_get_pix_fmt_name(grabber.getPixelFormat()).getString()
                        : "unknown";
                metadata.format = grabber.getFormat();

                return metadata;

            } finally {
                grabber.stop();
                grabber.release();
            }
        }
    }

    /**
     * Extracts a frame from the video and converts it to an RGB BufferedImage.
     *
     * @param videoPath Path to the video file
     * @param frameNumber Frame index to extract
     * @return RGB BufferedImage
     */
    public static BufferedImage extractRgbFrame(String videoPath, int frameNumber) throws Exception {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
                try {
                    grabber.start();

                    grabber.setFrameNumber(frameNumber);

                    Frame frame = grabber.grabImage();

                    if (frame == null) {
                        throw new IOException("Unable to grab frame " + frameNumber);
                    }

                    BufferedImage image = converter.convert(frame);

                    // Ensure image is TYPE_INT_RGB
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

    /**
     * Saves a BufferedImage as a PNG file.
     */
    public static void saveImage(BufferedImage image, String outputPath) throws IOException {
        ImageIO.write(image, "png", new File(outputPath));
    }

    /**
     * Example usage.
     */
    public static void main(String[] args) {
        String videoPath = "sample.mp4";

        try {
            // Extract metadata
            VideoMetadata metadata = extractMetadata(videoPath);
            System.out.println(metadata);

            // Extract frame 30
            BufferedImage frame = extractRgbFrame(videoPath, 30);

            // Save frame to disk
            saveImage(frame, "output.png");

            System.out.println("Frame extracted successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
