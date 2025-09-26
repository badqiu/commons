package com.github.rapid.common.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于键（Key）进行数据统计的映射表类。
 * 内部使用Map结构存储多个Stat对象，每个键对应一个独立的统计实例。
 * 默认使用ConcurrentHashMap实现线程安全，若无需线程安全可替换为HashMap。
 * 
 * @author badqiu
 * 
 * @date 2025-09-16
 */
public class StatMap<K> {
    /**
     * 存储键与对应统计对象的映射关系
     */
    private final Map<K, Stat> statsMap;

    /**
     * 是否自动初始化Stat对象的时间戳
     */
    private final boolean autoSetStartTime;

    /**
     * 默认构造函数，创建线程安全的Map并启用自动设置时间戳
     */
    public StatMap() {
        this(true, true);
    }

    /**
     * 参数化构造函数
     * @param threadSafe 是否启用线程安全（true使用ConcurrentHashMap，false使用HashMap）
     * @param autoSetStartTime 是否自动设置Stat对象的开始时间
     */
    public StatMap(boolean threadSafe, boolean autoSetStartTime) {
        this.statsMap = threadSafe ? new ConcurrentHashMap<>() : new HashMap<>();
        this.autoSetStartTime = autoSetStartTime;
    }

    /**
     * 为指定键添加数值
     * @param key 统计项的键
     * @param num 要添加的数值
     */
    public void addNumber(K key, Number num) {
        if (num == null) return;
        addNumber(key, num.doubleValue());
    }

    /**
     * 为指定键添加double值
     * @param key 统计项的键
     * @param value 要添加的double值
     */
    public void addNumber(K key, double value) {
        Stat stat = statsMap.computeIfAbsent(key, k -> new Stat());
        
        // 如果是新创建的Stat实例且启用自动设置时间戳，则设置开始时间
        if (autoSetStartTime && stat.getCount() == 0 && stat.getStartTime() == 0) {
            stat.setStartTime(System.currentTimeMillis());
        }
        
        stat.addNumber(value);
    }

    /**
     * 获取指定键的统计对象
     * @param key 统计项的键
     * @return 对应的Stat对象，如果不存在则返回null
     */
    public Stat getStat(K key) {
        return statsMap.get(key);
    }

    /**
     * 获取指定键的统计对象，如果不存在则创建
     * @param key 统计项的键
     * @return 对应的Stat对象，不会返回null
     */
    public Stat getOrCreateStat(K key) {
        return statsMap.computeIfAbsent(key, k -> new Stat());
    }

    /**
     * 移除指定键的统计对象
     * @param key 要移除的统计项的键
     * @return 被移除的Stat对象，如果不存在则返回null
     */
    public Stat removeStat(K key) {
        return statsMap.remove(key);
    }

    /**
     * 清空所有统计对象
     */
    public void clearAll() {
        statsMap.clear();
    }

    /**
     * 重置所有统计对象（清空计数但保留键）
     */
    public void resetAll() {
        for (Stat stat : statsMap.values()) {
            stat.clean();
        }
    }

    /**
     * 重置指定键的统计对象
     * @param key 要重置的统计项的键
     */
    public void reset(K key) {
        Stat stat = statsMap.get(key);
        if (stat != null) {
            stat.clean();
        }
    }

    /**
     * 获取所有键的集合
     * @return 所有统计键的Set视图
     */
    public Set<K> keySet() {
        return statsMap.keySet();
    }

    /**
     * 获取所有统计对象的集合
     * @return 所有Stat对象的Collection视图
     */
    public Collection<Stat> stats() {
        return statsMap.values();
    }

    /**
     * 获取统计映射表的大小（键的数量）
     * @return 映射表中键值对的数量
     */
    public int size() {
        return statsMap.size();
    }

    /**
     * 检查是否包含指定键
     * @param key 要检查的键
     * @return 如果包含指定键则返回true
     */
    public boolean containsKey(K key) {
        return statsMap.containsKey(key);
    }

    /**
     * 检查统计映射表是否为空
     * @return 如果没有统计项则返回true
     */
    public boolean isEmpty() {
        return statsMap.isEmpty();
    }

    /**
     * 获取整个统计映射表的字符串表示
     * @return 包含所有键和统计信息的字符串
     */
    @Override
    public String toString() {
        if (statsMap.isEmpty()) {
            return "StatMap{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("StatMap{");
        for (Map.Entry<K, Stat> entry : statsMap.entrySet()) {
            sb.append("\n  ")
              .append(entry.getKey())
              .append(": ")
              .append(entry.getValue().toString());
        }
        sb.append("\n}");
        return sb.toString();
    }

    /**
     * 获取指定键的统计信息字符串
     * @param key 统计项的键
     * @return 该键对应的统计信息字符串，如果键不存在则返回null
     */
    public String toString(K key) {
        Stat stat = statsMap.get(key);
        return stat != null ? stat.toString() : null;
    }
}