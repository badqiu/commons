package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmartFormatTest {

	@Test
	public void test() {
    	System.out.println(SmartFormat.format(0.0000007777));
    	System.out.println(SmartFormat.format(0.000006666));
    	System.out.println(SmartFormat.format(0.00005555));
    	System.out.println(SmartFormat.format(0.0004444));
    	System.out.println(SmartFormat.format(0.0033333));
    	System.out.println(SmartFormat.format(0.02222));
    	System.out.println(SmartFormat.format(0.1111));
    	System.out.println(SmartFormat.format(1.1111));
    	System.out.println(SmartFormat.format(10.12345));
    	System.out.println(SmartFormat.format(100.12345));
    	System.out.println(SmartFormat.format(999.12345));
    	System.out.println(SmartFormat.format(1000.12345));
    	System.out.println(SmartFormat.format(10000.12345));
    	System.out.println(SmartFormat.format(100000.12345));
	}

}
