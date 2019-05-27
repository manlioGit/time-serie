package com.github.manliogit.timeserie.smooth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MovingMedian implements Smooth{

	private int _order;
	private List<Double> _serie;

	public MovingMedian(int order) {
		_order = order;
	}
	
	public MovingMedian(int order, Double...serie) {
		this(order, Arrays.asList(serie));
	}
	
	public MovingMedian(int order, List<Double> serie) {
		_serie = serie;
		_order = order;
	}
	
	@Override
	public List<Double> trend() {
		
		List<Double> smoothed = new ArrayList<Double>();
		List<Double> centered = new ArrayList<Double>();
		for (int i = 0; i < _serie.size() - _order + 1; i++) {
			List<Double> subList = new ArrayList<Double>();
			for(int k = i; k < _order + i; k++) {
				subList.add(_serie.get(k));
			}
			Collections.sort(subList);
			double d = ( subList.get((_order  - 1)/ 2 ) + subList.get(_order / 2) ) / 2;
			smoothed.add(d);
			if(i > 0) {
				centered.add((smoothed.get(i - 1) + smoothed.get(i) ) / 2 );
			}
		}
		return _order % 2 == 0 ? centered : smoothed;
	}

//	public List<Double> seasonal(List<Double> y) {
//		trend(y);
//		return null;
//	}
//
//	public List<Double> detrend(List<Double> y) {
//		List<Double> trend = trend(y);
//		List<Double> detrend = new ArrayList<Double>();
//		for (int i = _order/2; i < trend.size(); i++) {
//			detrend.add(y.get(i) / trend.get(i - _order/2));    //consider additionalty
//		}
//		
//		return detrend;
//	}
}
