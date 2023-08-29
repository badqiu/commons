package com.github.rapid.common.jdbc.sqlgenerator.metadata;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang.StringUtils;
/**
 * 用于生成Table对象实例的工具类
 *
 * @see Table
 * @author badqiu
 *
 */
public class MetadataCreateUtils {

	public static Table createTable(Class clazz) {
		if(clazz == null) throw new IllegalArgumentException("'clazz' must be not null");
		
		BeanInfo info = getBeanInfo(clazz);
		PropertyDescriptor[] pds = info.getPropertyDescriptors();
		List<Column> columns = new ArrayList();
		boolean hasGeneratePk = false;
		for(PropertyDescriptor pd : pds) {
			String propertyName = pd.getName();
			
			if("class".equals(propertyName))
				continue;
			Method readMethod = pd.getReadMethod();
			Method writeMethod = pd.getWriteMethod();
			if(readMethod == null || writeMethod == null){
				continue;
			}
			if(isTransientProperty(readMethod,writeMethod)) {
				continue;
			}
			if(!isIncludeJavaType(readMethod.getReturnType())) {
			    continue;
			}
			
			
			
			String sqlName = getColumnSqlName(pd,readMethod,writeMethod);
			boolean isPrimaryKey = isPrimaryKeyColumn(readMethod) || isPrimaryKeyColumn(writeMethod);
			boolean generatedValue = isGeneratedValueColumn(readMethod) || isGeneratedValueColumn(writeMethod);
			boolean insertable = getColumnInsertable(pd, readMethod) && getColumnInsertable(pd,writeMethod);
			boolean updatable = getColumnUpdatable(pd, readMethod) && getColumnUpdatable(pd,writeMethod);
			boolean unique = getColumnUnique(pd, readMethod) || getColumnUnique(pd,writeMethod);
			boolean columnVersion = getColumnVersion(pd, readMethod)|| getColumnVersion(pd,writeMethod);
			boolean nullable = getColumnNullable(pd, readMethod) && getColumnNullable(pd,writeMethod);
			
			Column column = new Column(sqlName,propertyName,isPrimaryKey);
			column.setInsertable(insertable);
			column.setUpdatable(updatable);
			column.setUnique(unique);
			column.setNullable(nullable);
			column.setGeneratedValue(generatedValue);
			column.setVersion(columnVersion);
			
			columns.add(column);
		}

		Table t = new Table(getTableName(clazz),columns);
		return t;
	}

	
	public static Column buildColumnFromField(Class clazz,PropertyDescriptor pd) {
		if(!isJPAClassAvaiable) {
			return null;
		}
		
		Field field = getDeclaredField(clazz, pd);
		if(field != null && Modifier.isTransient(field.getModifiers())) {
			return null;
		}
		
		javax.persistence.Column c = field.getAnnotation(javax.persistence.Column.class);
		boolean isPrimaryKey = field.isAnnotationPresent(Id.class);
		boolean generatedValue = field.isAnnotationPresent(GeneratedValue.class);
		boolean columnVersion = field.isAnnotationPresent(Version.class);
		
		String propertyName = pd.getName();
		String sqlName = c.name();
		if(StringUtils.isBlank(sqlName)) {
			sqlName = toUnderscoreName(propertyName);
		}
		
		Column result = new Column(sqlName,propertyName,isPrimaryKey);
		result.setUnique(c.unique());
		result.setUpdatable(c.updatable());
		result.setInsertable(c.insertable());
		result.setNullable(c.nullable());
		result.setGeneratedValue(generatedValue);
		result.setVersion(columnVersion);
		return result;
	}


	private static Field getDeclaredField(Class clazz, PropertyDescriptor pd) {
		String name = pd.getName();
		try {
			Field field = clazz.getDeclaredField(name);
			return field;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("not found field on class:"+clazz+" field:"+name,e);
		} catch (SecurityException e) {
			throw new RuntimeException("error on field on class:"+clazz+" field:"+name,e);
		}
	}
	


	static boolean isIncludeJavaType(Class clazz) {
	    if(clazz == null) return false;
	    if(clazz.isArray()) return false;
	    
	    if(clazz.isPrimitive() || clazz.getName().startsWith("java.lang") || clazz.getName().startsWith("java.sql") || clazz.getName().startsWith("java.time") ) {
	        return true;
	    }
	    if(includeType.contains(clazz)) {
	        return true;
	    }
	    return false;
    }
	
	private static Set<Class> includeType = new HashSet();
	static {
		includeType.add(Date.class);
		includeType.add(Timestamp.class);
		includeType.add(java.sql.Date.class);
		includeType.add(java.sql.Time.class);
	}
	

    private  static boolean isTransientProperty(Method readMethod,Method writeMethod) {
		if(isJPAClassAvaiable) {
			if(readMethod.isAnnotationPresent(Transient.class)) {
				return true;
			}
			if(writeMethod.isAnnotationPresent(Transient.class)) {
				return true;
			}
		}
		return false;
	}

	private static BeanInfo getBeanInfo(Class clazz) {
		try {
			return Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException("error: generate Table instance from Class,clazz:"+clazz,e);
		}
	}

