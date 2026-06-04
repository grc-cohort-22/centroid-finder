import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import csv.CsvWriter;

import org.junit.jupiter.api.Test;

public class CsvWriterTest {

 @Test
 public void writeRowCreatesCorrectCsvFile() throws Exception {

  // Create temporary test file
  File tempFile = File.createTempFile("centroid-test", ".csv");

  // Create CsvWriter
  CsvWriter writer = new CsvWriter(tempFile.getAbsolutePath());

  // Write sample rows
  writer.writeRow(0.0, 100, 200);
  writer.writeRow(1.0, -1, -1);

  // Close writer so file saves properly
  writer.close();

  // Read all lines from the CSV file
  List<String> lines = Files.readAllLines(tempFile.toPath());

  // Check header row
  assertEquals("seconds,x,y", lines.get(0));

  // Check first data row
  assertEquals("0.0,100,200", lines.get(1));

  // Check second data row
  assertEquals("1.0,-1,-1", lines.get(2));
 }
}