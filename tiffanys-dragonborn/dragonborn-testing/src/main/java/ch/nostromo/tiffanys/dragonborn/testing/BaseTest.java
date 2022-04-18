package ch.nostromo.tiffanys.dragonborn.testing;

import ch.nostromo.tiffanys.commons.logging.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseTest {

    protected Logger logger = Logger.getLogger(getClass().getName());

    protected List<TestMeasurement> measurementList = new ArrayList<>();

    public BaseTest() {
        LogUtils.initializeLogging(Level.INFO, Level.OFF, null, null);
    }

    public TestMeasurement createTestMeasurment(String category, String title) {
        TestMeasurement result = new TestMeasurement(category, title);

        measurementList.add(result);

        return result;
    }

    protected void startMeasurement(TestMeasurement measurement) {
        logger.info("Start measurement for category=" + measurement.getCategory() + ", title=" + measurement.getTitle());
        measurement.start();
    }

    protected void stopMeasurment(TestMeasurement measurement) {
        measurement.stop();
        logger.info("Stop measurement for category=" + measurement.getCategory() + ", title=" + measurement.getTitle() + ", duration= " + measurement.getDuration());
    }


    public String dumpResults() {
        StringBuilder result = new StringBuilder();
        for (TestMeasurement measurement : measurementList) {
            result.append(measurement.getMeasurementDump());
        }

        return result.toString();
    }

}
