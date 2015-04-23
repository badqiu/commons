package com.github.rapid.common.rpc.fortestinvoker;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.github.rapid.common.rpc.fortest.api.model.Blog;


public class UserWebServiceImpl implements UserWebService{

	public String say(String name, int age, Long timestamp) {
		System.out.println("UserWebServiceImpl.say() name:"+name+" age:"+age+" timestamp:"+timestamp);
		return "say()"+name;
	}

	public String hello(String[] name, int[] age, List<Long> timestamp) {
		System.out.println("UserWebServiceImpl.hello() name:"+Arrays.toString(name)+" age:"+Arrays.toString(age)+" timestamp:"+timestamp);
		return "hello()"+Arrays.toString(name);
	}

	public String bye(long p11, Long p12, int p21, Integer p22, byte p31,
			Byte p32, boolean p41, Boolean p42, double p51, Double p52, char c,
			BigDecimal bd, BigInteger big) {
		System.out.println("UserWebServiceImpl.bye() p11:"+p11+" p21:"+p21+" p31:"+p31);
		return "bye()"+p11;
	}

	public String notArgument() {
		System.out.println("notArgument()");
		return "notArgument";
	}

	public String mapArgument(String[] name, int[] age, Map<String, String> map) {
		System.out.println("UserWebServiceImpl.mapArgument() name:"+Arrays.toString(name)+" age:"+Arrays.toString(age)+" map:"+map);
		return "mapArgument()";
	}

	public String objectArgument(String name, int age, UserInfo userInfo) {
		try {
			System.out.println("UserWebServiceImpl.objectArgument() name:"+name+" age:"+age+" userInfo:"+BeanUtils.describe(userInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "objectArgument()";
	}

	public String dateArgument(Date date, Timestamp timestamp, Time time,
			java.sql.Date sqlDate) {
		System.out.println("dateArgument() date:"+DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:ss")+" timestamp:"+timestamp+" time:"+time+" sqlDate:"+sqlDate);
		return "dateArgument():"+date;
	}

	public String enumArgument(UserTypeEnum userType, UserTypeEnum[] userTypes) {
		System.out.println("enumArgument() userType:"+userType+" userTypes:"+Arrays.toString(userTypes));
		return "enumArgument():";
	}
	
	public String simple(int age,long big,String name,String sex,double rate,Float f,Date tdate){
		return name;
	}

	public String singleBlog(Blog blog) {
//		System.out.println(blog.toString());
		return blog.toString();
	}

}
