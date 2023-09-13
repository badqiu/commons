package com.github.rapid.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * 搜索class的工具类
 * @author badqiu
 *
 */
public class ScanClassUtil {
	
	public static List<String> scanPackages(String basePackages) throws IllegalArgumentException{
		return scanPackages(basePackages,true);
	}
	
	/**
	 * 根据多个包名搜索class
	 * 例如: ScanClassUtil.scanPakcages("javacommon.**.*");
	 * @param basePackages 各个包名使用逗号分隔,各个包名可以有通配符
	 * @return List包含className
	 */
	@SuppressWarnings("all")
	public static List<String> scanPackages(String basePackages,boolean isIgnoreTestClass) throws IllegalArgumentException{
		Assert.hasText(basePackages,"'basePakcages' must be not null");
		
		String[] arrayPackages = basePackages.split(",");
		
		return scanPackages(arrayPackages,isIgnoreTestClass);
	}

	private static List<String> scanPackages(String[] basePackages,boolean isIgnoreTestClass) {
		ResourcePatternResolver rl = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(rl); 
		List result = new ArrayList();
		try {
			for(int j = 0; j < basePackages.length; j++) {
				String packageToScan = StringUtils.trim(basePackages[j]);
				if(StringUtils.isBlank(packageToScan)) continue;
				
				String packagePart = packageToScan.replace('.', '/');
				String classPattern = "classpath*:/" + packagePart + "/**/*.class";
				Resource[] resources = rl.getResources(classPattern);
				for (int i = 0; i < resources.length; i++) {
					Resource resource = resources[i];
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);   
					String className = metadataReader.getClassMetadata().getClassName();
					
					if(isIgnoreTestClass) {
						if(!isIgnoreClassName(className)) {
							result.add(className);
						}
					}else {
						result.add(className);
					}
				}
			}
		}catch(Exception e) {
			throw new IllegalArgumentException("scan pakcage class error,pakcages:"+StringUtils.join(basePackages,","),e);
		}

		return result;
	}

	public static boolean isIgnoreClassName(String className) {
		String shortClassName = ClassUtils.getShortName(className);
		if(shortClassName.equals("package-info")) {
			return true;
		}
		if(shortClassName.endsWith("Test") || shortClassName.startsWith("Test")) {
			return true;
		}
		String lowerClassName = shortClassName.toLowerCase();
		if(lowerClassName.endsWith("testcase") || lowerClassName.startsWith("testcase")) {
			return true;
		}
		
		return false;
	}
	
}
