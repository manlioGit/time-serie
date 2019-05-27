package com.github.manliogit.timeserie.smooth;

import static com.github.manliogit.stat.util.ListMatcher.closeTo;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class MovingAverageTest {

	@Test
	public void oddMovingAverage() throws Exception {
		List<Double> y = asList(25., 18., 23., 21., 19., 20., 18., 16., 17., 15.);
		List<Double> expectedMA = asList(22., 20.666666666666668, 21., 20., 19., 18., 17., 16.);
		
		assertThat(new MovingAverage(3, y).trend(), closeTo(expectedMA, 0.0001));
	}
	
	@Test
	public void evenMovingAverage() throws Exception {
		List<Double> y = asList(25., 18., 23., 21., 19., 20., 18., 16., 17., 15.);
		List<Double> expectedMA = asList(21., 20.5, 20.125, 18.875, 18., 17.125);
		
		assertThat(new MovingAverage(4, y).trend(), closeTo(expectedMA, 0.0001));
	}
	
	/***
	 * Chance Encounters by C.J.Wild and G.A.F. Seber, Time Series
	 * Part-time Unemployment Rates, Statistics New Zealand
	 */
	@Test
	public void evenReferringExample() {
		List<Double> y = asList(9.9,9.5,8.3,8.7, 9.9,8.8,7.0,7.9, 9.3,7.5,6.9,6.9);
		List<Double> expectedMA = asList(9.1, 9.0125, 8.7625, 8.5, 8.3250, 8.0875, 7.9125, 7.775);
		
		assertThat(new MovingAverage(4, y).trend(), closeTo(expectedMA, 0.0001));
	}
}
