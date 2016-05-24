package com.hulldiscover.zeus.locationdistancecalulation;

import android.test.AndroidTestCase;
import android.util.Log;

import com.hulldiscover.zeus.locationdistancecalulation.Activity.MainActivity;

import org.junit.Test;

/**
 * Created by Zeus on 24/05/16.
 */
public class TestManhattanDistanceCalculation extends AndroidTestCase{
    public static final String LOG_TAG = TestManhattanDistanceCalculation.class.getSimpleName();

    /*
     * The Test Runner will execute every function in this class
     * That begins with the name "Test",
     * In the order in which they are declared in the class.
     *
     * Each test should have a failure path that using an assert().
     */
    @Test
    public void testCalculation() throws Throwable {
        // init MainActivity, to use ManhattanDistance method
        MainActivity mainActivity = new MainActivity();
        // calculate distance
        float distance = mainActivity.manhattanDistance(3f, 5f, 6f, 8f);
        // check distance is what is expected
        // third argument - (delta) is used for comparison to avoid issues with floating-point rounding
        assertEquals(4f, distance, 0.0002);
        // log calculation is successful
        Log.d(LOG_TAG, "Calculation Successful: " + distance);
    }


}
