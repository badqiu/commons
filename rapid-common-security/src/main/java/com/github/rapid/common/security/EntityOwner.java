package com.github.rapid.common.security;

import java.util.List;

/**
 *  得到对象的拥有者
 * 数据库保存owner_list:
 * 
 * [
 * {ownerType:'user',ownerId:1,permission:'rwa'},
 * {ownerType:'role',ownerId:1,permission:'rwa'},
 * {ownerType:'group',ownerId:1,permission:'rwa'},
 * {ownerType:'other',permission:'rwa'},
 * ]
 * 
 * 
 * @author badqiu
 *
 */
public interface EntityOwner {

//	public List<String> getOwnerUserList();
//	public List<String> getOwnerRoleList();
//	public List<String> getOwnerGroupList();
//	public List<String> getOwnerDeptList();
	
	public List<Owner> getOwnerList();
	
}
