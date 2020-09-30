package com.github.rapid.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class ZipUtil {
	private static Logger logger = LoggerFactory.getLogger(ZipUtil.class);
	public static int defaultZipLevel = Deflater.DEFAULT_COMPRESSION;
	
	public ZipUtil() {
	}

	public static void zip(String zipFileName, String inputDir) {
		zip(zipFileName, new File(inputDir));
	}

	public static void zip(String zipFileName, File inputDir) {
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(
					zipFileName));
			out.setLevel(defaultZipLevel);
			zip(out, inputDir, "");
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static void zip(String inputDir, OutputStream output) {
		if(!new File(inputDir).exists()) {
			return;
		}
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(output);
			zip(out, new File(inputDir), "");
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			IOUtils.closeQuietly(out);
		}
	}

	private static void zip(ZipOutputStream out, File dir, String base) throws IOException {
		if (dir.isDirectory()) {
			File[] fl = dir.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(dir);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}
	
	public static void unzip(String zipFilePath, File destDir,List<String> mergeDirPatterns,List<String> ignoreDirPatterns) {
		if(!StringUtils.hasText(zipFilePath)) {
			return;
		}
		
        // create output directory if it doesn't exist
        if(!destDir.exists()) destDir.mkdirs();
        
        FileInputStream fis = null;
        ZipInputStream zis = null;
        try {
            fis = new FileInputStream(zipFilePath);
            zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                processZipEntry(destDir, zis, ze,mergeDirPatterns,ignoreDirPatterns);
                ze = zis.getNextEntry();
            }
            
            //close last ZipEntry
            zis.closeEntry();
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        	IOUtils.closeQuietly(zis);
        	IOUtils.closeQuietly(fis);
        }
        
    }

	private static void processZipEntry(File destDir, ZipInputStream zis, ZipEntry ze,List<String> mergeDirPatterns,List<String> ignoreDirPatterns)
			throws FileNotFoundException, IOException {
		String fileName = ze.getName();
		File outputFile = new File(destDir,fileName);
		if(ze.isDirectory()) {
			outputFile.mkdirs();
			return;
		}
		
//	    System.out.println("Unzipping to "+newFile.getAbsolutePath());
	    outputFile.getParentFile().mkdirs();
	    
	    FileOutputStream fos = null;
	    try {
	    	if(isMergeFile(fileName,mergeDirPatterns,ignoreDirPatterns) && outputFile.exists()) {
	    		logger.info("merge zip file:"+fileName);
	    		
	    		String content = FileUtils.readFileToString(outputFile);
	    		String result = content + "\n" + IOUtils.toString(zis);
	    		FileUtils.writeStringToFile(outputFile, result);
	    	}else {
	    		fos = new FileOutputStream(outputFile);
	    		IOUtils.copyLarge(zis, fos);
	    	}
	    }finally {
	    	IOUtils.closeQuietly(fos);
	    }
	    zis.closeEntry();
	}

	private static boolean isMergeFile(String path, List<String> mergeDirPatterns,List<String> ignoreDirPatterns) {
		return AnyPathMatcherUtil.match(path, mergeDirPatterns, ignoreDirPatterns);
	}

	public static void main(String[] temp) {
		ZipUtil.zip("d:/tmp/abc.zip", new File("d:/temp/"));
	}
}