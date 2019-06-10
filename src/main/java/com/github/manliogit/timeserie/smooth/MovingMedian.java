package com.github.manliogit.timeserie.smooth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovingMedian implements Smooth{

	private final int _order;
	private final List<Double> _serie;

	public MovingMedian(List<Double> serie, int order) {
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
}
