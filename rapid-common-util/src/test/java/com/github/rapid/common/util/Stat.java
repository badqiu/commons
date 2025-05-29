package com.github.rapid.common.util;

import java.util.function.DoubleConsumer;

public class Stat implements DoubleConsumer{

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
    		return "Stat{}";
    	}
    	
    	String format = "Stat{count=%d, sum=%.4f, min=%.4f, avg=%.4f, max=%.4f}";
    	double avg = getAvg();
    	if(avg > 1000) {
    		format = "Stat{count=%d, sum=%.0f, min=%.0f, avg=%.0f, max=%.0f}";
    	}else if(avg > 100) {
    		format = "Stat{count=%d, sum=%.1f, min=%.1f, avg=%.1f, max=%.1f}";
    	}else if(avg > 10) {
    		format = "Stat{count=%d, sum=%.2f, min=%.2f, avg=%.2f, max=%.2f}";
    	}else if(avg > 1) {
    		format = "Stat{count=%d, sum=%.3f, min=%.3f, avg=%.3f, max=%.3f}";
    	}
    	
        return String.format(format,
            getCount(),
            getSum(),
            getMin(),
            avg,
            getMax());
    }

	@Override
	public void accept(double value) {
		addNumber(value);
	}
}
