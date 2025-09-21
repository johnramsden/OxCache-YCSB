package site.ycsb.generator;

import java.util.Optional;

public class Workload {
    public int chunkSize;
    public int latency;
    public int evictThresholdHigh;
    public int evictThresholdLow;
    public Optional<Integer> cleanThresholdLow;
    public int iterations;
    public int zones;
    public String evictionType;

    public String toString() {
        String ret = "chunk_size=" + chunkSize +
               ",latency=" + latency +
               ",evict_high=" + evictThresholdHigh +
               ",evict_low=" + evictThresholdLow +
               ",eviction=" + evictionType +
               ",iterations=" + iterations +
               ",n_zones=" + zones;
        if (cleanThresholdLow.isPresent()) {
            ret += ",clean_low=" + cleanThresholdLow.get();
        }
        return ret;
    }

    Workload(int chunkSize, int latency, int iterations, int evictThresholdHigh, int evictThresholdLow, Optional<Integer> cleanThresholdLow, String evictionType, int zones) {
        this.chunkSize = chunkSize;
        this.latency = latency;
        this.iterations = iterations;
        this.evictThresholdHigh = evictThresholdHigh;
        this.evictThresholdLow = evictThresholdLow;
        this.cleanThresholdLow = cleanThresholdLow;
        this.evictionType = evictionType;
        this.zones = zones;
    }
}
