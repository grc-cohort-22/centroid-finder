package io.github.F00dDemon.centroidfinder;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ProcessFrameTest {
    private final ProcessFrame processor = new ProcessFrame();

    @Test
    void shouldDetectLargestRedGroup() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 100, 100);

        // small group
        g.setColor(Color.RED);
        g.fillRect(5, 5, 5, 5);

        // large group
        g.fillRect(40, 40, 20, 20);

        g.dispose();

        Group group = processor.ProcessSingleFrame(image, Color.RED.getRGB(), 10);

        assertNotNull(group);

        assertTrue(group.size() > 300);
        assertEquals(49, group.centroid().x(), 2);
        assertEquals(49, group.centroid().y(), 2);
    }

    @Test
    void shouldReturnDefaultGroupWhenNoMatchExists() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        // Entire image black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 50, 50);

        g.dispose();

        Group group = processor.ProcessSingleFrame(image, Color.RED.getRGB(), 5);

        assertEquals(0, group.size());
        assertEquals(-1, group.centroid().x());
        assertEquals(-1, group.centroid().y());
    }

    @Test
    void shouldRespectThresholdMatching() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        Color nearRed = new Color(250, 5, 5);

        g.setColor(nearRed);
        g.fillRect(10, 10, 20, 20);

        g.dispose();

        Group group = processor.ProcessSingleFrame(image, Color.RED.getRGB(), 20);

        assertTrue(group.size() > 0);
    }

    @Test
    void shouldRejectColorOutsideThreshold() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 50, 50);

        // Gray should not match white with low threshold
        g.setColor(new Color(100, 100, 100));
        g.fillRect(10, 10, 20, 20);

        g.dispose();

        Group group = processor.ProcessSingleFrame(image, Color.RED.getRGB(), 5);

        assertEquals(0, group.size());
    }

    @Test
    void shouldUseFourDirectionalConnectivity() {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        image.setRGB(1, 1, Color.RED.getRGB());
        image.setRGB(2, 2, Color.RED.getRGB());

        Group group = processor.ProcessSingleFrame(image, Color.RED.getRGB(), 5);

        // diagonal pixels should remain separate
        assertEquals(1, group.size());

        assertTrue(
            (group.centroid().x() == 1 && group.centroid().y() == 1) ||
            (group.centroid().x() == 2 && group.centroid().y() == 2)
        );
    }

    @Test
    void shouldHandleSinglePixelGroup() {
        BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);

        image.setRGB(5, 5, Color.RED.getRGB());

        Group group = processor.ProcessSingleFrame(image, Color.RED.getRGB(), 5);

        assertEquals(1, group.size());
        assertEquals(5, group.centroid().x());
        assertEquals(5, group.centroid().y());
    }

    @Test
    void shouldReturnDefaultGroupForNullImage() {
        Group group = processor.ProcessSingleFrame(null, Color.RED.getRGB(), 10);

        assertEquals(0, group.size());
        assertEquals(-1, group.centroid().x());
        assertEquals(-1, group.centroid().y());
    }

    @Test
    void shouldProcessExtractedVideoFrame() throws Exception {
        BufferedImage frame = ExtractFrame.extractRGBFrame("res/testInput/sample.mp4", 5);

        assertNotNull(frame);

        Group group = processor.ProcessSingleFrame(frame, Color.RED.getRGB(), 50);

        assertNotNull(group);

        // Depends on video contents
        assertTrue(group.size() >= 0);
    }

    @Test
    void shouldChooseLargestConnectedComponent() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        g.setColor(Color.RED);

        // group 1: 25 pixels
        g.fillRect(5, 5, 5, 5);

        // group 2: 400 pixels
        g.fillRect(40, 40, 20, 20);

        g.dispose();

        Group group = processor.ProcessSingleFrame(image, Color.RED.getRGB(), 5);

        assertTrue(group.size() > 300);

        assertEquals(49, group.centroid().x(), 2);
        assertEquals(49, group.centroid().y(), 2);
    }
}
