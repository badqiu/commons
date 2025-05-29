package com.github.rapid.common.util;

// Aggr => Stat
public class Stat {

	private double sum;
	private long count;
	private double min = Double.NaN;
	private double max = Double.NaN;

	public void addNumber(Integer num) {
		if(num == null) return;
		addNumber(num.doubleValue());
	}
	
	public void addNumber(int num) {
		addNumber((double)num);
	}
	
	public void addNumber(Long num) {
		if(num == null) return;
		addNumber(num.doubleValue());
	}
	
	public void addNumber(long num) {
		addNumber((double)num);
	}

	public void addNumber(Float num) {
		if(num == null) return;
		addNumber(num.doubleValue());
	}
	
	public void addNumber(float num) {
		addNumber((double)num);
	}
	
	public void addNumber(Double num) {
		if(num == null) return;
		addNumber(num.doubleValue());
	}

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
