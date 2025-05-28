package com.github.rapid.common.util;

// Aggr => Stat
public class Aggr {

	private double sum;
	private long count;
	private double min = Double.NaN;
	private double max = Double.NaN;
	
	public void addNumber(double num) {
		
		if(count == 0 || num < min) {
			min = num;
		}
		if(count == 0 || num > max) {
			max = num;
		}
		
		sum += num;
		count++;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double getSum() {
		return sum;
	}

	public long getCount() {
		return count;
	}
	
	public double getAvg() {
		if(count == 0) {
			return Double.NaN;
		}
		
		return sum / count;
	}
	
	
	public void reset() {
		sum = 0;
		count = 0;
		min = Double.NaN;
		max = Double.NaN;		
	}
	
}
