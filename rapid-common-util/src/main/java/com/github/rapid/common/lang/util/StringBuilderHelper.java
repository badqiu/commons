package com.github.rapid.common.lang.util;

import java.util.stream.IntStream;

import org.springframework.util.StringUtils;


/**
 * StringBuilder构建工具类.
 * 
 * 主要增加
 * appendNotNull(),
 * appendNotBlank(),
 * appendNotBlankAndTrim(),
 * appendNotBlankIfTrue(),
 * appendNotZero().
 * 
 * @author badqiu
 * 
 */
public class StringBuilderHelper {
    
    private StringBuilder builder = new StringBuilder();
    
    public StringBuilderHelper() {
    }
    
    public StringBuilderHelper(String str) {
    	builder = new StringBuilder(str);
    }
    
    public StringBuilderHelper(CharSequence str) {
    	builder = new StringBuilder(str);
    }
    
    public StringBuilderHelper(int capacity) {
    	builder = new StringBuilder(capacity);
    }
    
    
    public StringBuilderHelper appendNotNull(Object obj) {
        if(obj == null) return this;
        append(obj);
        return this;
    }
    
    public StringBuilderHelper appendNotBlank(Object obj) {
        if(obj == null) return this;
        
        appendNotBlank(String.valueOf(obj));
        return this;
    }
    
    public StringBuilderHelper appendNotBlank(String str) {
        if(StringUtils.hasText(str)) {
        	builder.append(str);
        }
        return this;
    }
    
    public StringBuilderHelper appendNotBlankIfTrue(Object obj,boolean condition) {
        if(condition) {
        	appendNotBlank(obj);
        }
        return this;
    }
    
    public StringBuilderHelper appendNotBlankIfTrue(String str,boolean condition) {
        if(condition) {
        	appendNotBlank(str);
        }
        return this;
    }
    
    public StringBuilderHelper appendNotBlankAndTrim(Object obj) {
        if(obj == null) return this;
        
        appendNotBlankAndTrim(String.valueOf(obj));
        return this;
    }
    
    public StringBuilderHelper appendNotBlankAndTrim(String str) {
        if(StringUtils.hasText(str)) {
        	builder.append(str.trim());
        }
        return this;
    }
    
    public StringBuilderHelper appendNotZero(long number) {
        if(number != 0) {
        	builder.append(number);
        }
        return this;
    }
    
    public StringBuilderHelper appendNotZero(int number) {
        if(number != 0) {
        	builder.append(number);
        }
        return this;
    }
    
    public StringBuilderHelper append(Object obj) {
        builder.append(obj);
        return this;
    }

    public char charAt(int index) {
        return builder.charAt(index);
    }

    public StringBuilderHelper append(int i) {
        builder.append(i);
        return this;
    }

    public StringBuilderHelper insert(int offset, Object obj) {
        builder.insert(offset, obj);
        return this;
    }

    public int lastIndexOf(String str, int fromIndex) {
        return builder.lastIndexOf(str, fromIndex);
    }

    public StringBuilderHelper insert(int offset, boolean b) {
        builder.insert(offset, b);
        return this;
    }

    public StringBuilderHelper append(StringBuffer sb) {
        builder.append(sb);
        return this;
    }

    public StringBuilderHelper append(boolean b) {
        builder.append(b);
        return this;
    }

    public void trimToSize() {
        builder.trimToSize();
    }

