package com.github.rapid.common.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.github.rapid.common.util.ThreadUtil;
import com.google.common.collect.Queues;

/**
 * 单条处理的任务变成异步的批量任务
 * 
 * 使用示例: 
 * 
 * 创建:
 * AsyncQueueBufferedConsumer<DemoLog> batchCreateDemoLog = new AsyncQueueBufferedConsumer<DemoLog>("batchCreateDemoLog",(list)-> {
 *		batchCreate(list);
 * }
 * 
 * 单条操作会自动转成上面异步多条写入
 * batchCreateDemoLog.accept(demoLog);
 * 
 * @author badqiu
 */
public class AsyncQueueBufferedConsumer<T> implements Consumer<T>,InitializingBean,AutoCloseable,DisposableBean {
	private static Logger logger = LoggerFactory.getLogger(AsyncQueueBufferedConsumer.class);

	private static final int DEFAULT_BUFFER_TIMEOUT_MILLS = 200;
	
	private BlockingQueue<T> queue = null;
	private int bufferSize = 500;
	private int bufferTimeoutMills = DEFAULT_BUFFER_TIMEOUT_MILLS;

	private String name;
	private Consumer<List<T>> consumer;
	
	private boolean running = false;
	
	private List<T> _bufferList = new ArrayList<T>(bufferSize);
	Thread _thread;
	
	public AsyncQueueBufferedConsumer(String name, Consumer<List<T>> consumer) {
		this(name,consumer,DEFAULT_BUFFER_TIMEOUT_MILLS,true);
	}
	
	public AsyncQueueBufferedConsumer(String name, Consumer<List<T>> consumer,int bufferTimeoutMills,boolean startConsumerTask) {
		setName(name);
		setConsumer(consumer);
		setBufferTimeoutMills(bufferTimeoutMills);
		_bufferList = new ArrayList<T>(this.bufferSize);
		queue = new ArrayBlockingQueue<T>(10000);
		
		if(startConsumerTask) {
			init();
		}
	}

	public BlockingQueue<T> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<T> queue) {
		this.queue = queue;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getBufferTimeoutMills() {
		return bufferTimeoutMills;
	}

	public void setBufferTimeoutMills(int bufferTimeoutMills) {
		this.bufferTimeoutMills = bufferTimeoutMills;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Consumer<List<T>> getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer<List<T>> consumer) {
		this.consumer = consumer;
	}

	public void putIntoQueue(T t) {
		assertRunning();
		_putIntoQueue(t);
	}
	
	private void assertRunning() {
		Assert.state(running,"closed,running is false");
	}

	private void _putIntoQueue(T t) {
		try {
			queue.put(t);
		} catch (InterruptedException e) {
			throw new RuntimeException("put error", e);
		}
	}

	public void putIntoQueue(Collection<T> list) {
		assertRunning();
		
		if(CollectionUtils.isEmpty(list)) return;
		
		list.forEach(item -> {
			_putIntoQueue(item);
		});
	}
	
	@Override
	public void accept(T t) {
		putIntoQueue(t);
	}
	
	public synchronized void startQueueConsumerThread() {
		if(running) return;
		
		running = true;
		
		Runnable task = newRunnable();
		_thread = new Thread(task, getClass().getSimpleName()+"-"+name);
		_thread.start();
	}

	private Runnable newRunnable() {
		Runnable task = new Runnable() {
			public void run() {
				try {
					whileRunningDoConsumeQueue();
				}finally {
					logger.info("thread task exit,name:"+name);
				}
			}
		};
		return task;
	}

	public void whileRunningDoConsumeQueue() {
		String clazzName = getClass().getSimpleName();
		logger.info(clazzName + " queue consumer task started,name:" + name);
		while (running) {
			try {
				doConsumeQueue();
			} catch (InterruptedException e) {
				throw new RuntimeException("queue drain InterruptedException", e);
			} catch(Exception e) {
				logger.error("task error on name:"+name+" error:"+e,e);
				ThreadUtil.sleep(bufferTimeoutMills);
			}
		}
	}

	public void doConsumeQueue() throws InterruptedException {
		try {
			Queues.drain(queue, _bufferList, bufferSize, bufferTimeoutMills, TimeUnit.MILLISECONDS);
			
			consumerAcceptList(_bufferList);
		}finally {
			_bufferList.clear();
		}
	}

	protected void consumerAcceptList(List<T> list) {
		if(CollectionUtils.isEmpty(list)) {
			return;
		}
		
		consumer.accept(list);
	}
	
	public void init() {
		startQueueConsumerThread();
	}
	
	public void close() {
		logger.info("close() name:"+name);
		running = false;
		threadJoin();
		
		List<T> tempBuffer = new ArrayList<T>();
		queue.drainTo(tempBuffer);
		consumerAcceptList(tempBuffer);
	}

	private void threadJoin() {
		if(_thread != null) {
			try {
				_thread.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	@Override
	public void destroy() throws Exception {
		close();
	}

}
