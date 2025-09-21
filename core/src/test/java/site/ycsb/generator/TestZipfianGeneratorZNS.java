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
    final int zone_size = 1077 * 1024 * 1024;

    int chunks_in_64K = (int) (zone_size / Math.pow(2, 16));
    int chunks_in_256M = (int) (zone_size / Math.pow(2, 28));
    int chunks_in_1077M = 1;

    // For marks we have
    //    high_water_evict = Number remaining from end, evicts if reaches here
    //    low_water_evict = Evict until below mark
    //    low_water_clean = Clean until below mark (only used with chunk)
    //
    // Use high clean = evict low
    // Use low clean = 1/2 of high clean

    final String directory = "./target/workloads/";
//    final Workload[] workloads = {
//        // Promotional - parameter eval, SIGMETRICS
//        new Workload((int) Math.pow(2, 16), 40632, 2*49152000, 1, 16, Optional.empty(), Optional.empty(), "promotional", 904),
//        new Workload((int) Math.pow(2, 28), 3209583, 2*12_000, 4, 16, Optional.empty(), Optional.empty(), "promotional", 904),
//        new Workload(zone_size, 11524248, 2*3_000, 8, 24, Optional.empty(), Optional.empty(), "promotional", 904),
//        // Chunk - parameter eval, SIGMETRICS
//        new Workload((int) Math.pow(2, 16), 40632, 2*49152000, chunks_in_64K, chunks_in_64K*16, Optional.of(chunks_in_64K), Optional.of(chunks_in_64K / 2), "chunk", 904),
//        new Workload((int) Math.pow(2, 28), 3209583, 2*12_000, 4*chunks_in_256M, chunks_in_256M*16, Optional.of(4*chunks_in_256M), Optional.of(chunks_in_256M * 2), "chunk", 904),
//        new Workload(zone_size, 11524248, 2*3_000, 8*chunks_in_1077M, chunks_in_1077M*24, Optional.of(8*chunks_in_1077M), Optional.of(chunks_in_1077M * 4), "chunk", 904),
//    };

    // Evict best params
    final Workload[] workloads = {
        new Workload((int) Math.pow(2, 16), 40632, 4000000,
             chunks_in_64K*2, chunks_in_64K*3, Optional.of(chunks_in_64K*1),
             "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*2, chunks_in_64K*4, Optional.of(chunks_in_64K*1),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*2, chunks_in_64K*4, Optional.of(chunks_in_64K*2),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*3, chunks_in_64K*4, Optional.of(chunks_in_64K*1),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*3, chunks_in_64K*4, Optional.of(chunks_in_64K*2),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*3, chunks_in_64K*6, Optional.of(chunks_in_64K*1),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*3, chunks_in_64K*6, Optional.of(chunks_in_64K*2),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*3, chunks_in_64K*6, Optional.of(chunks_in_64K*3),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*4, chunks_in_64K*6, Optional.of(chunks_in_64K*1),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*4, chunks_in_64K*6, Optional.of(chunks_in_64K*2),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*4, chunks_in_64K*6, Optional.of(chunks_in_64K*3),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*4, chunks_in_64K*8, Optional.of(chunks_in_64K*1),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*4, chunks_in_64K*8, Optional.of(chunks_in_64K*2),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*4, chunks_in_64K*8, Optional.of(chunks_in_64K*3),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*5, chunks_in_64K*7, Optional.of(chunks_in_64K*1),
                     "chunk", 40),

        new Workload((int) Math.pow(2, 16), 40632, 4000000,
                     chunks_in_64K*5, chunks_in_64K*7, Optional.of(chunks_in_64K*2),
                     "chunk", 40)

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
