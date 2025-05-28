package com.github.rapid.common.util;

public class Aggr {

	double sum;
	long count;
	Double min;
	Double max;
	
	public void addNumber(double num) {
		sum += num;
		count++;
		if(min == null || num < min) {
			min = num;
		}
		if(max == null || num > max) {
			max = num;
		}
	}

	public Double getMin() {
		return min;
	}


	public Double getMax() {
		return max;
	}

	public double getSum() {
		return sum;
	}

	public long getCount() {
		return count;
	}
	
	public double getAvg() {
		return sum / count;
	}
	
}
