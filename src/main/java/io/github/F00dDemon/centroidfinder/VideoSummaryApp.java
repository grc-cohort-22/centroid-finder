package io.github.F00dDemon.centroidfinder;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class VideoSummaryApp {
    public static int width;
    public static int height;
    public static double frameRate;
    public static long frameCount;

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java VideoSummaryApp <input_video> <output_csv> <hex_target_color> <threshold>");
            return;
        }

        String inputVideoPath = args[0];
        String outputCsv = args[1];
        String hexTargetColor = args[2];
        int threshold = 0;
        try {
            threshold = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }

        ExtractVideoData metaData = new ExtractVideoData();
        try {
            metaData.extractMetadata(inputVideoPath);
        } catch (Exception e) {
            System.err.println(e);
            return;
        };
        width = metaData.getWidth();
        height = metaData.getHeight();
        frameRate = metaData.getFrameRate();
        frameCount = metaData.getFrameCount();

        int currentFrame = 0;
        ProcessFrame processFrame = new ProcessFrame();
        Queue<Group> frames = new LinkedList<>();

        while(currentFrame < frameCount){
            try {
                BufferedImage frame = ExtractFrame.extractRGBFrame(inputVideoPath, currentFrame);
                Group frameToAdd = processFrame.ProcessSingleFrame(frame, Integer.parseInt(hexTargetColor, 16), threshold);
                frames.add(frameToAdd);
            } catch (Exception e) {
                System.err.println(e);
            };
            currentFrame += (int)frameRate;
        }

        try (PrintWriter writer = new PrintWriter(outputCsv)) {
            int size = frames.size();
            for(int i = 0; i < size ; i++){
                Group line = frames.poll();
                writer.println(i+","+line.centroid().x()+","+line.centroid().y());
            }
            System.out.println("CSV summary saved in: " + outputCsv);
        } catch (Exception e) {
            System.err.println("Error writing to " + outputCsv);
            e.printStackTrace();
        }

    }
}
