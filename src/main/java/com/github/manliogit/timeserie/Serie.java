package com.github.manliogit.timeserie;

import static com.github.manliogit.timeserie.util.Statistic.mean;
import static com.github.manliogit.timeserie.util.Statistic.sd;
import static com.github.manliogit.timeserie.util.Statistic.sum;

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
	private boolean _robust;
	
	public Serie(List<Double> serie, int m) {
		_serie = new ArrayList<>(serie);
		_order = m;
		_decomposition = DECOMPOSITION.ADDITIVE;
		_smooth = new MovingAverage(serie, m);
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
		_smooth = new MovingMedian(_serie, _order);
		return this;
	}
	
	public Serie smoothWithAverage() {
		_smooth = new MovingAverage(_serie, _order);
		return this;
	}
	
	public Serie robust() {
		_robust = true;
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
		
		for (int t = 0, i = start(); 
			     t < _order; 
				 t++,   i = (i + 1)  % _order ) {
			
			double season = detrend.get(i);
			for(int k = i + _order; k < detrend.size(); k += _order) {
				season += detrend.get(k);
			}
			seasonality.add( season / (detrend.size() / _order) );
		}

		double adjustment = sum(seasonality) / _order;
		List<Double> adjusted = new ArrayList<>();
		for (Double s : seasonality) {
			adjusted.add( op(s, adjustment) );
		}
		
		return adjusted;
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
	
	public List<Double> anomalies() {
		return anomalies(0, residual());
	}
	
	public boolean isLastObservationAtypical() {
		List<Double> residual = residual();
		return !anomalies(residual.size() - start(), residual).isEmpty();
	}
	
	private List<Double> anomalies(int start, List<Double> residual){
		List<Double> filter = robustness(residual);
		
		double min = mean(filter) - 3 * sd(filter);
		double max = mean(filter) + 3 * sd(filter);
		
		List<Double> anomalies = new ArrayList<Double>();
		for (int i = start; i < residual.size(); i++) {
			double observation = residual.get(i); 
			if(observation > max || observation < min) {
				anomalies.add(observation);
			}
		}
		return anomalies;
	}
	
	private List<Double> robustness(List<Double> residual){
		return _robust 
				? new MovingMedian(residual, 3).trend()
				: residual;
	}
	
	private int start() {
		return _order / 2 + _order % 2;
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
