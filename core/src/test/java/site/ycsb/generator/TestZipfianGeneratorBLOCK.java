package site.ycsb.generator;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Optional;

public class TestZipfianGeneratorBLOCK {

  @Test
  public void Run() throws IOException {

//    final int zone_size = 1077 * 1024 * 1024;
//    final int num_zones = 100;
    final int zone_size = 1077 * 1024 * 1024;

    int sz_64k = (int) Math.pow(2, 16);
    int sz_256m = (int) Math.pow(2, 28);
    int sz_1077m = 1024*1024*1077;

    int chunks_in_64K = (zone_size / sz_64k);
    int chunks_in_256M = (zone_size / sz_256m);
    int chunks_in_1077M = 1;

    int latency_64k = 40632;
    int latency_256m = 3209583;
    int latency_1077m = 5413781;

    // 6TB
    int iterations_64k = 98_304_000; // (6000*1024^3)/(64*1024)
    int iterations_256m = 24_000;
    int iterations_1077m = 6_000;

    final String directory = "./target/workloadsblock/";

    // Evict best params
    final Workload[] workloads = {
        // CHUNK EVICT STRUCTURE:
        // new Workload(SIZE, LATENCY, ITERATIONS, (chunks_in_size*H+1), (chunks_in_size*L+1), Optional.of(chunks_in_size*C), "chunk", 904),

        // ZONED EVICT (Promotional)
        // new Workload(SIZE, LATENCY, ITERATIONS, H+1, L+1, Optional.empty(), "promotional", 904),

//        // Block • 64KiB • Chunk • H=1 L=5 C=0
//        new Workload(
//            sz_64k,
//            latency_64k,
//            iterations_64k,
//            (chunks_in_64K * 1 + 1),
//            (chunks_in_64K * 5 + 1),
//            Optional.of(chunks_in_64K * 0),
//            "chunk",
//            904
//        ),
//
//        // Block • 64KiB • Zone • H=4 L=8
//        new Workload(
//            sz_64k,
//            latency_64k,
//            iterations_64k,
//            (4 + 1),
//            (8 + 1),
//            Optional.empty(),
//            "promotional",
//            904
//        ),
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
//
//        // Block • 1077MiB • Zone • H=1 L=2
//        new Workload(
//            sz_1077m,
//            latency_1077m,
//            iterations_1077m,
//            (1 + 1),
//            (2 + 1),
//            Optional.empty(),
//            "promotional",
//            904
//        )
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
