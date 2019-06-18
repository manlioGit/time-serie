package com.github.manliogit.timeserie.util;

import static com.github.manliogit.timeserie.util.Statistic.mean;
import static com.github.manliogit.timeserie.util.Statistic.sd;
import static com.github.manliogit.timeserie.util.Statistic.sum;
import static com.github.manliogit.timeserie.util.Statistic.var;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StatisticTest {

    @Test
    public void testMean() {
        assertThat(mean(10.,12.,14.,15.,17.,18.,18.,24.), is(16.));
    }
    
    @Test
    public void variance() {
        assertThat(var(9., 2., 4., 5., 7., 3.), closeTo(5.6, 0.1));
    }
    
    @Test
    public void standardDeviation() {
        assertThat(sd(9., 2., 4., 5., 7., 3.), closeTo(2.36, 0.1));
    }
    
    @Test
    public void summation() {
        assertThat(sum(1.,2.,3.,4.,5.), is(15.));
    }
}
