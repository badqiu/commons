package com.github.rapid.common.util;

import java.io.Serializable;

public class KeyValue<K, V> implements Serializable{

	private static final long serialVersionUID = -7863628178456494468L;
	
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

	public KeyValue<K,V> setKey(K key) {
		return new KeyValue(key,this.value);
	}

	public V getValue() {
		return value;
	}

	public KeyValue<K,V> setValue(V value) {
		return new KeyValue(this.key,value);
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

	@Override
	public String toString() {
		return "KeyValue [key=" + key + ", value=" + value + "]";
	}

	
}
