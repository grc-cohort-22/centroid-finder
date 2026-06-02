import java.util.List;
import java.util.Optional;

/**
     * Finds and returns the largest Group from a list of groups.
     *
     * Optional<Group> is used because there may not be a largest group.
     * For example, the list could be empty or null.
     */

public class LargestGroupSelector {
    public Optional<Group> selectLargest(List<Group> groups) {

       // If the list does not exist or contains no groups,
        // return an empty Optional instead of crashing.
        if(groups == null || groups.isEmpty()) {
            return Optional.empty();
        } 
        
         //Start by assuming the first group is the largest.
        Group largest = groups.get(0);

        //Loop through every group in the list
        for(Group group: groups){
             // Compare the current group's size with the largest size found so far.
            // If the current group is larger, replace largest.
        if(group.size() > largest.size()) {
            largest = group;
        }
    }

      // Return the largest group wrapped in Optional.of(...)
        // because a valid group was found.
        return Optional.of(largest);
    }

   
}
