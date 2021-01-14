package com.github.rapid.common.security;

/**
 *  权限操作枚举
 * @author badqiu
 *
 */
public enum ActionType {
	READ("r","读"),
	WRITE("w","写"), 
	ADMIN("a","管理");

	private final String shortName;
	private final String desc;

	private ActionType(String shortName, String desc) {
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
