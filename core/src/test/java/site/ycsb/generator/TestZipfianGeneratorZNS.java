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

//    final int zone_size = 1077 * 1024 * 1024;
//    final int num_zones = 904;
    final int zone_size = 1024 * 1024;
    final int num_zones = 28;

    final int iterations = 1500;
    final String directory = "/run/media/dabanya02/0f654c12-e9d1-4070-a15b-a572aea5a579/";
    final int[] chunk_sizes = new int[]{
        (int) Math.pow(2, 16), // 64KiB
        (int) Math.pow(2, 19)  // 512 KiB
    };
//    final int[] chunk_sizes = new int[]{
//        (int) Math.pow(2, 16), // 64KiB
//        (int) Math.pow(2, 29)  // 512 MiB
//    };
    final int[] workingSetRatios = new int[]{10, 1};

    for (int chunk_size : chunk_sizes) {
        for(distributionType distributionType : distributionType.values()) {
          for (int workingSetRatio : workingSetRatios) {
            ZipfianWorkloadGenerator generator = new ZipfianWorkloadGenerator(
                chunk_size,
                distributionType,
                workingSetRatio,
                zone_size,
                num_zones,
                iterations,
                directory,
                true);

            generator.generateWorkloadFile();
        }
      }
    }
  }
}
