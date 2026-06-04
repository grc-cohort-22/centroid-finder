import static org.junit.jupiter.api.Assertions.*;

import video.VideoProcessorOptions;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class VideoProcessorOptionsTest {

  @Test
  public void fromArgsStoresValuesCorrectly() throws IOException {
    File tempInput = File.createTempFile("test-video", ".mp4");

    String[] args = {
        tempInput.getAbsolutePath(),
        "sampleOutput/output.csv",
        "FFA200",
        "164"
    };

    VideoProcessorOptions options = VideoProcessorOptions.fromArgs(args);

    assertEquals(tempInput.getAbsolutePath(), options.getInputPath());
    assertEquals("sampleOutput/output.csv", options.getOutputCsvPath());
    assertEquals(0xFFA200, options.getTargetColor());
    assertEquals(164, options.getThreshold());

    tempInput.delete();
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
  public void fromArgsParsesHexColorCorrectly() throws IOException {
    File tempInput = File.createTempFile("test-video", ".mp4");

    String[] args = {
        tempInput.getAbsolutePath(),
        "output.csv",
        "000000",
        "100"
    };

    VideoProcessorOptions options = VideoProcessorOptions.fromArgs(args);

    assertEquals(0x000000, options.getTargetColor());

    tempInput.delete();
  }

  @Test
  public void fromArgsParsesThresholdCorrectly() throws IOException {
    File tempInput = File.createTempFile("test-video", ".mp4");

    String[] args = {
        tempInput.getAbsolutePath(),
        "output.csv",
        "FFFFFF",
        "200"
    };

    VideoProcessorOptions options = VideoProcessorOptions.fromArgs(args);

    assertEquals(200, options.getThreshold());

    tempInput.delete();
  }

  @Test
  public void fromArgsThrowsWhenColorIsNotHex() throws IOException {
    File tempInput = File.createTempFile("test-video", ".mp4");

    String[] args = {
        tempInput.getAbsolutePath(),
        "output.csv",
        "ZZZZZZ",
        "100"
    };

    assertThrows(IllegalArgumentException.class, () -> {
      VideoProcessorOptions.fromArgs(args);
    });

    tempInput.delete();
  }

  @Test
  public void fromArgsThrowsWhenThresholdIsNotNumber() throws IOException {
    File tempInput = File.createTempFile("test-video", ".mp4");

    String[] args = {
        tempInput.getAbsolutePath(),
        "output.csv",
        "FFA200",
        "abc"
    };

    assertThrows(IllegalArgumentException.class, () -> {
      VideoProcessorOptions.fromArgs(args);
    });

    tempInput.delete();
  }

  @Test
  public void fromArgsThrowsWhenInputFileDoesNotExist() {
    String[] args = {
        "missing-video.mp4",
        "output.csv",
        "FFA200",
        "100"
    };

    assertThrows(IllegalArgumentException.class, () -> {
      VideoProcessorOptions.fromArgs(args);
    });
  }

  @Test
  public void fromArgsThrowsWhenThresholdIsNegative() throws IOException {
    File tempInput = File.createTempFile("test-video", ".mp4");

    String[] args = {
        tempInput.getAbsolutePath(),
        "output.csv",
        "FFA200",
        "-1"
    };

    assertThrows(IllegalArgumentException.class, () -> {
      VideoProcessorOptions.fromArgs(args);
    });

    tempInput.delete();
  }
}