package com.github.rapid.common.json.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializerBase;

/**
 * long值大于javascript的最大值时，进行toString()操作，防止数值溢出
 * 
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class BigintToStringSerializer extends ToStringSerializerBase {
	
    public static long MAX_SAFE_INTEGER = 9007199254740991L;
    public static long MIN_SAFE_INTEGER = -9007199254740991L;
    
    /**
     * Singleton instance to use.
     */
    public final static BigintToStringSerializer instance = new BigintToStringSerializer();

    /**
     *<p>
     * Note: usually you should NOT create new instances, but instead use
     * {@link #instance} which is stateless and fully thread-safe. However,
     * there are cases where constructor is needed; for example,
     * when using explicit serializer annotations like
     * {@link com.fasterxml.jackson.databind.annotation.JsonSerialize#using}.
     */
    public BigintToStringSerializer() { 
    	super(Object.class); 
    }

    /**
     * Sometimes it may actually make sense to retain actual handled type.
     * 
     * @since 2.5
     */
    public BigintToStringSerializer(Class<?> handledType) {
        super(handledType);
    }


    
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
        throws IOException
    {
        Object v = smartValueToString(value);
        if(v == null) {
        	gen.writeNull();
        }if(v instanceof String) {
        	gen.writeString((String)v);
        }else if(v instanceof Long) {
        	gen.writeNumber((Long)value);
        }else {
        	throw new UnsupportedOperationException("unsupport value:"+v+" value class:"+v.getClass());
//        	gen.writeObject(v);
        }
    }
    
    @Override
    public boolean isEmpty(SerializerProvider prov, Object value) {
    	if(value == null) return true;
    	return false;
    }
    
    public final Object smartValueToString(Object value) {
    	if(value == null) return null;
    	
    	if(value instanceof Long) {
    		long num = (Long)value;
    		if(num >= MAX_SAFE_INTEGER || num <=MIN_SAFE_INTEGER) {
    			return value.toString();
    		}
    	}
    	
        return value;
    }

	@Override
	public String valueToString(Object value) {
		throw new RuntimeException("not yet impl");
	}
}
