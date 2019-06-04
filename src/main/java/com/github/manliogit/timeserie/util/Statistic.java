package com.github.manliogit.timeserie.util;

import static java.util.Arrays.asList;

import java.util.List;

public class Statistic {

	public static Double mean(List<Double> d) {
		return summationOn(new Action() {
			@Override
			public double on(double e) {
				return e;
			}
		}, d);
	}
	
	public static Double mean(Double...d) {
		return mean(asList(d));
	}

	public static Double var(List<Double> d) {
		final double mean = mean(d);
		return summationOn(new Action() {
			@Override
			public double on(double e) {
				return Math.pow(e - mean, 2.);
			}
		}, d);
	}
	
	public static Double var(Double...d) {
		return var(asList(d));
	}
	
	public static Double sd(List<Double> d) {
		return Math.sqrt(var(d));
	}
	
	public static Double sd(Double...d) {
		return sd(asList(d));
	}
	
	private static double summationOn(Action a, List<Double> d) {
		double sum = 0.;
		for (double e : d) {
			sum += a.on(e);
		}
		return sum / d.size();
	}
	
	private interface Action {
		double on(double e);
	}
}
