package com.github.rapid.common;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 命名服务: interface@version
 * 
 * @author badqiu
 *
 */
public class NamingService {

	public void bind(String name,Object object) {
		
	}
	
	public void rebind(String name,Object object) {
		
	}
	
	public void unbind(String name) {
	}
	
	public void rename(String oldName,String newName) {
		
	}

	public Object lookup(String name) {
		return null;
	}
	
	public static void main(String[] args) throws MalformedURLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		URL url = new URL("badqiu://www.163.com:8020/rpc/BlogService");
		System.out.println(BeanUtils.describe(url));
	}
	
}