	private static boolean isJPAClassAvaiable = false;
	static {
		try {
			Class.forName("javax.persistence.Table");
			isJPAClassAvaiable = true;
		} catch (ClassNotFoundException e) {
		}
	}

	private static boolean isPrimaryKeyColumn(Method readMethod) {
		boolean isPrimaryKey = false;
		if(isJPAClassAvaiable) {
			if(readMethod.isAnnotationPresent(Id.class)) {
				isPrimaryKey = true;
			}
		}
		return isPrimaryKey;
	}
	
	private static boolean isGeneratedValueColumn(Method readMethod) {
		boolean result = false;
		if(isJPAClassAvaiable) {
			if(readMethod.isAnnotationPresent(GeneratedValue.class)) {
				result = true;
			}
		}
		return result;
	}

	private static String getColumnSqlName(PropertyDescriptor pd, Method readMethod,Method writeMethod) {
		String sqlName = null;
		if(isJPAClassAvaiable) {
			javax.persistence.Column readAnnColumn = (javax.persistence.Column)readMethod.getAnnotation(javax.persistence.Column.class);
			if(readAnnColumn != null) {
				sqlName = readAnnColumn.name();
			}
			
			if(StringUtils.isBlank(sqlName)) {
				javax.persistence.Column writeAnnColumn = (javax.persistence.Column)writeMethod.getAnnotation(javax.persistence.Column.class);
				if(writeAnnColumn != null) {
					sqlName = writeAnnColumn.name();
				}
			}
		}
		
		if(StringUtils.isBlank(sqlName)) {
			sqlName = toUnderscoreName(pd.getName());
		}
		return sqlName;
	}

	private static boolean getColumnInsertable(PropertyDescriptor pd, Method method) {
		boolean insertable = true;
		if(isJPAClassAvaiable) {
			javax.persistence.Column annColumn = (javax.persistence.Column)method.getAnnotation(javax.persistence.Column.class);
			if(annColumn != null) {
				insertable = annColumn.insertable();
			}
		}
		return insertable;
	}

	private static boolean getColumnUpdatable(PropertyDescriptor pd, Method method) {
		boolean updatable = true;
		if(isJPAClassAvaiable) {
			javax.persistence.Column annColumn = (javax.persistence.Column)method.getAnnotation(javax.persistence.Column.class);
			if(annColumn != null) {
				updatable = annColumn.updatable();
			}
		}
		return updatable;
	}

	private static boolean getColumnNullable(PropertyDescriptor pd, Method method) {
		boolean nullable = true;
		if(isJPAClassAvaiable) {
			javax.persistence.Column annColumn = (javax.persistence.Column)method.getAnnotation(javax.persistence.Column.class);
			if(annColumn != null) {
				nullable = annColumn.nullable();
			}
		}
		return nullable;
	}
	
	private static boolean getColumnUnique(PropertyDescriptor pd, Method method) {
		boolean unique = false;
		if(isJPAClassAvaiable) {
			javax.persistence.Column annColumn = (javax.persistence.Column)method.getAnnotation(javax.persistence.Column.class);
			if(annColumn != null) {
				unique = annColumn.unique();
			}
		}
		return unique;
	}

	private static boolean getColumnVersion(PropertyDescriptor pd, Method method) {
		boolean version = false;
		if(isJPAClassAvaiable) {
			javax.persistence.Version v = (javax.persistence.Version)method.getAnnotation(javax.persistence.Version.class);
			if(v != null) {
				return true;
			}
		}
		return version;
	}
	
	private static String getTableName(Class clazz) {
		String tableName = toUnderscoreName(clazz.getSimpleName());
		if(isJPAClassAvaiable) {
			javax.persistence.Table annTable = (javax.persistence.Table)clazz.getAnnotation(javax.persistence.Table.class);
			if(annTable != null) {
				tableName = annTable.name();
			}
		}
		return tableName;
	}

	private static String toUnderscoreName(String name) {
		if(name == null) return null;

		String filteredName = name;
		if(filteredName.indexOf("_") >= 0 && filteredName.equals(filteredName.toUpperCase())) {
			filteredName = filteredName.toLowerCase();
		}
		if(filteredName.indexOf("_") == -1 && filteredName.equals(filteredName.toUpperCase())) {
			filteredName = filteredName.toLowerCase();
		}

		StringBuffer result = new StringBuffer();
		if (filteredName != null && filteredName.length() > 0) {
			result.append(filteredName.substring(0, 1).toLowerCase());
			for (int i = 1; i < filteredName.length(); i++) {
				String preChart = filteredName.substring(i - 1, i);
				String c = filteredName.substring(i, i + 1);
				if(c.equals("_")) {
					result.append("_");
					continue;
				}
				if(preChart.equals("_")){
					result.append(c.toLowerCase());
					continue;
				}
				if(c.matches("\\d")) {
					result.append(c);
				}else if (c.equals(c.toUpperCase())) {
					result.append("_");
					result.append(c.toLowerCase());
				}
				else {
					result.append(c);
				}
			}
		}
		return result.toString();
	}

}
