package com.github.rapid.common.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

public class XStreamUtil {

	public static XStream newXStream() {
		
		// PureJavaReflectionProvider可以使用类定义的默认值,原生默认值无法生效
		XStream xstream = new XStream(new PureJavaReflectionProvider(),new DomDriver());
		
		xstream.useAttributeFor(int.class);
		xstream.useAttributeFor(long.class);
		xstream.useAttributeFor(char.class);
		xstream.useAttributeFor(float.class);
		xstream.useAttributeFor(boolean.class);
		xstream.useAttributeFor(double.class);
		xstream.useAttributeFor(Integer.class);
		xstream.useAttributeFor(Long.class);
		xstream.useAttributeFor(Character.class);
		xstream.useAttributeFor(Float.class);
		xstream.useAttributeFor(Double.class);
		xstream.useAttributeFor(Boolean.class);
		xstream.useAttributeFor(String.class);
		
		xstream.addPermission(NoTypePermission.NONE); //forbid everything
		xstream.addPermission(NullPermission.NULL);   // allow "null"
		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES); // allow primitive types

		return xstream;
	}
	
}
