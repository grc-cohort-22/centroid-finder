
// read and store command-line arguments.

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
        if (args.length != 4) {
            throw new IllegalArgumentException(
                "Usage: java -jar videoProcessor.jar inputPath outputCsv targetColor threshold"
            );
        }

        int targetColor = Integer.parseInt(args[2], 16);
        int threshold = Integer.parseInt(args[3]);

        return new VideoProcessorOptions(args[0], args[1], targetColor, threshold);
    }
}
