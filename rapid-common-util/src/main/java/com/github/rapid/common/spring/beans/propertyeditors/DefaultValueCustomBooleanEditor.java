package com.github.rapid.common.spring.beans.propertyeditors;

import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.util.StringUtils;

/**
 * 
 * 为java原生数据类型byte,short,int,long,float,double设置值时将 null值默认转换为0,而不是抛NullPointerException
 * 
 * @author badqiu
 *
 */
public class DefaultValueCustomBooleanEditor extends
		CustomBooleanEditor {
	
	public DefaultValueCustomBooleanEditor(boolean allowEmpty) {
		super(allowEmpty);
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.hasText(text)) {
			super.setAsText(text);
		} else {
			setValue(false);
		}
	}
	
	@Override
	public void setValue(Object value) {
		if(value == null) {
			super.setValue(false);
		}else {
			super.setValue(value);
		}
	}
}