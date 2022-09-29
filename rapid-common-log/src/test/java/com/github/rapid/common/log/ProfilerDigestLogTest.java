package com.github.rapid.common.log;

import org.springframework.util.Assert;

import com.github.rapid.common.util.Profiler;


public class ProfilerDigestLogTest {

	public static void main(String[] args) throws Exception {
		ProfilerDigestLog.getDigestLogContext().put("clientIp", "192.192.168.100");
		
		Profiler.start("start-" + 1);
		Thread.sleep(100);
		
		Profiler.enter("enter-" + 1);
		Thread.sleep(200);
		Profiler.release();
		
		
		Profiler.release();
		
		
		
		System.out.println("Profiler.dump()");
		System.out.println(Profiler.dump() + "\n\n");
		System.out.println("ProfilerDigestLog.getDigestLog()");
		System.out.println(ProfilerDigestLog.getDigestLog() + "\n\n");
		ProfilerLogger.infoDigestLogAndDump();
	}
	
}
