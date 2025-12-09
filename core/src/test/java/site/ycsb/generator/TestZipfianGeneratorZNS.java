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

    int sz_64k = (int) Math.pow(2, 16);
    int sz_256m = (int) Math.pow(2, 28);
    int sz_1077m = 1024*1024*1077;

    int chunks_in_64K = (zone_size / sz_64k);
    int chunks_in_256M = (zone_size / sz_256m);
    int chunks_in_1077M = 1;

    int latency_64k = 40632;
    int latency_256m = 3209583;
    int latency_1077m = 5413781;

    // 12TB
    int iterations_64k = 2*98_304_000; // 2*(6000*1024^3)/(64*1024)
    int iterations_256m = 48_000;
    int iterations_1077m = 12_000;

    final String directory = "./target/workloadszoned/";

    // Evict best params
    final Workload[] workloads = {
        // CHUNK EVICT STRUCTURE:
        // new Workload(SIZE, LATENCY, ITERATIONS, (chunks_in_size*H+1), (chunks_in_size*L+1), Optional.of(chunks_in_size*C), "chunk", 904),

        // ZONED EVICT (Promotional)
        // new Workload(SIZE, LATENCY, ITERATIONS, H+1, L+1, Optional.empty(), "promotional", 904),

        // ---------------------- Zoned ----------------------

        // Zoned • 64KiB • Chunk • H=4 L=12 C=4
        new Workload(
            sz_64k,
            latency_64k,
            iterations_64k,
            (chunks_in_64K * 4 + 1),
            (chunks_in_64K * 12 + 1),
            Optional.of(chunks_in_64K * 4),
            "chunk",
            904,
            1024
        ),

        // Zoned • 64KiB • Zone • H=1 L=9
        new Workload(
            sz_64k,
            latency_64k,
            iterations_64k,
            (1 + 1),
            (9 + 1),
            Optional.empty(),
            "promotional",
            904,
            1024
        ),

        // Zoned • 256MiB • Chunk • H=1 L=9 C=6
        new Workload(
            sz_256m,
            latency_256m,
            iterations_256m,
            (chunks_in_256M * 1 + 1),
            (chunks_in_256M * 9 + 1),
            Optional.of(chunks_in_256M * 6),
            "chunk",
            904,
            256
        ),

        // Zoned • 256MiB • Zone • H=8 L=16
        new Workload(
            sz_256m,
            latency_256m,
            iterations_256m,
            (8 + 1),
            (16 + 1),
            Optional.empty(),
            "promotional",
            904,
            256
        ),

        // Zoned • 1077MiB • Zone • H=8 L=12
        new Workload(
            sz_1077m,
            latency_1077m,
            iterations_1077m,
            (8 + 1),
            (12 + 1),
            Optional.empty(),
            "promotional",
            904,
            64
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
