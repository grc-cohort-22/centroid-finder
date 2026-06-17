package video;
// read and store command-line arguments.
import java.io.File;

public class VideoProcessorOptions {
    private final String inputPath;
    private final String outputCsvPath;
    private final int targetColor;
    private final int threshold;

    public VideoProcessorOptions(String inputPath, String outputCsvPath, int targetColor, int threshold) {
        this.inputPath = inputPath;
        this.outputCsvPath = outputCsvPath;
        this.targetColor = targetColor;
        this.threshold = threshold;
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputCsvPath() {
        return outputCsvPath;
    }

    public int getTargetColor() {
        return targetColor;
    }

    public int getThreshold() {
        return threshold;
    }

    public static VideoProcessorOptions fromArgs(String[] args) {
    if (args == null || args.length != 4) {
        throw new IllegalArgumentException(
            "Usage: java -jar videoProcessor.jar inputPath outputCsv targetColor threshold"
        );
    }

    File inputFile = new File(args[0]);

    if (!inputFile.exists()) {
        throw new IllegalArgumentException(
            "Input file does not exist."
        );
    }

    int targetColor;

    try {
        targetColor = Integer.parseInt(args[2], 16);
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException(
            "Target color must be a valid hex value."
        );
    }

    int threshold;

    try {
        threshold = Integer.parseInt(args[3]);
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException(
            "Threshold must be a valid integer."
        );
    }

    if (threshold < 0) {
        throw new IllegalArgumentException(
            "Threshold must not be negative."
        );
    }

    return new VideoProcessorOptions(
        args[0],
        args[1],
        targetColor,
        threshold
    );
}
}
