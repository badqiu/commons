package com.github.rapid.common.util;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class KeyValue<K, V> implements Serializable{

	private static final long serialVersionUID = -7863628178456494468L;
	
	private static String DEFAULT_KEY_VALUE_SEPERATOR = "=";
	
	private K key;
	private V value;

	public KeyValue() {
	}
	
	public KeyValue(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyValue other = (KeyValue) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
	public static KeyValue<String,String> parse(String text) {
		return parse(text,DEFAULT_KEY_VALUE_SEPERATOR);
	}
	
	public static KeyValue<String,String> parse(String text,String seperator) {
		if(StringUtils.isBlank(text)) return null;
		
		int index = text.indexOf(seperator);
		if(index > 0) {
			String key = StringUtils.trim(text.substring(0,index));
			if(StringUtils.isBlank(key)) return null;
			
			String value = StringUtils.trim(text.substring(index + 1, text.length()));
			
			return new KeyValue<String,String>(key,value);
		}
		
		return null;
	}

	@Override
	public String toString() {
		return key + DEFAULT_KEY_VALUE_SEPERATOR + value;
	}

	
}
