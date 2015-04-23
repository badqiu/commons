package com.github.rapid.common.web.wrapper;
/**
 * 
 * @author badqiu
 *
 */
public class UnsafeRedirectException extends RuntimeException {
	private static final long serialVersionUID = -8187073279628212468L;
	
	private String location;
	public UnsafeRedirectException(String location) {
		this.location = location;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "unsafe location:" + getLocation();
	}
}
