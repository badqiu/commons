package com.github.rapid.common.util;

public class Aggr {

	double sum;
	long count;
	double min;
	double max;
	
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
	
}
