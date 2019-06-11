package com.github.rapid.common;

import org.apache.commons.lang3.StringUtils;

public class App {

	private String app;
	private String baseDir = "/data";
	
	public App() {
	}
	
	public App(String app) {
		setApp(app);
	}
	
	public void setApp(String app) {
		this.app = app;
	}

	public String getDataDir() {
		return "/data/"+app+"/data";
	}
	
	public String getConfigDir() {
		return "/data/"+app+"/conf";
	}
	
	public String getLogDir() {
		return "/data/log/"+app;
	}
	
	public String getTmpDir() {
		return System.getProperty("java.io.tmpdir")+"/"+app;
	}
	
	public String getAppMode() {
		String appMode =  System.getProperty("APP_MODE");
		if(StringUtils.isBlank(appMode)) {
			appMode = System.getenv("APP_MODE");
		}
		return appMode;
	}
	
}
