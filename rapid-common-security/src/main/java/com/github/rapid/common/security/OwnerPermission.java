package com.github.rapid.common.security;

import java.util.Set;

/**
 * 拥有者拥有的权限集合
 * 
 * @author badqiu
 *
 */
public class OwnerPermission {
	
	private Owner owner;
	private Set<Action> permissions;
	
}
