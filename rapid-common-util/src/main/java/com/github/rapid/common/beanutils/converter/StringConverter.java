package com.github.rapid.common.beanutils.converter;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;

public final class StringConverter implements Converter {

	public StringConverter() {
	}

	public Object convert(Class type, Object value) {
		if (value == null || StringUtils.isBlank(value.toString())) {
			return null;
		} else {
			return value.toString();
		}
	}
}

