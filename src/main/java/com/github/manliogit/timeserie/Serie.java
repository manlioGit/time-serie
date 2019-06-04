package com.github.manliogit.timeserie;

import static com.github.manliogit.timeserie.util.Statistic.mean;
import static com.github.manliogit.timeserie.util.Statistic.sd;

import java.util.ArrayList;
import java.util.List;

import com.github.manliogit.timeserie.smooth.MovingAverage;
import com.github.manliogit.timeserie.smooth.MovingMedian;
import com.github.manliogit.timeserie.smooth.Smooth;

public class Serie {

	private enum DECOMPOSITION {ADDITIVE, MULTIPLICATIVE}
	
	private final List<Double> _serie;
	private final int _order;
	
	private DECOMPOSITION _decomposition;
	private Smooth _smooth;
	
	public Serie(List<Double> serie, int m) {
		_serie = serie;
		_order = m;
		_decomposition = DECOMPOSITION.ADDITIVE;
		_smooth = new MovingAverage(m, serie);
	}
	
	public Serie additive() {
		_decomposition = DECOMPOSITION.ADDITIVE;
		return this;
	}
	
	public Serie multiplicative() {
		_decomposition = DECOMPOSITION.MULTIPLICATIVE;
		return this;
	}

	public Serie smoothWithMedian() {
		_smooth = new MovingMedian(_order, _serie);
		return this;
	}
	
	public Serie smoothWithAverage() {
		_smooth = new MovingAverage(_order, _serie);
		return this;
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
		
		for (int t = 0, i = _order / 2 + _order % 2; 
			     t < _order; 
				 t++,   i =   (i + 1)  % _order ) {
			
			double season = detrend.get(i);
			for(int k = i + _order; k < detrend.size(); k += _order) {
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
		
		for (int i = _order/2,   t = _order/2; 
				 i < _serie.size() - _order/2; 
				 i++,  t = (t + 1) % _order   ) {
			
			residual.add(
					op(_serie.get(i), season.get(t), trend.get(i - _order/2))
				);
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

	public List<Double> anomalies(List<Double> observation) {
		List<Double> residual = residual();
		double min = mean(residual) - 3 * sd(residual);
		double max = mean(residual) + 3 * sd(residual);
		
		List<Double> anomalies = new ArrayList<Double>();
		for (Double o : observation) {
			if(o > max || o < min) {
				anomalies.add(o);
			}
		}
		
		return anomalies;
	}
}
