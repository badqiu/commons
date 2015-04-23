package com.github.rapid.common.rpc.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;


public class NetUtil {
	public static char splitchar = ' ';
	public static MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
	static HttpClient client = new HttpClient();
	static {
		init();
	}

	public static void init() {
		if (manager != null) {
			manager.shutdown();
			manager = null;
		}
		manager = new MultiThreadedHttpConnectionManager();
		System.setProperty("apache.commons.httpclient.cookiespec",
				"COMPATIBILITY");
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setDefaultMaxConnectionsPerHost(50);
		params.setMaxTotalConnections(1000);
		params.setConnectionTimeout(10000);
		params.setSoTimeout(15000);
		manager.setParams(params);
		manager.closeIdleConnections(15000);
		client.setHttpConnectionManager(manager);
		HttpClientParams cparams = new HttpClientParams();
		cparams.setConnectionManagerTimeout(10000);
		cparams.setSoTimeout(15000);
		client.setParams(cparams);
	}

	public static String readURL(String urladdress) {
		return readURL(urladdress, "ISO-8859-1");
	}
	
	public static String readURL(String urladdress, String encoding) {
		while (urladdress.indexOf("&amp;") > -1) {
			urladdress = replace(urladdress, "&amp;", "&");
		}
		
		urladdress = encodeUrl(urladdress);
		GetMethod method = new GetMethod(urladdress);
		method.setFollowRedirects(true);
		method.setRequestHeader("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)");
		method.setRequestHeader("Accept-Language", "zh-cn");
		
		try {
			client.executeMethod(method);
			InputStream is = method.getResponseBodyAsStream();
			InputStreamReader isr = new InputStreamReader(is, "ISO-8859-1");
			BufferedReader in = new BufferedReader(isr);
			String strread = null;

			StringBuffer buf = new StringBuffer();
			while ((strread = in.readLine()) != null) {
				buf.append(strread);
				buf.append(splitchar);
			}
			in.close();
			is.close();
			isr.close();
			String str = buf.toString();
			if (isFine(encoding)
					&& !encoding.equalsIgnoreCase("ISO-8859-1")) {
				return new String(str.getBytes("ISO-8859-1"), encoding);
			}
			return new String(str.getBytes("ISO-8859-1"), getCharset(str));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			method.releaseConnection();
			manager.deleteClosedConnections();
			if (manager.getConnectionsInPool() > 50) {
				init();
			}
		}
	}
	
	public static int readURL_(String urladdress, String encoding) {
		while (urladdress.indexOf("&amp;") > -1) {
			urladdress = replace(urladdress, "&amp;", "&");
		}
		
		urladdress = encodeUrl(urladdress);
		GetMethod method = new GetMethod(urladdress);
		method.setFollowRedirects(true);
		method.setRequestHeader("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)");
		method.setRequestHeader("Accept-Language", "zh-cn");
		int status = -1;
		try {
			status = client.executeMethod(method);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			method.releaseConnection();
			manager.deleteClosedConnections();
			if (manager.getConnectionsInPool() > 50) {
				init();
			}
		}
		return status;
	}

	public static String getCharset(String str) {
		Pattern p = Pattern.compile("<meta[^>]*>", 2);
		Matcher m = p.matcher(str);
		while (m.find()) {
			String meta = m.group(0).toLowerCase();
			if (meta.indexOf("http-equiv") > -1
					&& meta.indexOf("content-type") > -1
					&& meta.indexOf("content") > -1
					&& meta.indexOf("text/html") > -1) {
				Pattern p2 = Pattern
						.compile("<meta(.*?)charset=(.*?)['\" ]{1}[^>]*>");
				Matcher m2 = p2.matcher(meta);
				if (m2.find()) {
					String charset = m2.group(2);
					if (charset.equals("gb2312")) {
						return "GBK";
					}
					return charset.toUpperCase();
				}
			}
		}
		return "GBK";
	}


	public static String encodeUrl(String url) {
		if (isFine(url)) {
			return url;
		}
		char[] cs = url.toCharArray();
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0; i < cs.length; i++) {
				if (cs[i] > 255) {
					sb.append(URLEncoder.encode(String.valueOf(cs[i]), "GBK"));
				} else {
					sb.append(cs[i]);
				}
			}
			return sb.toString();
		} catch (Exception e) {
			return url;
		}
	}
	
	public static boolean isFine(String str) {
		return str != null && str.length() > 0;
	}
	
	/**
	 * s中的s1替换成s2
	 * @param s
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String replace(String s, String s1, String s2)
	{
		if (s == null)
			return null;
		int i = 0;
		if ((i = s.indexOf(s1, i)) >= 0)
		{
			char ac[] = s.toCharArray();
			char ac1[] = s2.toCharArray();
			int j = s1.length();
			StringBuffer sb = new StringBuffer(ac.length);
			sb.append(ac, 0, i).append(ac1);
			i += j;
			int k;
			for (k = i; (i = s.indexOf(s1, i)) > 0; k = i)
			{
				sb.append(ac, k, i - k).append(ac1);
				i += j;
			}
			sb.append(ac, k, ac.length - k);
			return sb.toString();
		}
		else
		{
			return s;
		}
	}
	public static void main(String[] args) throws UnsupportedEncodingException{
		/*String game = "xxrz";
		String server = "s1";
		String passport = "hedream";
		String KEY = "skj(*&)#@_Qslfjsl*&(&SAF)_";
		StringBuilder authSign = new StringBuilder();
		authSign.append(game).append(server).append(passport).append(KEY);
		String sign_ = EncryptUtil.getMD5(authSign.toString(), "UTF-8");
		StringBuilder url = new StringBuilder();
		url.append("http://udblogin.duowan.com/nagiosLogin.do");
		url.append("?passport=").append(passport);
		url.append("&game=").append(game);
		url.append("&server=").append(server);
		url.append("&sign=").append(sign_);
		System.out.println(url.toString());
		String accid = NetUtil.readURL(url.toString(),"utf8").trim();
		System.out.println(accid);
		*/
		String key_ = "skfjslkj22873(*&(";
		String url2 = "http://58.218.207.245:801/ApiUnite.aspx?userallname=hedream02"+"&sign="+"sign";
		String result = NetUtil.readURL(url2,"UTF-8").trim();
		System.out.println(result);
	}
}
