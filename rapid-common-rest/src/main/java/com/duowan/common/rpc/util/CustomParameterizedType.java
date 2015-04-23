package com.duowan.common.rpc.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 自定义的 ParameterizedType
 * @author badqiu
 *
 */
public class CustomParameterizedType implements ParameterizedType {
	private Type rawType;
	private Type ownerType;
	private Type[] actualTypeArguments;
	
	public CustomParameterizedType(Type rawType,Type ownerType,Type[] actualTypeArguments) {
		this.rawType = rawType;
		this.ownerType = ownerType;
		this.actualTypeArguments = actualTypeArguments;
	}
	
	public Type[] getActualTypeArguments() {
		return actualTypeArguments;
	}

	public Type getOwnerType() {
		return ownerType;
	}

	public Type getRawType() {
		return rawType;
	}
	
}

