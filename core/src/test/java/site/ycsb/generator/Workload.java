package site.ycsb.generator;

public class Workload {
    public int chunkSize;
    public int latency;
    public int evictThresholdHigh;
    public int evictThresholdLow;
    public String evictionType;

    public String toString() {
        return "chunkSize=" + chunkSize +
               ",latency=" + latency +
               ",evictThresholdHigh=" + evictThresholdHigh +
               ",evictThresholdLow=" + evictThresholdLow +
               ",evictionType=" + evictionType;
    }

    Workload(int chunkSize, int latency, int evictThresholdHigh, int evictThresholdLow, String evictionType) {
        this.chunkSize = chunkSize;
        this.latency = latency;
        this.evictThresholdHigh = evictThresholdHigh;
        this.evictThresholdLow = evictThresholdLow;
        this.evictionType = evictionType;
    }
}
