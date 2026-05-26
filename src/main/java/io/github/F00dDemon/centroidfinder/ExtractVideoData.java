package io.github.F00dDemon.centroidfinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class ExtractVideoData {
        public int width;
        public int height;
        public double frameRate;
        public int frameCount;

        @Override
        public String toString() {
            return "VideoMetadata{" +
                    "width=" + width +
                    ", height=" + height +
                    ", frameRate=" + frameRate +
                    ", frameCount=" + frameCount;
        }
        public void extractMetadata(String videoPath) throws Exception {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            try {
                grabber.start();

                this.width = grabber.getImageWidth();
                this.height = grabber.getImageHeight();
                this.frameRate = grabber.getFrameRate();
                this.frameCount = grabber.getLengthInFrames();

            } finally {
                grabber.stop();
                grabber.release();
                }
            }
        }

        public int getWidth() {
            return width;
        }

        public long getFrameCount() {
            return frameCount;
        }

        public double getFrameRate() {
            return frameRate;
        }

        public int getHeight() {
            return height;
        }
        
}
