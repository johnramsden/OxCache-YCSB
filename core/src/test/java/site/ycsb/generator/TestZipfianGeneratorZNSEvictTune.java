package site.ycsb.generator;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TestZipfianGeneratorZNSEvictTune {

  @Test
  public void Run() throws IOException {
    final int zone_size = 1077 * 1024 * 1024;

    long six_tib = 6L * 1024 * 1024 * 1024 * 1024;

    int sz_64k = (int) Math.pow(2, 16);
    int sz_256m = (int) Math.pow(2, 28);
    int sz_1077m = 1024*1024*1077;

    int chunks_in_64K = (zone_size / sz_64k);
    int chunks_in_256M = (zone_size / sz_256m);
    int chunks_in_1077M = 1;
    final int workingSetRatio = 10;
    distributionType distributionType = site.ycsb.generator.distributionType.UNIFORM;
    final String directory = "./target/workloads/";

    // H, L
    final int[][] promo_spread = {
        {1, 2},
        {1, 5},
        {1, 9},
        {4, 5},
        {4, 8},
        {4, 12},
        {8, 9},
        {8, 12},
        {8, 16}
    };

    // H, L, C
    final int[][] chunk_spread = {
        {1, 5, 1},
        {1, 5, 2},
        {1, 5, 3},
        {1, 9, 2},
        {1, 9, 4},
        {1, 9, 6},
        {4, 8, 1},
        {4, 8, 2},
        {4, 8, 3},
        {4, 12, 2},
        {4, 12, 4},
        {4, 12, 6},
        {8, 12, 1},
        {8, 12, 2},
        {8, 12, 3},
        {8, 16, 2},
        {8, 16, 4},
        {8, 16, 6}
    };

    // Build promotional workloads
    List<Workload> promoList = new ArrayList<>();
    for (int[] p : promo_spread) {
      int H = p[0] + 1, L = p[1] + 1;  // Add one due to reserved zone
      // Small (64KiB)
      promoList.add(new Workload(sz_64k, 40632, (int) (six_tib / sz_64k), H, L,
          Optional.empty(), "promotional", 904));
      // Medium (256MiB)
      promoList.add(new Workload(sz_256m, 3209583, (int) (six_tib / sz_256m), H, L,
          Optional.empty(), "promotional", 904));
      // Full zone (1077MiB)
      promoList.add(new Workload(sz_1077m, 11524248, (int) (six_tib / sz_1077m), H, L,
          Optional.empty(), "promotional", 904));
    }

    // Build chunk workloads
    List<Workload> chunkList = new ArrayList<>();
    for (int[] c : chunk_spread) {
      int H = c[0] + 1, L = c[1] + 1, C = c[2];  // Add one due to reserved zone, dont adjust C since based on L-H which is unchanged

      // Small (64KiB)
      chunkList.add(new Workload((int) Math.pow(2, 16), 40632, (int) (six_tib / sz_64k), H * chunks_in_64K, L * chunks_in_64K,
          Optional.of(C * chunks_in_64K), "chunk", 904));
      // Medium (256MiB)
      chunkList.add(new Workload((int) Math.pow(2, 28), 3209583, (int) (six_tib / sz_256m), H * chunks_in_256M, L * chunks_in_256M,
          Optional.of(C * chunks_in_256M), "chunk", 904));
    }

    // Could be same array before but it's nice to keep them separate
    List<Workload> workloads = new ArrayList<>();
    workloads.addAll(promoList);
    workloads.addAll(chunkList);

    for (Workload w : workloads) {
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
