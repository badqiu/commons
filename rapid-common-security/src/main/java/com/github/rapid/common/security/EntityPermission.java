package com.github.rapid.common.security;

import java.util.List;

public interface EntityPermission {

//	public List<OwnerPermission> getOwnerUserPermissionList();
//	public List<OwnerPermission> getOwnerRolePermissionList();
//	public List<OwnerPermission> getOwnerGroupPermissionList();
//	public List<OwnerPermission> getOwnerDeptPermissionList();
//	public List<OwnerPermission> getOtherUserPermissionList();
	
	public List<OwnerPermission> getOwnerPermissionList();
	
}
