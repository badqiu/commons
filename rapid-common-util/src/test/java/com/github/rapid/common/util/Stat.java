package com.github.rapid.common.util;

public class Stat {

	private double sum;
	private long count;
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

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
			return 0.0;
		}
		
		return sum / count;
	}
	
	public boolean empty() {
		return count == 0;
	}
	
    @Override
    public String toString() {
    	if(empty()) {
    		return "";
    	}
    	
        return String.format(
            "%s{count=%d, sum=%f, min=%f, avg=%f, max=%f}",
            this.getClass().getSimpleName(),
            getCount(),
            getSum(),
            getMin(),
            getAvg(),
            getMax());
    }
}
