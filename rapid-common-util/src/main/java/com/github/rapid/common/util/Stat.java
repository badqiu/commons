package com.github.rapid.common.util;

import java.util.function.DoubleConsumer;

/**
 * 统计工具类，可以方便的统计:min,max,count,sum,avg等值。
 */
public class Stat implements DoubleConsumer{

	public long startTime = 0;
	
	private double sum;
	private long count;
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

    public Stat() {
    }
    
    public Stat(boolean useStartTime) {
    	if(useStartTime) {
    		startTime = System.currentTimeMillis();
    	}
    }
    
    /**
     * 计数+1
     * @return
     */
    public Stat increment() {
    	addNumber(1);
    	return this;
    }
    
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
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	public void clean() {
		this.startTime = System.currentTimeMillis();
		this.count = 0;
		this.sum = 0;
	    min = Double.POSITIVE_INFINITY;
	    max = Double.NEGATIVE_INFINITY;
	}

	/**
	 * 统计每毫秒的平均值
	 * @return
	 */
	public double getAvgNumberPerMills() {
		if(count == 0) {
			return 0.0;
		}
		double intervalMills = (System.currentTimeMillis() - startTime);
		return sum / intervalMills;
	}
	
	
	/**
	 * 统计每秒的平均值
	 * @return
	 */
	public double getAvgNumberPerSecond() {
		return getAvgNumberPerMills() * 1000;
	}
	
	/**
	 * 统计每分钟的平均值
	 * @return
	 */
	public double getAvgNumberPerMinute() {
		return getAvgNumberPerSecond() * 60;
	}
	
	/**
	 * 统计每小时的平均值
	 * @return
	 */
	public double getAvgNumberPerHour() {
		return getAvgNumberPerMinute() * 60;
	}

	/**
	 * 统计每天的平均值
	 * @return
	 */
	public double getAvgNumberPerDay() {
		return getAvgNumberPerHour() * 24;
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
