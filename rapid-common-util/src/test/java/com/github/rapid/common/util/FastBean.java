package com.github.rapid.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FastBean {

	/**
	 * 动作标识,采用类似目录树的结构,以便于动作多级分类
	 */
	private String action ;
	
	/**
	 * 动作发生时长,用于分析耗时操作,单位(毫秒)
	 */
	private long duration; 
	
	/**
	 * 动作发生时间
	 */
	private Date atime;
	
	/**
	 * 动作是否成功
	 */
	private boolean flag;
	
	/**
	 * 机器ID,用于标识不同的机器,现一台机器可以打开多个YY客户端,通过这个可以统计机器数
	 */
	private String mid;
	
	/**
	 * 产品标识,用于区分不同的客户端,如游戏大厅,YY,以便将来不同的客户端接入
	 */
	private String product;
	
	/**
	 * 客户端IP(在收集数据服务器获取,不需客户端上报)
	 */
	private String ip;
	
	/**
	 * 客户端代理IP(在收集数据服务器获取,不需客户端上报)
	 */
	private String proxyIp;
	
	/**
	 * 服务器接入数据的时间(取收集数据服务器时间不需客户端上报)
	 */
	private Date stime;
	
	//################二级字段################
	
	/**
	 * 通行证
	 */
	private String passport;
	
	/**
	 * 游戏代号
	 */
	private String game;
	
	/**
	 * 游戏服务器代号
	 */
	private String gameServer;
	
	/**
	 * 启动来源,用于标识游戏大厅是通过什么方式启动,如YY,独立版的游戏大厅(为将来规划)
	 */
	private String runSource;
	
	/**
	 * 安装包版本
	 */
	private String iver;
	
	/**
	 * 自动升级包版本
	 */
	private String uver;
	
	/**
	 * 安装渠道来源,用于标识是通过什么渠道安装的,如游戏大厅暂时是YY
	 */
	private String channel;
	
	/**
	 * 登录来源
	 */
	private String referrer;
	
	/**
	 * 游戏大厅的位置代号
	 */
	private String pid;
	
	/**
	 * 操作系统
	 */
	private String os;
	
	/**
	 * 浏览器版本号
	 */
	private String browerVersion;
	
	/**
	 * 屏幕分辨率
	 */
	private String screenResolution;
	
	/**
	 * 屏幕色深
	 */
	private String screenColor;
	
	/**
	 * 系统语言
	 */
	private String localLanguage;
	
	/**
	 * 是否支持java
	 */
	private boolean java;
	
	/**
	 * 是否支持cookies
	 */
	private boolean cookies;
	
	/**
	 * .net版本
	 */
	private String netVersion;
	
	/**
	 * 个性化数据,mpassport=dw_hemeng,content=xxxx,livetime=2837
	 */
	private Map<String,String> ext = new HashMap<String,String>();
	
	/**
	 * 国家
	 */
	private String country;
	
	/**
	 * 省份
	 */
	private String province;
	
	/**
	 * 城市
	 */
	private String city;
	
	/**
	 * 宽带接入商
	 */
	private String isp;
	
	/**
	 * 对话窗口ID,用于关联一个窗口的所有会话，如根据 /login /logout计算在线时长
	 */
	private String sessionId;
	
	/**
	 * 发生动作时,当前所在的位置
	 */
	private String location;

	/**
	 * 判断数据是否是加密出来的
	 */
	private boolean encrypt = false;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Date getAtime() {
		return atime;
	}

	public void setAtime(Date atime) {
		this.atime = atime;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getGameServer() {
		return gameServer;
	}

	public void setGameServer(String gameServer) {
		this.gameServer = gameServer;
	}

	public String getRunSource() {
		return runSource;
	}

	public void setRunSource(String runSource) {
		this.runSource = runSource;
	}

	public String getIver() {
		return iver;
	}

	public void setIver(String iver) {
		this.iver = iver;
	}

	public String getUver() {
		return uver;
	}

	public void setUver(String uver) {
		this.uver = uver;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getBrowerVersion() {
		return browerVersion;
	}

	public void setBrowerVersion(String browerVersion) {
		this.browerVersion = browerVersion;
	}

	public String getScreenResolution() {
		return screenResolution;
	}

	public void setScreenResolution(String screenResolution) {
		this.screenResolution = screenResolution;
	}

	public String getScreenColor() {
		return screenColor;
	}

	public void setScreenColor(String screenColor) {
		this.screenColor = screenColor;
	}

	public String getLocalLanguage() {
		return localLanguage;
	}

	public void setLocalLanguage(String localLanguage) {
		this.localLanguage = localLanguage;
	}

	public boolean isJava() {
		return java;
	}

	public void setJava(boolean java) {
		this.java = java;
	}

	public boolean isCookies() {
		return cookies;
	}

	public void setCookies(boolean cookies) {
		this.cookies = cookies;
	}

	public String getNetVersion() {
		return netVersion;
	}

	public void setNetVersion(String netVersion) {
		this.netVersion = netVersion;
	}

	public Map<String, String> getExt() {
		return ext;
	}

	public void setExt(Map<String, String> ext) {
		this.ext = ext;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isEncrypt() {
		return encrypt;
	}

	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}
	
}
