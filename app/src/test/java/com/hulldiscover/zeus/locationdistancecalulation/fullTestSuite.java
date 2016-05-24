package com.hulldiscover.zeus.locationdistancecalulation;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Zeus on 24/05/16.
 */
public class FullTestSuite extends TestSuite {
    /*
     * The code below will allow you add test
     * In additional classes
     */
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
            TestManhattanDistanceCalculation.class,
    })

    public class JunitTestSuite {
    }
}
