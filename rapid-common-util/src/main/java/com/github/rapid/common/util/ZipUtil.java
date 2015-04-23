package com.github.rapid.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class ZipUtil {
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

	private static void zip(ZipOutputStream out, File dir, String base)
			throws IOException {
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

	public static void main(String[] temp) {
		ZipUtil.zip("d:/tmp/abc.zip", new File("d:/temp/"));
	}
}