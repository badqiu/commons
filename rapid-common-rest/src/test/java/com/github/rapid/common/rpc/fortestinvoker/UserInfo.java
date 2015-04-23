package com.github.rapid.common.rpc.fortestinvoker;

import java.io.Serializable;

public class UserInfo implements Serializable{

	private String name;
	private int age;
	private long weight;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

}
