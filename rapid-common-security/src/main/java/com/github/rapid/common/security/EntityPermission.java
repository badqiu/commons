package com.github.rapid.common.security;

import java.util.Set;

/**
 * 得到对象的拥有者及权限
 * 
 * @author badqiu
 *
 */
public interface EntityPermission {

//	public List<OwnerPermission> getOwnerUserPermissionList();
//	public List<OwnerPermission> getOwnerRolePermissionList();
//	public List<OwnerPermission> getOwnerGroupPermissionList();
//	public List<OwnerPermission> getOwnerDeptPermissionList();
//	public List<OwnerPermission> getOtherUserPermissionList();
	
	public Set<OwnerPermission> getOwnerPermissionSet();
	
}
