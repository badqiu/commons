package com.duowan.common.rpc.fortest.api.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.duowan.common.rpc.fortestinvoker.UserInfo;
import com.duowan.common.rpc.fortestinvoker.UserTypeEnum;

public class ComplexObject implements Serializable {

	private List<UserInfo> userInfoList;
	
	private Set<UserInfo> userInfoSet;
	
	private Collection<UserInfo> userInfoCollection;
	
	private LinkedHashSet<UserInfo> userInfoLinkedHashSet;
	
	private LinkedHashMap<String,UserInfo> userInfoLinkedLinkedHashMap;
	
	private Map<String,UserInfo> userInfoMap;
	
	private UserTypeEnum userTypeEnum;
	
	private UserTypeEnum[] userTypeEnumArray;
	
	private UserInfo[] userInfoArray;

	public List<UserInfo> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<UserInfo> userInfoList) {
		this.userInfoList = userInfoList;
	}

	public Set<UserInfo> getUserInfoSet() {
		return userInfoSet;
	}

	public void setUserInfoSet(Set<UserInfo> userInfoSet) {
		this.userInfoSet = userInfoSet;
	}

	public Collection<UserInfo> getUserInfoCollection() {
		return userInfoCollection;
	}

	public void setUserInfoCollection(Collection<UserInfo> userInfoCollection) {
		this.userInfoCollection = userInfoCollection;
	}

	public LinkedHashSet<UserInfo> getUserInfoLinkedHashSet() {
		return userInfoLinkedHashSet;
	}

	public void setUserInfoLinkedHashSet(
			LinkedHashSet<UserInfo> userInfoLinkedHashSet) {
		this.userInfoLinkedHashSet = userInfoLinkedHashSet;
	}

	public LinkedHashMap<String, UserInfo> getUserInfoLinkedLinkedHashMap() {
		return userInfoLinkedLinkedHashMap;
	}

	public void setUserInfoLinkedLinkedHashMap(
			LinkedHashMap<String, UserInfo> userInfoLinkedLinkedHashMap) {
		this.userInfoLinkedLinkedHashMap = userInfoLinkedLinkedHashMap;
	}

	public Map<String, UserInfo> getUserInfoMap() {
		return userInfoMap;
	}

	public void setUserInfoMap(Map<String, UserInfo> userInfoMap) {
		this.userInfoMap = userInfoMap;
	}

	public UserTypeEnum getUserTypeEnum() {
		return userTypeEnum;
	}

	public void setUserTypeEnum(UserTypeEnum userTypeEnum) {
		this.userTypeEnum = userTypeEnum;
	}

	public UserTypeEnum[] getUserTypeEnumArray() {
		return userTypeEnumArray;
	}

	public void setUserTypeEnumArray(UserTypeEnum[] userTypeEnumArray) {
		this.userTypeEnumArray = userTypeEnumArray;
	}

	public UserInfo[] getUserInfoArray() {
		return userInfoArray;
	}

	public void setUserInfoArray(UserInfo[] userInfoArray) {
		this.userInfoArray = userInfoArray;
	}
	
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
