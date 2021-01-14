package com.github.rapid.common.security;

/**
 *  权限操作枚举
 * @author badqiu
 *
 */
public enum SecurityActionType {
	READ("r","读权限"),
	WRITE("w","写权限"), 
	ADMIN("a","管理权限");

	private final String shortName;
	private final String desc;

	private SecurityActionType(String shortName, String desc) {
		this.shortName = shortName;
		this.desc = desc;
	}

	public String getShortName() {
		return shortName;
	}

	public String getDesc() {
		return desc;
	}

}
