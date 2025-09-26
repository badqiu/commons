package com.github.rapid.common.util;

import java.util.List;

import org.junit.Test;

import com.github.rapid.common.testbean.TestBeanForArrayUtil;
import com.github.rapid.common.util.BeanChangeUtil.FieldChangeRecord;

public class BeanChangeUtilTest {

	@Test
	public void test() {
		TestBeanForArrayUtil bean1 = new TestBeanForArrayUtil();
		bean1.setName("jane");
		TestBeanForArrayUtil bean2 = bean1.clone();
		bean2.setName("chaos");
		bean2.setMoney(100.0);
		List<FieldChangeRecord> changes = BeanChangeUtil.compareObjects(bean1, bean2);
		
		printList("TestBeanForArrayUtil修改日志",changes);
	}

	private void printList(String info,List<FieldChangeRecord> changes) {
		System.out.println(info);
		for(Object item : changes) {
			System.out.println(item);
		}
	}

}
