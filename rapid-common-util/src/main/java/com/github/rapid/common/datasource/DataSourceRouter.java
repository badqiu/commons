package com.github.rapid.common.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 多数据源router,可以根据ThreadLocal传递的名称，切换使用不同的数据源
 */
public class DataSourceRouter implements DataSource,InitializingBean{

	private DataSource masterDataSource;
	private Map<String,DataSource> dataSourceMap;
	
	private String masterName = "master";
	
	// for sql query dataSource
//	private List<DataSource> readDataSourceList = new ArrayList<DataSource>();
	
	public Map<String, DataSource> getDataSourceMap() {
		return dataSourceMap;
	}

	public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public DataSource getMasterDataSource() {
		return masterDataSource;
	}
	
	public void setMasterDataSource(DataSource masterDataSource) {
		this.masterDataSource = masterDataSource;
	}

	public void init() {
		Assert.notEmpty(dataSourceMap,"dataSourceMap must be not empty");
		setMasterDataSource(dataSourceMap.get(masterName));
	}

	public Connection getConnection() throws SQLException {
		return getRealDataSource().getConnection();
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return getRealDataSource().getConnection(username, password);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return getRealDataSource().getLogWriter();
	}

	public int getLoginTimeout() throws SQLException {
		return getRealDataSource().getLoginTimeout();
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return getRealDataSource().getParentLogger();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return getRealDataSource().isWrapperFor(iface);
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		getRealDataSource().setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		getRealDataSource().setLoginTimeout(seconds);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return getRealDataSource().unwrap(iface);
	}

	public DataSource getRealDataSource() {
		return getRealDataSource(null);
	}

	public DataSource getRealDataSource(String name) {
		if(name == null) {
			name = DataSourceContextHolder.peek();
		}
		
		if(name == null) {
			return masterDataSource;
		}
		
		DataSource result = dataSourceMap.get(name);
		if(result == null) {
			result = masterDataSource;
		}
		
		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	
}
