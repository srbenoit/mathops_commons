package dev.mathops.commons.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@code IntValuedMap} class.
 */
final class TestIntValuedMap {

    /**
     * Constructs a new {@code TestIntValuedMap}.
     */
    TestIntValuedMap() {

        // No action
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Test storage, update, and retrieval of values")
    void TestStorageUpdateRetrieval() {

        final IntValuedMap<String> testMap = new IntValuedMap<>();

        final long millis = System.currentTimeMillis();
        final RandomGenerator rnd = new Random(millis);

        final int numKeys = 10000;

        final int[][] histogram = new int[numKeys][2];
        for (int i = 0; i < numKeys; ++i) {
            histogram[i][0] = rnd.nextInt();
            histogram[i][1] = 0;
        }

        final int numUpdates = 2000000;
        final int maxValue = 5000000;

        for (int i = 0; i < numUpdates; ++i) {
            final int index = rnd.nextInt(numKeys);
            final String key = Integer.toString(histogram[index][0]);

            final int currentValue = testMap.get(key, 0);
            assertEquals(histogram[index][1], currentValue, "Map does not return correct value while updating");

            final int newValue = rnd.nextInt(maxValue) + 1;
            testMap.put(key, newValue);
            histogram[index][1] = newValue;
        }

        for (int i = 0; i < numKeys; ++i) {
            final String key = Integer.toString(histogram[i][0]);
            final int mapValue = testMap.get(key, 0);

            if (histogram[i][1] == 0) {
                assertEquals(0, mapValue, "Map value that was never set does not return default value");
            } else {
                assertEquals(histogram[i][1], mapValue, "Map value that was set does not return correct value");
            }
        }
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Test increment, decrement, adjust")
    void TestIncrementDecrementAdjust() {

        final IntValuedMap<String> testMap = new IntValuedMap<>();

        final long millis = System.currentTimeMillis();
        final RandomGenerator rnd = new Random(millis);

        final int numKeys = 10000;
        final int maxValue = 5000000;

        final int[][] histogram = new int[numKeys][2];
        for (int i = 0; i < numKeys; ++i) {
            histogram[i][0] = rnd.nextInt();
            histogram[i][1] = rnd.nextInt(maxValue);

            final String key = Integer.toString(histogram[i][0]);
            testMap.put(key, histogram[i][1]);
        }

        final int numUpdates = 2000000;

        for (int i = 0; i < numUpdates; ++i) {
            final int index = rnd.nextInt(numKeys);
            final int which = rnd.nextInt(3);

            final String key = Integer.toString(histogram[index][0]);

            final int currentValue = testMap.get(key, 0);
            assertEquals(histogram[index][1], currentValue, "Map does not return correct value while updating");

            if (which == 0) {
                final int newValue = testMap.increment(key, 0);
                ++histogram[index][1];
                assertEquals(histogram[index][1], newValue, "New value returned by increment was incorrect");
            } else if (which == 1) {
                final int newValue = testMap.decrement(key, 0);
                --histogram[index][1];
                assertEquals(histogram[index][1], newValue, "New value returned by decrement was incorrect");
            } else {
                final int delta = rnd.nextInt(101) - 50;
                final int newValue = testMap.adjust(key, delta, 0);
                histogram[index][1] += delta;
                assertEquals(histogram[index][1], newValue, "New value returned by adjust was incorrect");
            }
        }

        for (int i = 0; i < numKeys; ++i) {
            final String key = Integer.toString(histogram[i][0]);
            final int mapValue = testMap.get(key, 0);

            assertEquals(histogram[i][1], mapValue, "Map value after adjustments was incorrect");
        }
    }

}
