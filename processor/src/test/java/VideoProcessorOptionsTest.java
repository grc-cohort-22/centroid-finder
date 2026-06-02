import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class VideoProcessorOptionsTest {

 @Test
 public void fromArgsStoresValuesCorrectly() {
  String[] args = {
    "sampleInput/video.mp4",
    "sampleOutput/output.csv",
    "FFA200",
    "164"
  };

  VideoProcessorOptions options = VideoProcessorOptions.fromArgs(args);

  assertEquals("sampleInput/video.mp4", options.getInputPath());
  assertEquals("sampleOutput/output.csv", options.getOutputCsvPath());
  assertEquals(0xFFA200, options.getTargetColor());
  assertEquals(164, options.getThreshold());
 }

 @Test
 public void fromArgsThrowsExceptionWhenTooFewArgs() {
  String[] args = {
    "sampleInput/video.mp4",
    "sampleOutput/output.csv"
  };

  assertThrows(IllegalArgumentException.class, () -> {
   VideoProcessorOptions.fromArgs(args);
  });
 }

 @Test
 public void fromArgsThrowsExceptionWhenTooManyArgs() {
  String[] args = {
    "sampleInput/video.mp4",
    "sampleOutput/output.csv",
    "FFA200",
    "164",
    "extra"
  };

  assertThrows(IllegalArgumentException.class, () -> {
   VideoProcessorOptions.fromArgs(args);
  });
 }

 @Test
 public void fromArgsParsesHexColorCorrectly() {
  String[] args = {
    "input.mp4",
    "output.csv",
    "000000",
    "100"
  };

  VideoProcessorOptions options = VideoProcessorOptions.fromArgs(args);

  assertEquals(0x000000, options.getTargetColor());
 }

 @Test
 public void fromArgsParsesThresholdCorrectly() {
  String[] args = {
    "input.mp4",
    "output.csv",
    "FFFFFF",
    "200"
  };

  VideoProcessorOptions options = VideoProcessorOptions.fromArgs(args);

  assertEquals(200, options.getThreshold());
 }
}