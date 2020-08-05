package com.github.rapid.common.spring.beans.propertyeditors;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.util.StringUtils;

/**
 * 
 * 为java原生数据类型byte,short,int,long,float,double设置值时将 null值默认转换为0,而不是抛NullPointerException
 * 
 * @author badqiu
 *
 */
public class DefaultValueCustomNumberEditor extends
		CustomNumberEditor {
	
	public DefaultValueCustomNumberEditor(Class numberClass,
			boolean allowEmpty) throws IllegalArgumentException {
		super(numberClass, allowEmpty);
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.hasText(text)) {
			super.setAsText(text);
		} else {
			setValue(0);
		}
	}
	
	@Override
	public void setValue(Object value) {
		if(value == null) {
			setValue(0);
		}else {
			super.setValue(value);
		}
	}
}