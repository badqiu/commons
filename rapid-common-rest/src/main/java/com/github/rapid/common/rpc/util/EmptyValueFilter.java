package com.github.rapid.common.rpc.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.serializer.ValueFilter;

public class EmptyValueFilter implements ValueFilter {

	@Override
	public Object process(Object object, String name, Object value) {
		if(value == null) return null;
		if(isEmpty(value)) {
			return null;
		}
		return value;
	}

	public static boolean isEmpty(Object o)  {
        if(o == null) return true;

        if(o instanceof String) {
            if(((String)o).length() == 0){
                return true;
            }
        } else if(o instanceof Collection) {
            if(((Collection)o).isEmpty()){
                return true;
            }
        } else if(o.getClass().isArray()) {
            if(Array.getLength(o) == 0){
                return true;
            }
        } else if(o instanceof Map) {
            if(((Map)o).isEmpty()){
                return true;
            }
        }else {
            return false;
        }

        return false;
    }
	
}