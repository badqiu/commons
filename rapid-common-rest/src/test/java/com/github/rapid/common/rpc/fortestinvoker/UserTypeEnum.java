package com.github.rapid.common.rpc.fortestinvoker;

public enum UserTypeEnum {
	SYSTEM("system"),CUSTOM("custom"),PERSON("person");
	
	private String code;

	private UserTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
