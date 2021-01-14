package com.github.rapid.common.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/** 登录用户信息 */
public class LoginUser implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long userId;
	private String username;
	private boolean superAdmin; // 是否超级管理员
	private Set<String> userPermissionSet = new HashSet<String>(); // 用户拥有的权限
	private Date loginTime = new Date();
//		private Set<String> userRoleSet; //用户拥有的角色

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public Set<String> getUserPermissionSet() {
		return userPermissionSet;
	}

	public void setUserPermissionSet(Set<String> userPermissionSet) {
		this.userPermissionSet = userPermissionSet;
	}
	
	public LoginUser addPermission(Class<?> actionType,String permission) {
		addPermission(toActionTypeString(actionType),permission);
		return this;
	}
	
	private static String toActionTypeString(Class<?> actionType) {
		return actionType.getSimpleName().toLowerCase();
	}

	public LoginUser addPermission(String actionType,String permission) {
		userPermissionSet.add(actionType+":"+permission);
		return this;
	}
	
	public boolean hasPermission(Class<?> actionType,String permission) {
		return hasPermission(toActionTypeString(actionType),permission);
	}
	
	public boolean hasPermission(String actionType,String permission) {
		if(superAdmin) return true;
		
		if(userPermissionSet.contains(actionType)){
			return true;
		}
		
		if(userPermissionSet.contains(actionType+":"+ActionType.ADMIN.getShortName())) {
			return true;
		}
		
		if(userPermissionSet.contains(actionType+":"+permission)) {
			return true;
		}
		return false;
	}

	public Date getLoginTime() {
		return loginTime;
	}
	
//		public Set<String> getUserRoleSet() {
//			return userRoleSet;
//		}
//		public void setUserRoleSet(Set<String> userRoleSet) {
//			this.userRoleSet = userRoleSet;
//		}

}