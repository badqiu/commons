package com.github.rapid.common;

public class App {

	private String app;
	private String baseDir = "/data";
	
	private String getDataDir() {
		return "/data/"+app+"/data";
	}
	
	private String getConfigDir() {
		return "/data/"+app+"/conf";
	}
	
	private String getLogDir() {
		return "/data/log/"+app;
	}
	
}
