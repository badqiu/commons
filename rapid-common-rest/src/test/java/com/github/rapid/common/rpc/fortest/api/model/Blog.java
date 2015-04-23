package com.github.rapid.common.rpc.fortest.api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Blog implements Serializable,Cloneable{
	private String id;
	
	private char charValue;
	private Character character;

	private byte byteValue;
	private Byte byteObject;

	private short shortValue;
	private Short shortObject;

	private int intValue;
	private Integer integer;

	private long longValue;
	private Long longObject;

	private double doubleValue;
	private Double doubleObject;

	private float floatValue;
	private Float floatObject;

	private BigDecimal bigDecimal;
	private BigInteger bigInteger;

	private java.util.Date date;
	private java.sql.Date sqlDate;
	private java.sql.Time sqlTime;
	private java.sql.Timestamp timestamp;

	public Blog() {
	}
	
	public Blog(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public char getCharValue() {
		return charValue;
	}

	public void setCharValue(char charValue) {
		this.charValue = charValue;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public byte getByteValue() {
		return byteValue;
	}

	public void setByteValue(byte byteValue) {
		this.byteValue = byteValue;
	}

	public Byte getByteObject() {
		return byteObject;
	}

	public void setByteObject(Byte byteObject) {
		this.byteObject = byteObject;
	}

	public short getShortValue() {
		return shortValue;
	}

	public void setShortValue(short shortValue) {
		this.shortValue = shortValue;
	}

	public Short getShortObject() {
		return shortObject;
	}

	public void setShortObject(Short shortObject) {
		this.shortObject = shortObject;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public Integer getInteger() {
		return integer;
	}

	public void setInteger(Integer integer) {
		this.integer = integer;
	}

	public long getLongValue() {
		return longValue;
	}

	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}

	public Long getLongObject() {
		return longObject;
	}

	public void setLongObject(Long longObject) {
		this.longObject = longObject;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public Double getDoubleObject() {
		return doubleObject;
	}

	public void setDoubleObject(Double doubleObject) {
		this.doubleObject = doubleObject;
	}

	public float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}

	public Float getFloatObject() {
		return floatObject;
	}

	public void setFloatObject(Float floatObject) {
		this.floatObject = floatObject;
	}

	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}

	public void setBigDecimal(BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}

	public BigInteger getBigInteger() {
		return bigInteger;
	}

	public void setBigInteger(BigInteger bigInteger) {
		this.bigInteger = bigInteger;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public java.sql.Date getSqlDate() {
		return sqlDate;
	}

	public void setSqlDate(java.sql.Date sqlDate) {
		this.sqlDate = sqlDate;
	}

	public java.sql.Time getSqlTime() {
		return sqlTime;
	}

	public void setSqlTime(java.sql.Time sqlTime) {
		this.sqlTime = sqlTime;
	}

	public java.sql.Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(java.sql.Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

//	@Override
//	public boolean equals(Object obj) {
//		return EqualsBuilder.reflectionEquals(this, obj);
//	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		return toString().equals(obj.toString());
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", charValue=" + charValue + ", character="
				+ character + ", byteValue=" + byteValue + ", byteObject="
				+ byteObject + ", shortValue=" + shortValue + ", shortObject="
				+ shortObject + ", intValue=" + intValue + ", integer="
				+ integer + ", longValue=" + longValue + ", longObject="
				+ longObject + ", doubleValue=" + doubleValue
				+ ", doubleObject=" + doubleObject + ", floatValue="
				+ floatValue + ", floatObject=" + floatObject + ", bigDecimal="
				+ bigDecimal + ", bigInteger=" + bigInteger + ", date=" + date
				+ ", sqlDate=" + sqlDate + ", sqlTime=" + sqlTime
				+ ", timestamp=" + timestamp + "]";
	}
	
	public Blog clone() {
		try {
			return (Blog)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	

}
