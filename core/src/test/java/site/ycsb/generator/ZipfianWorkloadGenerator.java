package site.ycsb.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import java.nio.ByteOrder;

import static org.testng.AssertJUnit.assertFalse;

public class ZipfianWorkloadGenerator {
  private Workload workload;
  private distributionType distributionType;
  private int working_set_ratio;
  private int zone_size;
  private int iterations;
  private int num_zones;
  private int total_chunks;
  private String file_name;
  private String directory;

  @Override
  public String toString() {
    return workload.toString() +
        ",distributionType=" + distributionType +
        ",working_set_ratio=" + working_set_ratio +
        ",zone_size=" + zone_size +
        ",iterations=" + iterations +
        ",num_zones=" + num_zones +
        ",total_chunks=" + total_chunks;
  }

  public ZipfianWorkloadGenerator(Workload w, distributionType distributionType, int working_set_ratio, int zone_size,
                                  int num_zones, int iterations, String directory) {
    this.workload = w;
    this.distributionType = distributionType;
    this.working_set_ratio = working_set_ratio;
    this.iterations = iterations;
    this.zone_size = zone_size;
    this.num_zones = num_zones;
    this.total_chunks = (zone_size / w.chunkSize) * num_zones;
    this.file_name = toString();
    this.directory = directory;
  }

  public void generateWorkloadFile() throws IOException {
    System.out.println("Creating " + this.file_name);
    new File(directory).mkdirs();

    File outputBinary = new File(this.directory + this.file_name + ".bin");
    File outputText = new File(this.directory + this.file_name + ".txt");

    try (
        FileOutputStream binaryStream = new FileOutputStream(outputBinary);
        // Use a BufferedWriter for the text file
        java.io.BufferedWriter textWriter = new java.io.BufferedWriter(
            new java.io.FileWriter(outputText))
    ) {
      // Generate the data IDs that will be referenced in the workload
      int total_ids = total_chunks * working_set_ratio;

      // Prepare a ByteBuffer for binary output
      ByteBuffer arr = ByteBuffer.allocate(4 * iterations);
      arr.order(ByteOrder.LITTLE_ENDIAN);

      long min = 0;
      long max = total_ids - 1;

      Random rand = new Random();

      switch (this.distributionType) {
        case ZIPFIAN:
          ZipfianGenerator zipfian = new ZipfianGenerator(min, max);
          for (int i = 0; i < iterations; i++) {
            int rnd = Math.toIntExact(zipfian.nextValue());
            assertFalse(rnd < min);
            assertFalse(rnd > max);

            arr.putInt(rnd);

            // Also write to the text file
            textWriter.write(i + ": " + rnd);
            textWriter.newLine();
          }
          break;

        case SEQUENTIAL:
          for (int i = 0; i < iterations; i++) {
            arr.putInt(i);

            textWriter.write(i + ": " + i);
            textWriter.newLine();
          }
          break;

        case UNIFORM:
          for (int i = 0; i < iterations; i++) {
            int rnd = rand.nextInt(total_ids);
            arr.putInt(rnd);

            textWriter.write(i + ": " + rnd);
            textWriter.newLine();
          }
          break;
      }

      binaryStream.write(arr.array());

    }

    System.out.println("Generated files: " + outputBinary.getAbsolutePath() + ", " + outputText.getAbsolutePath());
  }

}
