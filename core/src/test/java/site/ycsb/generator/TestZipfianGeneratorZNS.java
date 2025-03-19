package site.ycsb.generator;

import org.testng.annotations.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

import static org.testng.AssertJUnit.assertFalse;

public class TestZipfianGeneratorZNS {

  @Test
  public void Run() throws IOException {

    final int zone_size = 1077 * 1024 * 1024;
    final int num_zones = 904;
//    final int zone_size = 1024 * 1024;
//    final int num_zones = 28;
    final int iterations = 100000;

    final String directory = "./target/workloads/";
    final int[][] chunk_sizes = {
        {(int) Math.pow(2, 16), 40430}, // 64KiB, mean us latency
        {(int) Math.pow(2, 29), 5413781}  // 512 MiB, mean us latency
    };

    final int[] workingSetRatios = new int[]{10, 1};

    for (int[] chunk_size : chunk_sizes) {
        for(distributionType distributionType : distributionType.values()) {
          for (int workingSetRatio : workingSetRatios) {
            ZipfianWorkloadGenerator generator = new ZipfianWorkloadGenerator(
                chunk_size[0],
                chunk_size[1],
                distributionType,
                workingSetRatio,
                zone_size,
                num_zones,
                iterations,
                directory);

            generator.generateWorkloadFile();
        }
      }
    }
  }
}
