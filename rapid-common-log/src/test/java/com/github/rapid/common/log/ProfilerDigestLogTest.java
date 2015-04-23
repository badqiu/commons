package com.github.rapid.common.log;

import org.springframework.util.Assert;

import com.github.rapid.common.util.Profiler;


public class ProfilerDigestLogTest {

	public static void main(String[] args) throws InterruptedException {
		ProfilerDigestLog.getDigestLogContext().put("clientIp", "192.192.168.100");
		
		Profiler.start("start-" + 1);


		Profiler.release();
		System.out.println(Profiler.dump() + "\n\n");
		System.out.println(ProfilerDigestLog.getDigestLog() + "\n\n");
		ProfilerLogger.infoDigestLogAndDump();
	}
	
}
