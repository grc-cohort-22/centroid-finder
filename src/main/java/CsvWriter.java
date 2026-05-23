/*
 * This class writes centroid information into a CSV file.
 * Each row in the CSV will look like:
 *
 * seconds,x,y
 * 0.0,100,200
 * 1.0,-1,-1
 */

import java.io.File;
import java.io.PrintWriter;

public class CsvWriter {

    // PrintWriter is used to write text into a file.
    private PrintWriter writer;

    /*
     * Constructor
     * Runs when a new CsvWriter object is created.
     *
     * outputPath is the name/path of the CSV file.
     */
    public CsvWriter(String outputPath) throws Exception {

        // Create a File object for the output CSV file.
        File outputFile = new File(outputPath);

        // Connect PrintWriter to the file so we can write into it.
        writer = new PrintWriter(outputFile);

        // Write the CSV header row.
        writer.println("seconds,x,y");
    }

    /*
     * Writes one row into the CSV file.
     *
     * Example:
     * 0.5,120,300
     */
    public void writeRow(double seconds, int x, int y) {

        // Combine the values into one CSV line separated by commas.
        writer.println(seconds + "," + x + "," + y);
    }

    /*
     * Closes the file after writing is finished.
     * This is important so all data is properly saved.
     */
    public void close() {

        // Close the PrintWriter connection to the file.
        writer.close();
    }
}