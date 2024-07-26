package com.github.rapid.common.freemarker.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.github.rapid.common.freemarker.FreemarkerTemplateException;

import freemarker.cache.TemplateLoader;

/**
 * 用于freemarker从数据库装载template文件
 * 
 * 
 * 属性值配置实例,将生成如下的load sql:
 * <pre>
 * //select template_content from template where template_name=?
 * DataSourceTemplateLoader loader = new DataSourceTemplateLoader();
 * loader.setDataSource(ds);
 * loader.setTableName("freemarker_template");
 * loader.setTemplateNameColumn("template_name");
 * loader.setTemplateContentColumn("template_content");
 * loader.setTimestampColumn("update_time");
 * </pre>
 * 
 * mysql的表创建语句:
 * <pre>
 * CREATE TABLE freemarker_template (
 *  id bigint PRIMARY KEY,
 *  template_name varchar(255) ,
 *  template_content text ,
 *  update_time timestamp 
 *)
 * </pre>
 * @author badqiu
 *
 */
public class DataSourceTemplateLoader implements TemplateLoader,InitializingBean{
	Log log = LogFactory.getLog(DataSourceTemplateLoader.class);
	
	private JdbcTemplate jdbcTemplate;
	
    private String tableName;
    private String templateNameColumn;
    private String templateContentColumn;
    private String timestampColumn;

	public void setDataSource(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTemplateNameColumn(String column) {
		this.templateNameColumn = column;
	}

	public void setTemplateContentColumn(String templateContentColumn) {
		this.templateContentColumn = templateContentColumn;
	}

	public void setTimestampColumn(String timestampColumn) {
		this.timestampColumn = timestampColumn;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	public static DataSourceTemplateLoader buildDefault() {
		DataSourceTemplateLoader r = new DataSourceTemplateLoader();
		r.setTableName("freemarker_template");
		r.setTemplateNameColumn("template_name");
		r.setTemplateContentColumn("template_content");
		r.setTimestampColumn("update_time");
		return r;
	}
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(jdbcTemplate,"'dataSource' must be not null");
		Assert.hasText(tableName,"'tableName' must be not blank");
		Assert.hasText(templateNameColumn,"'templateNameColumn' must be not blank");
		Assert.hasText(templateContentColumn,"'templateContentColumn' must be not blank");
		log.info("Freemarker template load sql:"+getSql(templateContentColumn));
	}

	public Object findTemplateSource(final String name) throws IOException {
		int count = getJdbcTemplate().queryForObject(getSql("count(*)"),new Object[]{name},Integer.class);
		return count > 0 ? name : null;
	}

	private String getSql(String columnNames) {
		return "select "+columnNames+" from "+tableName+" where "+templateNameColumn+"=?";
	}

	@SuppressWarnings("unchecked")
	public Reader getReader(Object templateSource, final String encoding) throws IOException {
		final String templateName = (String)templateSource;
		return (Reader)getJdbcTemplate().query(getSql(templateContentColumn),new Object[]{templateName}, new ResultSetExtractor() {
			public Reader extractData(ResultSet rs) throws SQLException,DataAccessException {
				while(rs.next()) {
					try {
						Object obj = rs.getObject(templateContentColumn);
						if(obj instanceof String) {
							return new StringReader((String)obj);
						}else if(obj instanceof Clob) {
							return new StringReader(rs.getString(templateContentColumn));
						}else if(obj instanceof InputStream) {
							return new InputStreamReader((InputStream)obj,encoding);
						}else if(obj instanceof Blob) {
							return new InputStreamReader(rs.getBinaryStream(templateContentColumn),encoding);
						}else {
							throw new FreemarkerTemplateException("error sql type of templateContentColumn:"+templateContentColumn);
						}
					} catch (UnsupportedEncodingException e) {
						throw new FreemarkerTemplateException("load template from dataSource with templateName:"+templateName+" occer UnsupportedEncodingException",e);
					}
				}
				
				throw new FreemarkerTemplateException("not found template from dataSource with templateName:"+templateName);
			}
		});
	}

	public long getLastModified(Object templateSource) {
		if(StringUtils.hasText(timestampColumn)) {
			String templateName = (String)templateSource;
	    	Object timestamp = getJdbcTemplate().queryForObject(getSql(timestampColumn),new Object[]{templateName},new SingleColumnRowMapper());
	    	if(timestamp instanceof Number) {
	    		return ((Number)timestamp).longValue();
	    	}else if(timestamp instanceof Date) {
	    		return ((Date)timestamp).getTime();
	    	}else {
	    		throw new FreemarkerTemplateException("error template timestamp column type,must be Date or Number type");
	    	}
		}
		return -1;
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
	}

}
