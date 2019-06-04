package com.github.manliogit.timeserie.smooth;

import static com.github.manliogit.timeserie.util.ListMatcher.closeTo;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class MovingMedianTest {

	@Test
	public void oddMovingMedian() {
		List<Double> y = asList(25., 18., 23., 21., 19., 20., 18., 16., 17., 15.);
		List<Double> expectedMM = asList(23., 21., 21., 20., 19., 18., 17., 16.);
		
		assertThat(new MovingMedian(3, y).trend(), closeTo(expectedMM, 0.0001));
	}
	
	@Test
	public void evenMovingMedian() {
		List<Double> y = asList(7.3, 13.8, 11.4, 11.2, 16., 12.2, 10.9, 15.3, 12.7, 13.7, 16., 14.5);
		List<Double> expectedMM = asList(11.95, 12.2, 11.75, 12.725, 13.1, 12.825, 13.85, 14.3);
							    
		assertThat(new MovingMedian(4, y).trend(), closeTo(expectedMM, 0.0001));
	}
}
