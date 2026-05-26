package io.github.F00dDemon.centroidfinder;

import java.awt.image.BufferedImage;
import java.util.List;

public class ProcessFrame {
    public Group ProcessSingleFrame(BufferedImage frame, int targetColor, int threshold) {
        try {
            ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
            ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
        
            ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
        
            List<Group> groups = groupFinder.findConnectedGroups(frame);

            if (groups == null || groups.isEmpty()) return new Group(0, new Coordinate(-1, -1)); 

            return groups.get(0);
        } 
        catch (Exception e) { return new Group(0, new Coordinate(-1, -1)); }
    }
}
