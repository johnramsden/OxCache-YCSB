package site.ycsb.generator;

public class Workload {
    public int chunkSize;
    public int latency;
    public int evictThresholdHigh;
    public int evictThresholdLow;
    public String evictionType;

    public String toString() {
        return "chunk_size=" + chunkSize +
               ",latency=" + latency +
               ",evict_high_water=" + evictThresholdHigh +
               ",evict_low_water=" + evictThresholdLow +
               ",eviction_type=" + evictionType;
    }

    Workload(int chunkSize, int latency, int evictThresholdHigh, int evictThresholdLow, String evictionType) {
        this.chunkSize = chunkSize;
        this.latency = latency;
        this.evictThresholdHigh = evictThresholdHigh;
        this.evictThresholdLow = evictThresholdLow;
        this.evictionType = evictionType;
    }
}
