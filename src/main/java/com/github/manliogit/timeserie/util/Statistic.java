package com.github.manliogit.timeserie.util;

import static java.util.Arrays.asList;

import java.util.List;

public class Statistic {

    public static double mean(List<Double> d) {
        return sum(d) / d.size();
    }
    
    public static double mean(Double...d) {
        return mean(asList(d));
    }

    public static double var(List<Double> d) {
        double mean = mean(d);
        double sum = 0.;
        for (double e : d) {
            sum += Math.pow(e - mean, 2.);
        }
        return sum / d.size();
    }
    
    public static double var(Double...d) {
        return var(asList(d));
    }
    
    public static double sd(List<Double> d) {
        return Math.sqrt(var(d));
    }
    
    public static double sd(Double...d) {
        return sd(asList(d));
    }
    
    public static double sum(List<Double> d) {
        double sum = 0.;
        for (double e : d) {
            sum += e;
        }
        
        return sum;
    }
    
    public static double sum(Double...d) {
        return sum(asList(d));
    }
}
