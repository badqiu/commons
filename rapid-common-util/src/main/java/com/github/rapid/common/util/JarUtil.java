package com.github.rapid.common.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.github.rapid.common.util.Profiler;
/**
 *  可以将classpath的jar包,打包成一个jar包
 *  
 * @author badqiu
 *
 */
public class JarUtil {
	private static Logger logger = LoggerFactory.getLogger(JarUtil.class);
	
	/** 合并文件的目录 */
	private static List<String> mergeDirPatterns = Arrays.asList("META-INF/**/*");
	private static List<String> ignoreDirPatterns = Arrays.asList("META-INF/MANIFEST.MF");

	public static String[] splitClasspath(String classpath) {
		String[] classpathArray = StringUtils.tokenizeToStringArray(classpath, ":;,");
		return classpathArray;
	}
	
	public static File buildOneJarIfHasChange(String classpath) {
		return buildOneJarIfHasChange("default",classpath);
	}
	
	public static File buildOneJarIfHasChange(String resultJarFilename,String classpath) {
		if(!StringUtils.hasText(classpath)) {
			return null;
		}
		
		String filesDigests = allFileSizeAndLastModifiedDigest(classpath);
		String md5 = DigestUtils.md5Hex(filesDigests);
		File tempJar = new File(getTempDirectory(),"tmp_jar/"+resultJarFilename+"_"+md5+".jar");
		if(tempJar.exists()) {
			return tempJar;
		}
		
		try {
			buildOneJar(tempJar.getAbsolutePath(),classpath);
			return tempJar;
		}catch(Exception e) {
			tempJar.delete();
			throw new RuntimeException(e);
		}
	}
	
	public static void buildOneJar(String jarFile,String classpath)  {
		
		File tempBuildDir = new File(getTempDirectory(),"tmp_build_jar_dir/"+System.currentTimeMillis());
		Profiler.start("buildOneJar");
		try {
			
			logger.info("start unzipJars: "+jarFile+" tempBuildDir:"+tempBuildDir+" classpat:"+classpath);
			Profiler.enter("unzipJars");
			unzipJars(tempBuildDir,classpath,mergeDirPatterns);
			Profiler.release();
			
			logger.info("start zip: "+jarFile+" tempBuildDir:"+tempBuildDir);
			Profiler.enter("zip");
			new File(jarFile).getParentFile().mkdirs();
			ZipUtil.zip(jarFile, tempBuildDir);
			Profiler.release();
			
			logger.info("start cleanDirectory, tempBuildDir:"+tempBuildDir);
			Profiler.enter("cleanTempBuildDir");
			FileUtils.cleanDirectory(tempBuildDir);
			Profiler.release();
			
		}catch(Exception e) {
			throw new RuntimeException("buildOneJar file error,classpath:"+classpath,e);
		}finally {
			Profiler.release();
			logger.info("buildOneJar,jarFile:"+jarFile+" costTime dump:\n"+Profiler.dump());
			try {
				FileUtils.cleanDirectory(tempBuildDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }
	
	public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }
	
	/**
	 * 所有文件的摘要信息
	 * @return
	 */
	public static String allFileSizeAndLastModifiedDigest(String classpath) {
		String[] classpathArray = splitClasspath(classpath);
		List<File> files = listAllFile(classpathArray);
		List<String> sizes = new ArrayList<String>();
		for(File file : files) {
			if(file.isFile() && file.exists()) {
				long length = file.length();
				sizes.add(file+":"+length+":"+file.lastModified());
			}else if(file.isDirectory()) {
//				unzipDir(file,tempBuildDir);
			}
		}
		return org.apache.commons.lang.StringUtils.join(sizes,"\n");
	}
	
	public static void unzipJars(File tempUnzipDir,String classpath,List<String> mergeDirPatterns) {
		tempUnzipDir.mkdirs();
		
		String[] classpathArray = splitClasspath(classpath);
		List<File> files = listAllFile(classpathArray);
		for(File file : files) {
			if(file.isFile() && file.exists()) {
				logger.info("unzipJar:"+file);
				unzipJar(file.getPath(),tempUnzipDir,mergeDirPatterns);
			}else if(file.isDirectory()) {
//				unzipDir(file,tempBuildDir);
			}
		}
		
	}

	private static List<File> listAllFile(String[] paths) {
		List<File> r = new ArrayList<File>();
		for(String path : paths) {
			if(!StringUtils.hasText(path)) {
				continue;
			}
			
			File file = new File(path);
			if(file.isDirectory()) {
				r.addAll(Arrays.asList(file.listFiles()));
			}else if(file.isFile()) {
				r.add(file);
			}
		}
		return r;
	}

	private static void unzipDir(File file,File unzipDir) {
		for(File f : file.listFiles()) {
			ZipUtil.unzip(f.getPath(), unzipDir,null,ignoreDirPatterns);
		}
	}
	
	public static void unzipJar(String jarFile, File destDir,List<String> mergeDirPatterns) {
		if(jarFile.endsWith(".jar")) {
			ZipUtil.unzip(jarFile,destDir,mergeDirPatterns,ignoreDirPatterns);
		}
	}

	public static void main(String... args) {
		String classpath = System.getProperty("classpath");
		Assert.hasText(classpath,"-Dclasspath must be not blank");
		
		System.out.println("buildOneJarIfHasChange:"+classpath);
		File jarFile = JarUtil.buildOneJarIfHasChange(classpath);
		System.out.println("result jarFile:"+jarFile+" size:"+jarFile.length() / 1024 +"KB");
	}
	

	
}
