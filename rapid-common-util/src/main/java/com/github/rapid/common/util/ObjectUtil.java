package com.github.rapid.common.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author badqiu
 */
public class ObjectUtil {

	public static boolean isNullOrEmptyString(Object o) {
		if(o == null)
			return true;
		if(o instanceof String) {
			String str = (String)o;
			if(str.length() == 0)
				return true;
		}
		return false;
	}
	
	/**
	 * 可以用于判断 Map,Collection,String,Array是否为空
	 * @param o
	 * @return
	 */
	@SuppressWarnings("all")
    public static boolean isEmpty(Object o)  {
        if(o == null) return true;

        if(o instanceof String) {
            String str = (String)o;
			if(StringUtils.isBlank(str)){
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
        }

        return false;
    }
	
	/**
	 * 得到Array,Collection,Map的大小
	 * @param o
	 * @return
	 */
    public static int getSize(Object o)  {
        if(o == null) return 0;

        if(o instanceof Collection) {
            return ((Collection)o).size();
        } else if(o.getClass().isArray()) {
            return Array.getLength(o);
        } else if(o instanceof Map) {
            return ((Map)o).size();
        }else {
            return 1;
        }
    }
    
	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object c) throws IllegalArgumentException{
		return !isEmpty(c);
	}
	
	public static void flushAll(Object... items) throws IOException {
		if(items == null) return;
		
		for(Object item : items) {
			if(item == null) continue;
			
			if(item instanceof Flushable) {
				((Flushable)item).flush();
			}
		}
	}
	
	public static void closeAll(Object... items) throws Exception {
		if(items == null) return;
		
		for(Object item : items) {
			if(item == null) continue;
			
			if(item instanceof AutoCloseable) {
				((AutoCloseable)item).close();
			}else if(item instanceof Closeable) {
				((Closeable)item).close();
			}
		}
	}
	
	public static void closeQuietlyAll(Object... items)  {
		if(items == null) return;
		
		for(Object item : items) {
			if(item == null) continue;
			
			try {
				if(item instanceof AutoCloseable) {
					((AutoCloseable)item).close();
				}else if(item instanceof Closeable) {
					((Closeable)item).close();
				}
			}catch(Exception e) {
				//ignore
			}
		}
	}
	
	public static void afterPropertiesSetAll(Object... items) throws Exception {
		if(items == null) return;
		
		for(Object item : items) {
			if(item == null) continue;
			
			if(item instanceof AutoCloseable) {
				((InitializingBean)item).afterPropertiesSet();
			}
		}
	}
	
	public static void destroyAll(Object... items) throws Exception {
		if(items == null) return;
		
		for(Object item : items) {
			if(item == null) continue;
			
			if(item instanceof DisposableBean) {
				((DisposableBean)item).destroy();
			}
		}
	}
	
}
