package com.github.rapid.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.thirdparty.com.google.common.base.Objects;

/**
 * 对象变更比较工具
 */
public class BeanChangeUtil {
    
    /**
     * 比较两个相同类型对象的差异
     */
    public static List<FieldChangeRecord> compareObjects(Object oldObj, Object newObj) 
            {
        
        if (!oldObj.getClass().equals(newObj.getClass())) {
            throw new IllegalArgumentException("对象类型必须相同");
        }
        
        List<FieldChangeRecord> changes = new ArrayList<>();
        Field[] fields = oldObj.getClass().getDeclaredFields();
        
        for (Field field : fields) {
        	try {
            // 只处理有TrackChange注解的字段
//            if (field.isAnnotationPresent(TrackChange.class)) {
                field.setAccessible(true);
                
                Object oldValue = field.get(oldObj);
                Object newValue = field.get(newObj);
                
                // 检查值是否发生变化
                if (isValueChanged(oldValue, newValue)) {
//                    TrackChange annotation = field.getAnnotation(TrackChange.class);
//                    String displayName = annotation.name().isEmpty() ? field.getName() : annotation.name();
                    String displayName = field.getName();
                    
                    // 值转换
                    oldValue = convertValue(oldValue);
                    newValue = convertValue(newValue);
                    
                    FieldChangeRecord fieldChangeRecord = new FieldChangeRecord(field.getName(), displayName, oldValue, newValue);
					changes.add(fieldChangeRecord);
                }
//            }
        	}catch(Exception e) {
        		throw new RuntimeException("field error:"+field.getName()+" on Object:"+oldObj,e);
        	}
        }
        
        return changes;
    }
    
    public static boolean isValueChanged(Object oldValue, Object newValue) {
        if (oldValue == null && newValue == null) {
            return false;
        }
        if (oldValue == null || newValue == null) {
            return true;
        }
        return !Objects.equal(oldValue, newValue);
    }
    
    private static Object convertValue(Object value) { // TrackChange annotation) {
        if (value == null) {
            return "空";
        }
        
        // 日期类型格式化
        if (value instanceof Date) {
            return DateConvertUtil.format((Date)value, DateFormats.DATE_TIME_FORMAT);
        }
        
        // 值映射转换
//        if (!annotation.valueMap().isEmpty()) {
//            String[] mappings = annotation.valueMap().split(",");
//            for (String mapping : mappings) {
//                String[] keyValue = mapping.split(":");
//                if (keyValue.length == 2 && keyValue[0].equals(value.toString())) {
//                    return keyValue[1];
//                }
//            }
//        }
        
        return value;
    }
    
    

    /**
     * 字段变更记录实体
     */
    public static class FieldChangeRecord {
        private String fieldName;
        private String fieldDisplayName;
        private Object oldValue;
        private Object newValue;

        public FieldChangeRecord(String fieldName, String fieldDisplayName, 
                               Object oldValue, Object newValue) {
            this.fieldName = fieldName;
            this.fieldDisplayName = fieldDisplayName;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        // Getter和Setter方法
        public String getFieldName() { return fieldName; }
        public void setFieldName(String fieldName) { this.fieldName = fieldName; }
        
        public String getFieldDisplayName() { return fieldDisplayName; }
        public void setFieldDisplayName(String fieldDisplayName) { this.fieldDisplayName = fieldDisplayName; }
        
        public Object getOldValue() { return oldValue; }
        public void setOldValue(Object oldValue) { this.oldValue = oldValue; }
        
        public Object getNewValue() { return newValue; }
        public void setNewValue(Object newValue) { this.newValue = newValue; }
        
        @Override
        public String toString() {
            return String.format("%s: %s -> %s", 
                fieldDisplayName, oldValue, newValue);
        }
    }
    
}