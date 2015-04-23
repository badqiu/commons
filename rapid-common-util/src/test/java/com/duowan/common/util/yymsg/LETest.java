package com.duowan.common.util.yymsg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class LETest {
	public @Rule TestName testName = new TestName();
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	LEDataOutputStream leoutput = new LEDataOutputStream(output);
	@Before
	public void setUp() {
		System.out.println("\n\n--------------------------------------");
		System.out.print(testName.getMethodName()+"()   ");
	}
	
	@After
	public void tearDown() throws UnsupportedEncodingException {
		System.out.print(Arrays.toString(output.toByteArray()) +" string:"+output.toString());
	}
	
	@Test
	public void test_writeInt() throws IOException {
		leoutput.writeInt(100);
		ByteArrayOutputStream x = new ByteArrayOutputStream();
		x.write(toBytes(100));
		System.out.println("\n byte:"+Arrays.toString(x.toByteArray()));
	}
	
	  /**
	   * Convert an int value to a byte array
	   * @param val value
	   * @return the byte array
	   */
	  public static byte[] toBytes(int val) {
	    byte [] b = new byte[4];
	    for(int i = 3; i > 0; i--) {
	      b[i] = (byte) val;
	      val >>>= 8;
	    }
	    b[0] = (byte) val;
	    return b;
	  }
	
	@Test
	public void test_writeBoolean() throws IOException {
		leoutput.writeBoolean(true);
	}
	
	@Test
	public void test_writeBytes() throws IOException {
		leoutput.writeBytes("abc中国");
	}
	
	@Test
	public void test_writeLong() throws IOException {
		leoutput.writeLong(11111111111111L);
	}
	
}
