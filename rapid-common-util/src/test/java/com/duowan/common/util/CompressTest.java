package com.duowan.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.xerial.snappy.Snappy;

public class CompressTest extends Assert{

	private String input = null;
	
	@Before
	public void setUp() throws FileNotFoundException, IOException {
		input = FileUtils.readFileToString(ResourceUtils.getFile("classpath:compress_test_input.txt"));
	}
	
	@Test
	public void testCompressTps() throws DataFormatException {
		System.out.println("------------Inflate, compress tps--------------");
		byte[] bytes = input.getBytes();
		for(int level = 1; level <= 9; level++) {
			Profiler.start("compress-"+level+" length:"+compress(level,input.length(),bytes).length,10000);
			for(int i = 1; i < 10000; i++) {
				compress(level,input.length(),bytes);
			}
			Profiler.release();
			Profiler.printDump();
		}
		
		byte[] compressed = compress(input.length(),bytes);
		Profiler.start("decompress",10000);
		for(int i = 1; i < 10000; i++) {
			decompress(input.length(),compressed);
		}
		Profiler.release();
		
		Profiler.printDump();
		
	}
	
	@Test
	public void testSnappyCompressTps() throws DataFormatException, IOException {
		System.out.println("------------Snappy, compress tps--------------");
		Profiler.start("compress",10000);
		byte[] bytes = input.getBytes();
		for(int i = 1; i < 10000; i++) {
			Snappy.compress(bytes);
		}
		Profiler.release();
		Profiler.printDump();
		
		byte[] compressed = Snappy.compress(bytes);
		Profiler.start("decompress",10000);
		for(int i = 1; i < 10000; i++) {
			Snappy.uncompress(compressed);
		}
		Profiler.release();
		
		Profiler.printDump();
		
	}
	
	@Test
	public void test_by_size() throws DataFormatException, IOException {
		
		System.out.println("------------Inflate,input by RandomStringUtils.randomAlphabetic(count) --------------");
		for(int i = 1; i < 2046; i+= 100) {
			String str = RandomStringUtils.randomAlphabetic(i);
			testCompressAndDecompress(str);
		}
		System.out.println("------------Inflate,input by normal english article--------------");
		for(int i = 1; i < 2046; i+= 100) {
			String str = input.substring(0,i);
			testCompressAndDecompress(str);
		}
		
		
		System.out.println("------------Snappy,input by RandomStringUtils.randomAlphabetic(count) --------------");
		for(int i = 1; i < 2046; i+= 100) {
			String str = RandomStringUtils.randomAlphabetic(i);
			testCompressAndDecompressBySnappy(str);
		}
		System.out.println("------------Snappy,input by normal english article--------------");
		for(int i = 1; i < 3046; i+= 100) {
			String str = input.substring(0,i);
			testCompressAndDecompressBySnappy(str);
		}
		
		
		System.out.println("--------------------------");
		for(int i = 9086; i < 1024 * 1024; i += i) {
			String str = RandomStringUtils.randomAlphabetic(i);
			testCompressAndDecompress(str);
		}
	}

	private void testCompressAndDecompressBySnappy(String str) throws DataFormatException, IOException {
		int bufSize = str.length() * 2;
		byte[] input = str.getBytes();
		byte[] output = Snappy.compress(input);
		int incomeLength = input.length - output.length;
		int incomePercent = (int)(((float)incomeLength/input.length) * 100.0);
		System.out.println("input.length="+input.length+" output.length="+output.length+" Income.length:"+incomeLength+" income.percent:"+incomePercent+"%");
		
		String decompress = new String(Snappy.uncompress(output));
		assertEquals(str,decompress);
	}
	
	private void testCompressAndDecompress(String str) throws DataFormatException {
		int bufSize = str.length() * 2;
		byte[] input = str.getBytes();
		byte[] output = compress(bufSize, input);
		int incomeLength = input.length - output.length;
		int incomePercent = (int)(((float)incomeLength/input.length) * 100.0);
		System.out.println("input.length="+input.length+" output.length="+output.length+" Income.length:"+incomeLength+" income.percent:"+incomePercent+"%");
		
		String decompress = new String(decompress(bufSize, output));
		assertEquals(str,decompress);
	}

	public static byte[] compress(byte[] input) {
		return compress(-1,input.length,input);
	}
	
	public static byte[] compress(int bufSize, byte[] input) {
		return compress(-1,bufSize,input);
	}
	
	public static byte[] compress(int compressLevel,int bufSize, byte[] input) {
		if(input == null) return null;
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream(bufSize);
			Deflater deflater = new Deflater(compressLevel,true);
			DeflaterOutputStream dos = new DeflaterOutputStream(output,deflater);
			dos.write(input);
			dos.close();
			return output.toByteArray();
		}catch(IOException e) {
			throw new RuntimeException("cannot compress input data,"+e,e);
		}
	}

	public static byte[] decompress(byte[] input)  {
		return decompress(500,input);
	}
	
	public static byte[] decompress(int bufSize, byte[] input)  {
		if(input == null) return null;
		
		try {
			Inflater inflater = new Inflater(true);
			ByteArrayInputStream bis = new ByteArrayInputStream(input);
			InflaterInputStream in = new InflaterInputStream(bis,inflater);
			
			ByteArrayOutputStream bout = new ByteArrayOutputStream(bufSize);
			ioCopy(bufSize, in,bout);
			return bout.toByteArray();
		}catch(IOException e) {
			throw new RuntimeException("cannot decompress input data,"+e,e);
		}
	}

	private static void ioCopy(int bufSize, InputStream in,OutputStream out
			) throws IOException {
		byte[] buf = new byte[bufSize];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
		    out.write(buf, 0, len);
		}
	}
	
}
