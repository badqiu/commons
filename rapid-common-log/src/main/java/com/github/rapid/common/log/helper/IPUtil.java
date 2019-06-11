package com.github.rapid.common.log.helper;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;

public class IPUtil {
	/**
	 * 获取第一个非 127.0.0.1 的ipv4的地址
	 * @return
	 */
	private static String ip = null;
	public static String getIp() {
		if(ip == null) {
			ip = getIp0();
		}
		return ip;
	}

	private static String getIp0() {
		Enumeration<NetworkInterface> allNetInterfaces = null;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}
		InetAddress inetAddress = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface =  allNetInterfaces.nextElement();
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				inetAddress =  addresses.nextElement();
				if (inetAddress != null && inetAddress instanceof Inet4Address) {
					String ip = inetAddress.getHostAddress();
					if("127.0.0.1".equals(StringUtils.trim(ip))){
						continue;
					}
					if("localhost".equals(inetAddress.getHostName())) {
						continue;
					}
					return ip;
				}
			}
		}
		return null;
	}
}

