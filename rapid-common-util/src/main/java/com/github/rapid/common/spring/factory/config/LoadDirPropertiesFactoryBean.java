package com.github.rapid.common.spring.factory.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        
        Collection<File> files = FileUtils.listFiles(new File(searchPath), new String[] {PROPERTIES_EXT}, true);
        
        for (File file : files) {
            if (file.exists()) {
            	log.info("loadPropertiesFromDirectory() auto load config:"+file);
                Properties prop = new Properties();
                FileInputStream fileInputStream = null;
                try {
                	fileInputStream = new FileInputStream(file);
                	prop.load(fileInputStream);
                	combinedProps.putAll(prop);
                }finally {
                	IOUtils.closeQuietly(fileInputStream);
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