package com.github.rapid.common.log.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.DigestUtils;



public class SmsUtil{
	
    public SmsUtil() {
    }

    public static boolean send(final String phone, final String content, boolean wait)   {
        if(wait){
            return send(phone, subuser, content);
        } else {
           new Thread() {

                public void run(){
                    SmsUtil.send(phone, subuser, content);
                }

            }.start();
            return true;
        }
    }

    private static boolean send(String phone, String subuser, String content) {
        long timeMillis = System.currentTimeMillis();
        return send(phone, subuser, timeMillis, content);
    }

    public static boolean send(String phone, String subuser, long timeMillis, String content) {
        String userid = "datawarehouse_stat";
        return send(userid, phone, subuser, timeMillis, content);
    }

    private static boolean send(String userid, String phone, String subuser, long timeMillis, String content)  {
        String time = getTime(timeMillis);
        String mac = getMac(userid, phone, subuser, time);
        try {
            content = URLEncoder.encode(content, "UTF-8");
        }
        catch(UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("对短信内容编码出错 ,content="+ content);
        }
        StringBuilder sb = new StringBuilder((new StringBuilder()).append("http://sms.duowan.com/send/smssending_emay.jsp?userid=").append(userid).toString());
        sb.append((new StringBuilder()).append("&phone=").append(phone).toString());
        sb.append((new StringBuilder()).append("&subuser=").append(subuser).toString());
        sb.append((new StringBuilder()).append("&time=").append(time).toString());
        sb.append((new StringBuilder()).append("&mac=").append(mac).toString());
        sb.append((new StringBuilder()).append("&content=").append(content).toString());
        String url = sb.toString();
        String html = getHtmlCode(url).trim();
//        LOGGER.info(url);
        if(LOGGER.isDebugEnabled())
        	LOGGER.debug((new StringBuilder()).append("code:").append(html).append("  url:").append(url).toString());
        if(html == null || html.length() == 0)
            throw new RuntimeException("\u8BBF\u95EE\u63A5\u53E3\u51FA\u9519.");
        int code = Integer.parseInt(html);
        if(code == SUCCESS)
            return true;
        if(code == MAC_ERROR)
            throw new IllegalArgumentException("短信发送状态code=" +code+ ", mac=" + mac + ",mac值验证错误. ");
        if(code == MISSING_PARAMETER)
            throw new IllegalArgumentException("短信发送状态code=" +code+ ", 参数信息不完整.");
        if(code == UPDATE_DATABASE_ERRROR)
        	throw new IllegalArgumentException("短信发送状态code=" +code+ ", 信息入库失败.");
        if(code == SERVICE_PROVIDER_ERROR)
        	throw new IllegalArgumentException("短信发送状态code=" +code+ ", 发送接口失败.");
        if(code == IDENTIFIER_ERROR)
        	throw new IllegalArgumentException("短信发送状态code=" +code+ ", 项目编号错误.");
        if(code == HAS_KEYWORD)
        	throw new IllegalArgumentException("短信发送状态code=" +code+ ", 手机号码错误.");
        else
            throw new RuntimeException("短信发送失败.");
    }

    private static String getTime(long timeMillis)    {
        Date date = new Date();
        if(timeMillis > 0L)
            date.setTime(timeMillis);
        SimpleDateFormat GET_TIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
        return GET_TIME_FORMAT.format(date);
    }

    static boolean isDebug = false;
    private static String getHtmlCode(String uri)    {
    	if(isDebug) {
    		LOGGER.info("send sms msg:"+uri);
    		return "1";
    	}else {
    		return readUrl(uri,"UTF-8");
    	}
    }

    private static String getMac(String userid, String phone, String subuser, String time)     {
        String digesta = (new StringBuilder()).append(userid).append(phone).append(subuser).append(time).append(KEY).toString();
		return DigestUtils.md5DigestAsHex(digesta.getBytes()).toLowerCase();
    }

	public static String readUrl(String urlString,String encoding)  {
		java.io.InputStream input = null;
		try {
			java.net.URL url = new java.net.URL(urlString);
			input = url.openStream();
			return IOUtils.toString(new java.io.InputStreamReader(input,encoding));
		}catch(IOException e) {
			throw new RuntimeException("read url error:"+urlString,e);
		}finally {
			IOUtils.closeQuietly(input);
		}
	}
	
    public static void main(String args[])    {
        String phone = "13570477590";
        String content = "\u5C0A\u656C\u7684\u7528\u6237\uFF0C\u60A8\u83B7\u5F97\u7684\u624B\u673A\u9A8C\u8BC1\u7801\u662F1234\uFF0C\u8BF7\u5728\u9875\u9762\u4E0A\u8F93\u5165\u8BE5\u9A8C\u8BC1\u7801\u8FDB\u884C\u4E0B\u4E00\u6B65\u3002";
        boolean flag = send(phone, content, true);
        System.out.println((new StringBuilder()).append("flag:").append(flag).toString());
    }

    private static final Log LOGGER = LogFactory.getLog(SmsUtil.class);
    public static final int SUCCESS = 1;
    public static final int MAC_ERROR = -1;
    public static final int MISSING_PARAMETER = -2;
    public static final int UPDATE_DATABASE_ERRROR = -3;
    public static final int SERVICE_PROVIDER_ERROR = -4;
    public static final int IDENTIFIER_ERROR = -5;
    public static final int HAS_KEYWORD = -6;
	private static String subuser = "09010100";
    private static final String KEY = "RDA1NThDQkJEMTUyNzhGN";


}