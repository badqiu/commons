package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmartFormatTest {

	@Test
	public void test() {
    	System.out.println(SmartFormat.format(0.0000007));
    	System.out.println(SmartFormat.format(0.000006));
    	System.out.println(SmartFormat.format(0.00005));
    	System.out.println(SmartFormat.format(0.0004));
    	System.out.println(SmartFormat.format(0.003));
    	System.out.println(SmartFormat.format(0.02));
    	System.out.println(SmartFormat.format(0.1));
    	System.out.println(SmartFormat.format(1.1));
    	System.out.println(SmartFormat.format(10.12));
    	System.out.println(SmartFormat.format(100.123));
    	System.out.println(SmartFormat.format(1000.1234));
    	System.out.println(SmartFormat.format(10000.12345));
	}

}
