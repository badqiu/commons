package com.github.rapid.common.util;

import java.net.Inet4Address;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 
 * 雪花算法，用于数据库生成long类型 ID
 *
 *  修改目的：因为js不支持原生雪花算法生成的ID（原生19位长度），js只支持16长度的整型数字
 *  修改内容：将sequenceBits从12L => 4L,  生成的ID长度从19位 => 16位
 *  修改代价：一毫秒可以生成ID数量从 4096个 => 16个
 * 
 * 性能:
 * [totalCost:6,258ms, loopCount:100,000, TPS:15,979] - generateId_perf_test_by_sequenceBits=4
 * [totalCost:2,442ms, loopCount:10,000,000, TPS:4,095,004] - generateId_perf_test_by_sequenceBits=12
 * [totalCost:1,564ms, loopCount:100,000, TPS:63,938] - generateId_perf_test_by_sequenceBits=6
 * */
public class SnowflakeIdGenerator {

	private static Logger logger = LoggerFactory.getLogger(SnowflakeIdGenerator.class);
	
	private final long twepoch = 1288834974657L;
	private final long workerIdBits = 5L;
	private final long datacenterIdBits = 5L;
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	public final long sequenceBits = 4L;
	private final long workerIdShift = sequenceBits;
	private final long datacenterIdShift = sequenceBits + workerIdBits;
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	public final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long workerId;
	private long datacenterId;
	private long sequence = 0L;
	private long lastTimestamp = -1L;
	
	private static SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(getWorkId(),getDataCenterId());

	public SnowflakeIdGenerator(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",maxDatacenterId));
		}
		
		this.workerId = workerId;
		this.datacenterId = datacenterId;
		
		logger.info("SnowflakeIdGenerator:"+ToStringBuilder.reflectionToString(this));
	}

	public synchronized long nextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",lastTimestamp - timestamp));
		}
		
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		long finalTimestamp = timestamp - twepoch;
		return (finalTimestamp << timestampLeftShift)
				| (datacenterId << datacenterIdShift)
				| (workerId << workerIdShift) 
				| sequence;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}
	
	public static long generateId() {
		return snowflakeIdGenerator.nextId();
	}
	
	private static Long getWorkId(){
        try {
//            String hostAddress = getIp();
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();

            int[] ints = StringUtils.toCodePoints(hostAddress);
            long sums = sumInts(ints);
            long workId = (long)(sums % 32);
            logger.info("SnowflakeIdGenerator.hostAddress: "+hostAddress+" for generate workId: "+workId);
            return workId;
        } catch (Exception e) {
        	e.printStackTrace();
            // 如果获取失败，则使用随机数备用
            return RandomUtils.nextLong(0,31);
        }
    }
	
    private static Long getDataCenterId(){
        String hostName = SystemUtils.getHostName();
		int[] ints = StringUtils.toCodePoints(hostName);
        long sums = sumInts(ints);
        long dataCenterId = (long)(sums % 32);
        logger.info("SnowflakeIdGenerator.hostName: "+hostName+" for generate dataCenterId: "+dataCenterId);
        return dataCenterId;
    }

	private static long sumInts(int[] ints) {
		long sums = 0;
        for (int i: ints) {
            sums += i;
        }
		return sums;
	}

	public static void main(String[] args) {
		SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(0, 0);
		for (int i = 0; i < 5; i++) {
			long id = SnowflakeIdGenerator.generateId();
			System.out.println(id);
		}
	}
}