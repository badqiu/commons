package com.github.rapid.common.util;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

public class MysqlPartitionGenerator {

	public String generateDay(Date date) {
		String yyyyMMdd = DateConvertUtil.format(date, "yyyyMMdd");
		String longDate = DateConvertUtil.format(date, "yyyy-MM-dd");
		String sql = "PARTITION p"+yyyyMMdd+" VALUES LESS THAN (to_days('"+longDate+"')),";
		return sql;
	}
	
	@Test
	public void generateByDays() {
		int count = 365 * 10 * 2;
		System.out.println("gen_count:"+count);
		
		Date date = DateConvertUtil.extract(new Date(),"yyyyMM");
		StringBuilder sb = new StringBuilder("(");
		for(int i = 0; i < count; i++) {
			Date newDate = DateUtils.addDays(date, i);
			String sql = generateDay(newDate);
			sb.append(sql).append("\n");
		}
		sb.append("PARTITION p_other    VALUES LESS THAN (MAXVALUE));");
		
		printPartition(sb);
	}
	
	@Test
	public void generateByMonths() {
		int count = 12 * 10 * 4;
		System.out.println("gen_count:"+count);
		
		Date date = DateConvertUtil.extract(new Date(),"yyyyMM");
		StringBuilder sb = new StringBuilder("(");
		for(int i = 0; i < count; i++) {
			Date newDate = DateUtils.addMonths(date, i);
			String sql = generateDay(newDate);
			sb.append(sql).append("\n");
		}
		sb.append("PARTITION p_other    VALUES LESS THAN (MAXVALUE));");
		
		printPartition(sb);
	}

	private void printPartition(StringBuilder sb) {
		String alterTable = "ALTER TABLE test_demo_partition_table; \n";
		String createTable = "CREATE TABLE test_demo_partition_table (\n"
				+ "demo_id bigint(20) NOT NULL ,\n"
				+ "create_time datetime NOT NULL,\n"
				+ "PRIMARY KEY (create_time,demo_id) \n"
				+ ") \n";
		System.out.println(alterTable + createTable + "PARTITION BY RANGE (to_days(create_time))  \n" + sb);
	}
	
	@Test
	public void generateByWeek() {
		int count = 520 + 500;
		System.out.println("gen_count:"+count);
		
		Date date = DateConvertUtil.extract(new Date(),"yyyyww");
		date = DateUtils.addDays(date, 1); //加1天，改为周1
		
		StringBuilder sb = new StringBuilder("(");
		for(int i = 0; i < count; i++) {
			Date newDate = DateUtils.addWeeks(date, i);
			String sql = generateDay(newDate);
			sb.append(sql).append("\n");
		}
		sb.append("PARTITION p_other    VALUES LESS THAN (MAXVALUE));");
		
		printPartition(sb);
	}
}
