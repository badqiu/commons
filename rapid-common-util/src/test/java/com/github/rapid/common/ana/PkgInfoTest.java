package com.github.rapid.common.ana;

import java.lang.annotation.Annotation;

import org.junit.Test;

public class PkgInfoTest {

	@Test
	public void test() {
		Package pkg = PkgInfoTest.class.getPackage();
		System.out.println(pkg);
		Annotation[] annotations = pkg.getAnnotations();
		for(Annotation item : annotations) {
			System.out.println(item);
		}
	}

}
