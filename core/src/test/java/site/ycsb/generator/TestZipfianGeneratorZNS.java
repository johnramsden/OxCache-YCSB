package site.ycsb.generator;

import org.testng.annotations.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.testng.AssertJUnit.assertFalse;

public class TestZipfianGeneratorZNS {

  @Test
  public void Run() throws IOException {

//    final int zone_size = 1077 * 1024 * 1024;
//    final int num_zones = 100;
    // final int zone_size = 1077 * 1024 * 1024; // ZNS
   final int zone_size = 1024 * 1024 * 1024; // SSD

    int chunks_in_64K = (int) (zone_size / Math.pow(2, 16));
    int chunks_in_512M = (int) (zone_size / Math.pow(2, 29));

    final String directory = "./target/workloads/";
    final Workload[] workloads = {
        // Promotional
        new Workload((int) Math.pow(2, 16), 40632, 19660800, 1, 2, null, null, "promotional", 80),
        new Workload((int) Math.pow(2, 29), 5413781, 6_000, 8, 16, null, null, "promotional", 200),
        // Chunk
        new Workload((int) Math.pow(2, 16), 40632, 19660800, chunks_in_64K, chunks_in_64K*2, Optional.of(chunks_in_64K), Optional.of(chunks_in_64K / 2), "chunk", 80),
        new Workload((int) Math.pow(2, 29), 5413781, 6_000, chunks_in_512M*8, chunks_in_512M*16, Optional.of(chunks_in_512M*8), Optional.of(chunks_in_512M*4), "chunk", 200),
    };

    final int[] workingSetRatios = new int[]{10, 2};

    for (Workload w : workloads) {
        for(distributionType distributionType : distributionType.values()) {
          for (int workingSetRatio : workingSetRatios) {
            ZipfianWorkloadGenerator generator = new ZipfianWorkloadGenerator(
                w,
                distributionType,
                workingSetRatio,
                zone_size,
                directory);

            generator.generateWorkloadFile();
        }
      }
    }
  }
}
