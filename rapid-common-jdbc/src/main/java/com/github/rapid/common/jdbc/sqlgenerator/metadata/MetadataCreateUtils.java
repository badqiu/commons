package com.github.rapid.common.jdbc.sqlgenerator.metadata;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
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
			if("class".equals(pd.getName()))
				continue;
			Method readMethod = pd.getReadMethod();
			if(readMethod == null || pd.getWriteMethod() == null){
				continue;
			}
			if(isTransientProperty(readMethod,pd.getWriteMethod())) {
				continue;
			}
			if(!isIncludeJavaType(readMethod.getReturnType())) {
			    continue;
			}
			boolean isPrimaryKey = isPrimaryKeyColumn(readMethod);
			boolean generatedValue = isGeneratedValueColumn(readMethod);
			String sqlName = getColumnSqlName(pd,readMethod);
			
			Column column = new Column(sqlName,pd.getName(),isPrimaryKey);
			column.setInsertable(getColumnInsertable(pd, readMethod));
			column.setUpdatable(getColumnUpdatable(pd, readMethod));
			column.setUnique(getColumnUnique(pd, readMethod));
			column.setGeneratedValue(generatedValue);
			column.setVersion(getColumnVersion(pd, readMethod));
			
			columns.add(column);
		}

		Table t = new Table(getTableName(clazz),columns);
		return t;
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

	private static String getColumnSqlName(PropertyDescriptor pd, Method readMethod) {
		String sqlName = null;
		if(isJPAClassAvaiable) {
			javax.persistence.Column annColumn = (javax.persistence.Column)readMethod.getAnnotation(javax.persistence.Column.class);
			if(annColumn != null) {
				sqlName = annColumn.name();
			}
		}
		if(sqlName == null || sqlName.length() == 0) {
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
