package ch.nostromo.tiffanys.dragonborn.testing;

import lombok.Data;

@Data
public class TestMeasurement {

    long min = Long.MAX_VALUE;
    long max = Long.MIN_VALUE;
    long total = 0;

    int runCount = 0;

    String category;
    String title;

    long startMs;
    long duration;

    public TestMeasurement(String category, String title) {
        this.category = category;
        this.title = title;
    }

    public void start() {
        this.startMs = System.currentTimeMillis();
        this.runCount ++;
    }

    public void stop() {
        duration = System.currentTimeMillis() - startMs;

        min = Math.min(min, duration);
        max = Math.max(max, duration);
        total += duration;
    }

    public String getMeasurementDump() {
        return "\"" + category + "\";\"" + title + "\";" + min + ";" + max +";" + (total / runCount) + System.lineSeparator();
    }

}
