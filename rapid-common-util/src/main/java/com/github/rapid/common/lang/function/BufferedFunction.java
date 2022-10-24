package com.github.rapid.common.lang.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rapid.common.util.SystemTimer;


public class BufferedFunction <T, R> extends ProxyFunction<T, R> {
	private static Logger logger = LoggerFactory.getLogger(BufferedFunction.class);
	
	
	private int bufferSize = 1000;
	private int bufferTimeout = 500;
	
	private long lastSendTime = System.currentTimeMillis();
	private List<T> bufferList = new ArrayList<T>();
	
	private boolean init = false;
	
	public BufferedFunction() {
		super();
	}

	public BufferedFunction(Function<T, R> proxy) {
		super(proxy);
	}
	
	@Override
	public R apply(T t)  {
		bufferList.add(t);
		
		if(bufferList.size() > bufferSize) {
			flushBuffer();
		}else if(bufferTimeout > 0 && isTimeout()) {
			flushBuffer();
		}
		
		return null;
	}

	private void flushBuffer() {
		if(bufferTimeout > 0) {
			lastSendTime = SystemTimer.currentTimeMillis();
		}
		
		if(bufferList == null || bufferList.isEmpty()) {
			return;
		}
		
		List<T> tempBuf = bufferList;
		bufferList = new ArrayList<T>(bufferSize);
		for(T item : tempBuf) {
			realApply(item);
		}
		
	}

	private boolean isTimeout() {
		return Math.abs(lastSendTime - SystemTimer.currentTimeMillis()) > bufferTimeout;
	}
	
	private R realApply(T t) {
		return super.apply(t);
	}

	
}
