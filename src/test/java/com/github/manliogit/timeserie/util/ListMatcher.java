package com.github.manliogit.timeserie.util;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class ListMatcher extends BaseMatcher<List<Double>> {

	private List<Double> _list;
	private double _delta;

	public ListMatcher(List<Double> list, double delta) {
		_list = list;
		_delta = delta;
	}
	
	@Override
	public boolean matches(Object other) {
		if(_list.size() == ((List<Double>)other).size()) {
			for (int i = 0; i < _list.size(); i++) {
				if(Math.abs(_list.get(i) - ((List<Double>)other).get(i)) > _delta) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText(_list.toString());
	}
	
	public static BaseMatcher<List<Double>> closeTo(List<Double> list, double delta){
		return new ListMatcher(list, delta);
	}
}
