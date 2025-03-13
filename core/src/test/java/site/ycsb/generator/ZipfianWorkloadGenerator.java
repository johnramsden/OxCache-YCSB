package site.ycsb.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import static org.testng.AssertJUnit.assertFalse;

public class ZipfianWorkloadGenerator {
  private int chunk_size;
  private distributionType distributionType;
  private int working_set_ratio;
  private int zone_size;
  private int iterations;
  private int num_zones;
  private int total_chunks;
  private String file_name;
  private String directory;
  private boolean isBinaryFile;

  @Override
  public String toString() {
    return "chunk_size=" + chunk_size +
        ",distributionType=" + distributionType +
        ",working_set_ratio=" + working_set_ratio +
        ",zone_size=" + zone_size +
        ",iterations=" + iterations +
        ",num_zones=" + num_zones +
        ",total_chunks=" + total_chunks;
  }

  public ZipfianWorkloadGenerator(int chunk_size, distributionType distributionType, int working_set_ratio, int zone_size,
                          int num_zones, int iterations, String directory, boolean isBinaryFile) {
    this.chunk_size = chunk_size;
    this.distributionType = distributionType;
    this.working_set_ratio = working_set_ratio;
    this.iterations = iterations;
    this.zone_size = zone_size;
    this.num_zones = num_zones;
    this.total_chunks = (zone_size / chunk_size) * num_zones;
    this.file_name = toString();
    this.directory = directory;
    this.isBinaryFile = isBinaryFile;
  }

  public void generateWorkloadFile() throws IOException {
    System.out.println("Creating " + this.file_name);
    File output = new File(this.directory + this.file_name);
    FileOutputStream stream = new FileOutputStream(output);

    // Generate the data IDs that will be referenced in the workload
    int total_ids = total_chunks * working_set_ratio;
    int[] db = new int[total_ids];
    Random rand = new Random();
    for (int i = 0;i < total_ids; i++) {
      db[i] = rand.nextInt();
    }

    // Generate the workload
    ByteBuffer arr = ByteBuffer.allocate(4 * iterations);
    long min = 0;
    long max = total_ids - 1;

    switch (this.distributionType) {
      case ZIPFIAN:
        ZipfianGenerator zipfian = new ZipfianGenerator(min, max);
        for (int i = 0; i < iterations; i++) {
          long rnd = zipfian.nextValue();
          assertFalse(rnd < min);
          assertFalse(rnd > max);
          arr.putInt(db[(int) rnd]);
        }
        break;
      case SEQUENTIAL:
        for (int i = 0; i < iterations; i++) {
          long rnd = i % total_ids;
          arr.putInt(db[(int) rnd]);
        }
        break;
      case UNIFORM:
        for (int i = 0; i < iterations; i++) {
          long rnd = rand.nextInt(total_ids);
          arr.putInt(db[(int) rnd]);
        }
        break;
    }


    stream.write(arr.array());
    stream.close();
  }
}
