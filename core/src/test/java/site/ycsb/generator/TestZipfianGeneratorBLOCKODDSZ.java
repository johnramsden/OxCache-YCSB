package site.ycsb.generator;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Optional;

public class TestZipfianGeneratorBLOCKODDSZ {

  @Test
  public void Run() throws IOException {

//    final int zone_size = 1077 * 1024 * 1024;
//    final int num_zones = 100;
    final int zone_size = 1077 * 1024 * 1024;

    int sz_134m = 1024*1024*134;

    int chunks_in_134m = (zone_size / sz_134m);

    int latency_134m = 3209583/2;

    // 6TB
    int iterations_134m = 45_850; // (6000*1024^3)/(64*1024)

    final String directory = "./target/workloadsblockoddsz/";

    // Evict best params
    final Workload[] workloads = {
        // CHUNK EVICT STRUCTURE:
        // new Workload(SIZE, LATENCY, ITERATIONS, (chunks_in_size*H+1), (chunks_in_size*L+1), Optional.of(chunks_in_size*C), "chunk", 904),

        // ZONED EVICT (Promotional)
        // new Workload(SIZE, LATENCY, ITERATIONS, H+1, L+1, Optional.empty(), "promotional", 904),

        // Block • 134MiB • Chunk • H=1 L=9 C=0
        new Workload(
            sz_134m,
            latency_134m,
            iterations_134m,
            (chunks_in_134m * 1 + 1),
            (chunks_in_134m * 9 + 1),
            Optional.of(chunks_in_134m * 0),
            "chunk",
            904
        ),

        // Block • 134MiB • Zone • H=1 L=5
        new Workload(
            sz_134m,
            latency_134m,
            iterations_134m,
            (1 + 1),
            (5 + 1),
            Optional.empty(),
            "promotional",
            904
        ),
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
