package io.github.F00dDemon.centroidfinder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ExtractFrameTest {
    private static String TEST_VIDEO;

    @BeforeAll
    static void setup() {
        File file = new File("res/testInput/sample.mp4");
        TEST_VIDEO = file.getAbsolutePath();

        assertTrue(file.exists(), "Test video file should exist");
    }

    @Test
    void shouldExtractValidFrame() throws Exception {
        BufferedImage image = ExtractFrame.extractRGBFrame(TEST_VIDEO, 10);

        assertNotNull(image);
        assertTrue(image.getWidth() > 0);
        assertTrue(image.getHeight() > 0);
    }

    @Test
    void shouldExtractFirstFrame() throws Exception {
        BufferedImage image = ExtractFrame.extractRGBFrame(TEST_VIDEO, 0);

        assertNotNull(image);
        assertTrue(image.getWidth() > 0);
        assertTrue(image.getHeight() > 0);
    }

    @Test
    void shouldReturnRgbImageType() throws Exception {
        BufferedImage image = ExtractFrame.extractRGBFrame(TEST_VIDEO, 5);

        assertEquals(BufferedImage.TYPE_INT_RGB, image.getType());
    }

    @Test
    void shouldThrowExceptionForInvalidVideoPath() {
        Exception exception = assertThrows(
                Exception.class,
                () -> ExtractFrame.extractRGBFrame("does-not-exist.mp4", 0)
        );

        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForFrameBeyondVideoLength() {
        Exception exception = assertThrows(
                Exception.class,
                () -> ExtractFrame.extractRGBFrame(TEST_VIDEO, 1_000_000)
        );

        assertTrue(
                exception.getMessage().contains("Unable to grab frame"),
                "Expected frame grab failure message"
        );
    }

    @Test
    void shouldThrowExceptionForNegativeFrameNumber() {
        assertThrows(
                Exception.class,
                () -> ExtractFrame.extractRGBFrame(TEST_VIDEO, -1)
        );
    }

    @Test
    void extractedFrameShouldContainPixelData() throws Exception {
        BufferedImage image = ExtractFrame.extractRGBFrame(TEST_VIDEO, 3);

        int rgb = image.getRGB(0, 0);

        // Just verifies image contains readable pixel data
        assertNotEquals(0, rgb);
    }
}