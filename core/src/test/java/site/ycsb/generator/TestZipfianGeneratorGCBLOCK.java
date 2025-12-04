package site.ycsb.generator;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Optional;

public class TestZipfianGeneratorGCBLOCK {

  @Test
  public void Run() throws IOException {

//    final int zone_size = 1077 * 1024 * 1024;
//    final int num_zones = 100;
    final int zone_size = 1077 * 1024 * 1024;

    int sz_256m = (int) Math.pow(2, 28);

    int chunks_in_256M = (zone_size / sz_256m);
    int chunks_in_1077M = 1;

    int latency_256m = 3209583;

    // 6TB
    int iterations_256m = 24_000;

    final String directory = "./target/workloadsgcblock/";

    // Evict best params
    final Workload[] workloads = {
//        // CHUNK EVICT STRUCTURE:
//        // new Workload(SIZE, LATENCY, ITERATIONS, (chunks_in_size*H+1), (chunks_in_size*L+1), Optional.of(chunks_in_size*C), "chunk", 904),
//
//        // ZONED EVICT (Promotional)
//        // new Workload(SIZE, LATENCY, ITERATIONS, H+1, L+1, Optional.empty(), "promotional", 904),
//
//        // Block • 256MiB • Chunk • H=1 L=9 C=0
//        new Workload(
//            sz_256m,
//            latency_256m,
//            iterations_256m,
//            (chunks_in_256M * 1 + 1),
//            (chunks_in_256M * 9 + 1),
//            Optional.of(chunks_in_256M * 0),
//            "chunk",
//            904
//        ),
//
//        // Block • 256MiB • Zone • H=1 L=5
//        new Workload(
//            sz_256m,
//            latency_256m,
//            iterations_256m,
//            (1 + 1),
//            (5 + 1),
//            Optional.empty(),
//            "promotional",
//            904
//        ),
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
