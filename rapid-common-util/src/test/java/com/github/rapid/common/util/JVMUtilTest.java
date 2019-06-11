package com.github.rapid.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JVMUtilTest {

	@Test
	public void lockFileForOnlyProcess() {
		JVMUtil.lockFileForOnlyProcess(JVMUtilTest.class);
	}

	@Test
	public void getPid() {
		System.out.println("pid:"+JVMUtil.getPid());
	}
	
	static Logger logger = LoggerFactory.getLogger(JVMUtil.class);
	private static List<FileLock> fileLocks = new ArrayList<FileLock>(); 
	public static boolean lockFileForOnlyProcess(String lockName,int lockNum) {
		File file = new File(System.getProperty("java.io.tmpdir"),lockName+"_"+lockNum+".lock");
		try {
			FileOutputStream output = new FileOutputStream(file);
			FileLock fileLock = output.getChannel().tryLock();
			if(fileLock == null) {
				return false;
			}
			fileLocks.add(fileLock);
			return true;
		}catch(IOException e) {
			logger.warn("tryLock file error:'"+file,e);
			return false;
		}
	}
	
}
