package com.github.rapid.common.util;
import java.sql.Timestamp;  
  
public class SnowflakeIdReverse {  
  
    // 假设的起始时间戳，这里用Twitter的起始时间戳作为示例  
    private static final long twepoch = 1288834974657L;  
  
    // 提取时间戳部分  
    private static long extractTimestamp(long id) {  
        // 由于时间戳占用41位，左移22位（数据中心ID+机器ID+序列号=12位）后，再右移12位（序列号部分）  
        return (id >> 22) + twepoch;  
    }  
  
    // 将时间戳转换为可读的日期时间字符串  
    public static Timestamp convertToTimestamp(long id) {  
        long timestamp = extractTimestamp(id);  
        return new Timestamp(timestamp);
    }  
  
    public static void main(String[] args) {  
        // 假设有一个雪花算法生成的ID  
        long snowflakeId = 1234567890123456789L; // 请替换为实际的ID  
        Timestamp dateTime = convertToTimestamp(snowflakeId);  
        System.out.println("时间戳解析的日期时间: " + dateTime);  
    }  
}