    public int indexOf(String str, int fromIndex) {
        return builder.indexOf(str, fromIndex);
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        builder.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public StringBuilderHelper replace(int start, int end, String str) {
        builder.replace(start, end, str);
        return this;
    }

    public StringBuilderHelper delete(int start, int end) {
        builder.delete(start, end);
        return this;
    }

    public StringBuilderHelper append(char[] str) {
        builder.append(str);
        return this;
    }

    public StringBuilderHelper insert(int dstOffset, CharSequence s) {
        builder.insert(dstOffset, s);
        return this;
    }

    public StringBuilderHelper insert(int offset, double d) {
        builder.insert(offset, d);
        return this;
    }

    public String substring(int start, int end) {
        return builder.substring(start, end);
    }

    public IntStream chars() {
        return builder.chars();
    }

    public void ensureCapacity(int minimumCapacity) {
        builder.ensureCapacity(minimumCapacity);
    }

    public StringBuilderHelper insert(int offset, String str) {
        builder.insert(offset, str);
        return this;
    }

    public int length() {
        return builder.length();
    }

    public StringBuilderHelper insert(int offset, long l) {
        builder.insert(offset, l);
        return this;
    }

    public StringBuilderHelper append(CharSequence s) {
        builder.append(s);
        return this;
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return builder.codePointCount(beginIndex, endIndex);
    }

    public String substring(int start) {
        return builder.substring(start);
    }

    public int codePointAt(int index) {
        return builder.codePointAt(index);
    }

    public StringBuilderHelper reverse() {
        builder.reverse();
        return this;
    }

    public StringBuilderHelper append(double d) {
        builder.append(d);
        return this;
    }

    public StringBuilderHelper insert(int offset, char c) {
        builder.insert(offset, c);
        return this;
    }

    public StringBuilderHelper append(String str) {
        builder.append(str);
        return this;
    }

    public void setLength(int newLength) {
        builder.setLength(newLength);
    }

    public StringBuilderHelper insert(int index, char[] str, int offset, int len) {
        builder.insert(index, str, offset, len);
        return this;
    }

    public StringBuilderHelper append(long lng) {
        builder.append(lng);
        return this;
    }

    public int lastIndexOf(String str) {
        return builder.lastIndexOf(str);
    }

    public void setCharAt(int index, char ch) {
        builder.setCharAt(index, ch);
    }

    public StringBuilderHelper deleteCharAt(int index) {
        builder.deleteCharAt(index);
        return this;
    }

    public StringBuilderHelper append(char c) {
        builder.append(c);
        return this;
    }

    public StringBuilderHelper insert(int dstOffset, CharSequence s, int start, int end) {
        builder.insert(dstOffset, s, start, end);
        return this;
    }

    public int indexOf(String str) {
        return builder.indexOf(str);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return builder.offsetByCodePoints(index, codePointOffset);
    }

    public StringBuilderHelper appendCodePoint(int codePoint) {
        builder.appendCodePoint(codePoint);
        return this;
    }

    public StringBuilderHelper insert(int offset, char[] str) {
        builder.insert(offset, str);
        return this;
    }

    public StringBuilderHelper append(char[] str, int offset, int len) {
        builder.append(str, offset, len);
        return this;
    }

    public StringBuilderHelper insert(int offset, float f) {
        builder.insert(offset, f);
        return this;
    }

    public IntStream codePoints() {
        return builder.codePoints();
    }

    public int codePointBefore(int index) {
        return builder.codePointBefore(index);
    }

    public CharSequence subSequence(int start, int end) {
        return builder.subSequence(start, end);
    }

    public int capacity() {
        return builder.capacity();
    }

    public StringBuilderHelper append(float f) {
        builder.append(f);
        return this;
    }

    public StringBuilderHelper insert(int offset, int i) {
        builder.insert(offset, i);
        return this;
    }

    public StringBuilderHelper append(CharSequence s, int start, int end) {
        builder.append(s, start, end);
        return this;
    }
    
    @Override
    public String toString() {
        return builder.toString();
    }
    
    // 添加一些实用方法
    
    /**
     * 如果字符串不为空则追加，并在前后添加分隔符
     */
    public StringBuilderHelper appendWithSeparator(String str, String separator) {
        if (str != null && !str.isEmpty()) {
            if (builder.length() > 0) {
                builder.append(separator);
            }
            builder.append(str);
        }
        return this;
    }
    
    /**
     * 清除内容
     */
    public StringBuilderHelper clear() {
        builder.setLength(0);
        return this;
    }
    
    /**
     * 检查是否为空
     */
    public boolean isEmpty() {
        return builder.length() == 0;
    }
}