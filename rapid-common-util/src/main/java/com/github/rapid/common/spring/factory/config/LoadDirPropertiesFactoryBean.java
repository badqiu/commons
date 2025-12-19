package com.github.rapid.common.spring.factory.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
/**
 * 自动加载某个目录的所有.properties配置文件用于创建Properties对象
 */
public class LoadDirPropertiesFactoryBean extends PropertiesFactoryBean implements EnvironmentAware {
	protected static final Logger log = LoggerFactory.getLogger(LoadDirPropertiesFactoryBean.class);
	
    private ConfigurableEnvironment environment;
	
    private String autoSearchDir;
    private static final String PROPERTIES_EXT = "properties";
    private static final String XML_EXT = "xml";

    @Override
    protected Properties createProperties() throws IOException {
        Properties mergedProps = super.createProperties();
        Properties dirProps = loadPropertiesFromDirectory();
        mergedProps.putAll(dirProps);
        
        pubPropertiesIntoEnvironment(mergedProps);
        return mergedProps;
    }

    private void pubPropertiesIntoEnvironment(Properties mergedProps) {
    	MapPropertySource customSource = new MapPropertySource(getClass().getName(), (Map)mergedProps);
    	MutablePropertySources propertySources = environment.getPropertySources();
    	propertySources.addFirst(customSource); 
	}

	private Properties loadPropertiesFromDirectory() throws IOException {
        Properties combinedProps = new Properties();
        
        String searchPath = environment.resolveRequiredPlaceholders(autoSearchDir);
        
        log.info("loadPropertiesFromDirectory() searchPath:"+searchPath);
        
        Collection<File> files = FileUtils.listFiles(new File(searchPath), new String[] {PROPERTIES_EXT,XML_EXT}, true);
        
        for (File file : files) {
            if (file.exists()) {
            	log.info("loadPropertiesFromDirectory() auto load config:"+file);
                Properties prop = new Properties();
                
                FileReader reader = null;
                InputStream inputStream = null;
                
                try {
                	if(file.getName().endsWith(XML_EXT)) {
                		inputStream = new FileInputStream(file);
                		prop.loadFromXML(inputStream);
                	}else if(file.getName().endsWith(PROPERTIES_EXT)) {
                		reader = new FileReader(file);
                		prop.load(reader);
                	}else {
                		throw new RuntimeException("unsupport file extension:"+file);
                	}
                	
                	combinedProps.putAll(prop);
                }finally {
                	IOUtils.closeQuietly(reader);
                	IOUtils.closeQuietly(inputStream);
                }
            }
        }
        return combinedProps;
    }

    // Setter for Spring injection
    public void setAutoSearchDir(String autoSearchDir) {
        this.autoSearchDir = autoSearchDir;
    }

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = (ConfigurableEnvironment)environment;
	}
    
}