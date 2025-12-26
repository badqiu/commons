package com.github.rapid.common.util;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

import com.github.rapid.common.util.DateConvertUtil;
import com.github.rapid.common.util.DurationUtil;

/**
 * 生成日期范围的工具类
 */
public class DateListGenerator {

	/**
	 * 生成日期列表
	 * 
	 * @param startTime 开始时间，格式：yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss
	 * @param interval  时间间隔，支持：d(天)、h(小时)、m(分钟)、s(秒)
	 * @param num         生成的数量
	 * @return 日期字符串列表
	 */
	public static List<Date> generateDateList(Date startTime, String interval, int num) {
		Assert.isTrue(num>0,"num > 0 must be true");
		Assert.hasText(interval,"interval must be not blank");
		Assert.notNull(startTime,"startTime must be not null");
		
		List<Date> dateList = new ArrayList<>();

		// 解析时间间隔
		Duration timeInterval = parseInterval(interval);
		for (int i = 0; i < num; i++) {
			long mills = i * timeInterval.toMillis();
			long newTime = startTime.getTime() + mills;
			dateList.add(new Date(newTime));
		}

		return dateList;
	}
	
	public static List<Date> generateDateList(Date startTime, Date endTime, String interval) {
		Assert.hasText(interval,"interval must be not blank");
		Assert.notNull(startTime,"startTime must be not null");
		Assert.notNull(endTime,"endTime must be not null");
		
		
		List<Date> dateList = new ArrayList<>();

		// 解析时间间隔
		Duration timeInterval = parseInterval(interval);
		for (long i = 0; i < Long.MAX_VALUE; i++) {
			long mills = i * timeInterval.toMillis();
			long newTime = startTime.getTime() + mills;
			if(newTime > endTime.getTime()) {
				break;
			}
			
			dateList.add(new Date(newTime));
		}

		return dateList;
	}
	
	public static List<Date> generateDateList(Date startTime, Date endTime, String interval,int num) {
		if(endTime != null) {
			return generateDateList(startTime,endTime,interval);
		}
		return generateDateList(startTime, interval, num);
	}

	/**
	 * 解析时间间隔字符串
	 * 
	 * @param interval 间隔字符串，如：1d, 2h, 30m, 60s
	 */
	private static Duration parseInterval(String interval) {
		return DurationUtil.parseDuration(interval);
	}



	/**
	 * 生成日期列表（仅日期）
	 * 
	 * @param startDate    开始日期
	 * @param intervalDays 间隔天数
	 * @param n            生成数量
	 * @return 日期列表
	 */
	public static List<Date> generateDateOnlyList(Date startDate, int intervalDays, int n) {
		return generateDateList(startDate, intervalDays + "d", n);
	}

	/**
	 * 测试方法
	 */
	public static void main(String[] args) {
		System.out.println("=== 日期列表生成工具测试 ===\n");

		Date startDate = DateConvertUtil.parse("2025-10-10", "yyyy-MM-dd");
		// 测试1：生成日期列表（仅日期）
		System.out.println("测试1：生成日期列表（2025-10-10开始，间隔1天，生成10个）");
		List<Date> dates1 = generateDateOnlyList(startDate, 1, 10);
		printDates(dates1);


		System.out.println("\n测试3：生成日期列表（自定义格式，2025-10-10开始，间隔3天，生成30个）");
		List<Date> dates3 = generateDateList(startDate, "3d", 30);
		printDates(dates3);
		System.out.println("... 共生成 " + dates3.size() + " 个日期");

		System.out.println("\n测试4：生成分钟间隔列表（2025-10-10 09:00:00开始，间隔30分钟，生成5个）");
		List<Date> dates4 = generateDateList(startDate, "30m", 5);
		printDates(dates4);

		System.out.println("\n测试5：生成秒间隔列表（2025-10-10 10:00:00开始，间隔10秒，生成5个）");
		List<Date> dates5 = generateDateList(startDate, "10s", 5);
		printDates(dates5);
		
		List<Date> dates6 = generateDateList(startDate,DateUtils.addDays(startDate, 1), "1h");
		printDates(dates6);
		
		Date endDate = DateUtils.addDays(startDate, 10);
		System.out.println(DateConvertUtil.format(endDate, "yyyy-MM-dd HH:mm:ss"));
		List<Date> dates7 = generateDateList(startDate,endDate, "1d");
		printDates(dates7);
	}

	private static void printDates(List<Date> list) {
		System.out.println("\n\nprintDates.size:"+list.size());
		AtomicInteger count = new AtomicInteger(0);
		list.forEach(date -> {
			int i = count.incrementAndGet();
			System.out.println(DateConvertUtil.format(date, "yyyy-MM-dd HH:mm:ss") +" index="+i);
		});
		
	}
}