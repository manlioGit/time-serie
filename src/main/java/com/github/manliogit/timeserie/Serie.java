package com.github.manliogit.timeserie;

import java.util.ArrayList;
import java.util.List;

import com.github.manliogit.timeserie.smooth.MovingAverage;
import com.github.manliogit.timeserie.smooth.Smooth;

public class Serie {

	public enum DECOMPOSITION {ADDITIVE, MULTIPLICATIVE}
	
	private final List<Double> _serie;

	private int _order;
	private DECOMPOSITION _decomposition;
	private Smooth _smooth;
	
	public Serie(List<Double> serie, int m) {
		this(serie, m, DECOMPOSITION.ADDITIVE);
	}
	
	public Serie(List<Double> serie, int m, DECOMPOSITION decomposition) {
		_serie = serie;
		_order = m;
		_decomposition = decomposition;
		_smooth = new MovingAverage(m, serie);
	}

	public List<Double> trend() {
		return _smooth.trend();
	}
	
	public List<Double> detrend() {
		List<Double> trend = trend();
		List<Double> detrend = new ArrayList<Double>();
		for (int i = _order/2; i < _serie.size() - _order/2; i++) {
			detrend.add(op(_serie.get(i), trend.get(i - _order/2)));
		}
		
		return detrend;
	}
	
	public List<Double> season() {
		List<Double> detrend = detrend();
		List<Double> seasonality = new ArrayList<Double>();
		for (int i = _order/2, t = 0; t < _order ; i = (i + 1) % _order, t++) {
			
			double season = detrend.get(i);
			for(int k = i + _order; k < detrend.size(); k+=_order) {
				season += detrend.get(k);
			}
			seasonality.add(season / (detrend.size() / _order));
		}

		return seasonality;
	}

	public List<Double> residual() {
		List<Double> residual = new ArrayList<Double>();
		List<Double> trend = trend();
		List<Double> season = season();
		
		for (int i = _order/2, t = _order/2; i < _serie.size() - _order/2; i++, t = (t + 1) % _order) {
			residual.add(op(_serie.get(i), season.get(t), trend.get(i - _order/2)));
		}
		
		return residual;
	}
	
	private double op(double a, double b) {
		return _decomposition == DECOMPOSITION.ADDITIVE
				? a - b
				: a / b;		
	}
	
	private double op(double a, double b, double c) {
		return _decomposition == DECOMPOSITION.ADDITIVE
				? a -  b - c
				: a / (b * c);		
	}
}
