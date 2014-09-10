package com.duowan.common.rpc.fortestinvoker;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.duowan.common.rpc.fortest.api.model.Blog;

public interface UserWebService {
	
	public String dateArgument(Date date,Timestamp timestamp,java.sql.Time time,java.sql.Date sqlDate);
	
	public String notArgument();
	
	public String say(String name,int age,Long timestamp);
	
	public String hello(String[] name,int[] age,List<Long> timestamp);
	
	public String mapArgument(String[] name,int[] age,Map<String,String> map);
	
	public String bye(long p11,Long p12,int p21,Integer p22,byte p31,Byte p32,boolean p41,Boolean p42,double p51,Double p52,char c,BigDecimal bd,BigInteger big);
	
	public String objectArgument(String name,int age,UserInfo userInfo);
	
	public String enumArgument(UserTypeEnum userType,UserTypeEnum[] userTypes);
	
	public String singleBlog(Blog blog);
}
