package com.github.manliogit.timeserie.smooth;

import java.util.ArrayList;
import java.util.List;

public class MovingAverage implements Smooth {

	private final List<Double> _serie;
	private final int _order;
	
	public MovingAverage(int order, List<Double> serie) {
		_serie = serie;
		_order = order;
	}
	
	@Override
	public List<Double> trend() {
		/* O(n) */
//		double sum = 0;
//		List<Double> smoothed = new ArrayList<Double>();
//		for (int i = 0; i < serie.size(); i++) {
//			sum += serie.get(i);
//			if( i >= _window - 1) {
//				smoothed.add(sum / _window);
//				sum -= serie.get(i - _window + 1);
//			}
//		}

		List<Double> smoothed = new ArrayList<Double>();
		for (int i = 0; i < _serie.size() - window() + 1; i++) {
			double sum = 0;
			for(int k = i; k < window() + i; k++) {
				sum += _serie.get(k) / factor(k - i);
			}
			smoothed.add(sum);
		}
		return smoothed;
	}
	
	private int factor(int i) {
		if(isEven(_order) && (i == 0 || i == _order)) {
			return 2 * _order;
		}
		return _order;
	}
	
	private boolean isEven(int i) {
		return i % 2 == 0; 
	}
	
	private int window() {
		return isEven(_order) ? _order + 1 : _order;
	}
}
