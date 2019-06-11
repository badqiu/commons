package com.github.rapid.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.github.rapid.common.util.DateConvertUtil;

/**
 * 上传文件保存的工具类
 * 
 * @author badqiu
 *
 */
public class UploadFileProcessor {

	private static Logger logger = LoggerFactory.getLogger(UploadFileProcessor.class);
	
	private String rootDir = null;
	
	public UploadFileProcessor() {
	}
	
	public UploadFileProcessor(String rootDir) {
		setRootDir(rootDir);
	}
	
	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		Assert.hasText(rootDir,"rootDir must be not empty");
		Assert.isTrue(rootDir.endsWith("/")," rootDir.endsWith('/') must be true");
		this.rootDir = rootDir;
	}

	public String saveUploadFile(String fileModule,String filenameExtension,InputStream input){
		return saveUploadFile(fileModule,new Date(),filenameExtension,input);
	}
	
	public String saveUploadFile(String fileModule,Date saveDate,String filenameExtension,InputStream input){
		String filename = UUID.randomUUID().toString().replace("-","");
		return saveUploadFile(fileModule, saveDate, filename,filenameExtension, input);
	}

	/**
	 * 保存上传文件
	 * 
	 * @param fileModule 文件模块
	 * @param saveDate 时间,用于创建目录
	 * @param filename 文件名
	 * @param filenameExtension 文件扩展名
	 * @param input 输入流,用完会关闭
	 * 
	 * @return 保存后的文件相对路径
	 */
	public String saveUploadFile(String fileModule, Date saveDate,String filename, String filenameExtension, InputStream input) {
		Assert.hasText(fileModule,"fileModule must be not empty");
		Assert.hasText(filenameExtension,"filenameExtension must be not empty");
		Assert.notNull(saveDate,"saveDate must be not null");
		Assert.notNull(input,"input must be not null");
		
		String dateDir = DateConvertUtil.format(saveDate, "/yyyy/MM/dd/");
		String filePath = fileModule + dateDir + filename + "." + StringUtils.lowerCase(filenameExtension);
		saveUploadFile(filePath,input);
		return filePath;
	}
	
	public void saveUploadFile(String filePath,InputStream input) {
		File file = new File(rootDir,filePath);
		logger.info("saveUploadFile file:"+file);
		BufferedOutputStream output = null;
		try {
			file.getParentFile().mkdirs();
			output = new BufferedOutputStream(new FileOutputStream(file),16384);
			IOUtils.copy(input, output);
		}catch(IOException e){
			throw new RuntimeException("save upload file error,filePath:"+filePath,e);
		}finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	} 
	
	/**
	 * 根据相对路径，得到所有完整路径文件件，文件间用逗号分隔
	 * @param filePath
	 * @return
	 */
	public List<File> getUploadFiles(String filePaths) {
		if(StringUtils.isBlank(filePaths)) return null;
		List<String> paths = Arrays.asList(filePaths.split(","));
		return paths.stream().filter((v) -> { 
			return StringUtils.isNotBlank(v);}
		).map( path -> {
			return getUploadFile(path);
		}).collect(Collectors.toList());
	}
	
	/**
	 * 根据相对路径，得到完整路径文件
	 * @param filePath
	 * @return
	 */
	public File getUploadFile(String filePath) {
		String uploadPath = getUploadPath(filePath);
		if(uploadPath == null) return null;
		return new File(uploadPath);
	}
	
	/**
	 * 根据相对路径，得到完整路径
	 * @param filePath
	 * @return
	 */
	public String getUploadPath(String filePath) {
		if(StringUtils.isBlank(filePath)) return null;
		
		return rootDir + StringUtils.trim(filePath);
	}
	
	public boolean deleteFile(String filePath) {
		if(StringUtils.isBlank(filePath)) return false;
		
		return getUploadFile(filePath).delete();
	}
	
}
