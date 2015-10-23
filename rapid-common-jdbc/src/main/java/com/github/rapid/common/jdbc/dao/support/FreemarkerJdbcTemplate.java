package com.github.rapid.common.jdbc.dao.support;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.Assert;

import com.github.rapid.common.freemarker.loader.MapTemplateLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerJdbcTemplate extends PropertiesLoaderSupport implements NamedParameterJdbcOperations,InitializingBean{

	private Properties sqlMap = new Properties();
	private Configuration conf = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public FreemarkerJdbcTemplate() {
	}
	
	public FreemarkerJdbcTemplate(NamedParameterJdbcTemplate proxy) {
		setNamedParameterJdbcTemplate(proxy);
	}
	
	public FreemarkerJdbcTemplate(JdbcOperations classicJdbcTemplate) {
		setJdbcTemplate(classicJdbcTemplate);
	}
	
	public FreemarkerJdbcTemplate(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public void setJdbcTemplate(JdbcOperations classicJdbcTemplate) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(classicJdbcTemplate);
	}
	
	public void setSqlMap(Properties sqlMap) {
		this.sqlMap = sqlMap;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public String getSql(String sqlId) {
		return getSql(sqlId,null);
	}
	
	public String getSql(String sqlId, Object params) {
		Template t = null;
		try {
			StringWriter out = new StringWriter(100);
			t = conf.getTemplate(sqlId);
			t.process(params, out);
			return out.toString();
		} catch (Exception e) {
			throw new RuntimeException("process template error,id:" + sqlId + " params:" + params + " template:" + t,e);
		}
	}
	
	public JdbcOperations getJdbcOperations() {
		return namedParameterJdbcTemplate.getJdbcOperations();
	}

	public void setCacheLimit(int cacheLimit) {
		namedParameterJdbcTemplate.setCacheLimit(cacheLimit);
	}

	public int getCacheLimit() {
		return namedParameterJdbcTemplate.getCacheLimit();
	}

	public <T> T execute(String sql, SqlParameterSource paramSource,
			PreparedStatementCallback<T> action) throws DataAccessException {
		return namedParameterJdbcTemplate.execute(getSql(sql), paramSource, action);
	}

	public <T> T execute(String sql, Map<String, ?> paramMap,
			PreparedStatementCallback<T> action) throws DataAccessException {
		return namedParameterJdbcTemplate.execute(getSql(sql,paramMap), paramMap, action);
	}

	public <T> T execute(String sql, PreparedStatementCallback<T> action)
			throws DataAccessException {
		return namedParameterJdbcTemplate.execute(getSql(sql), action);
	}

	public <T> T query(String sql, SqlParameterSource paramSource,
			ResultSetExtractor<T> rse) throws DataAccessException {
		return namedParameterJdbcTemplate.query(getSql(sql), paramSource, rse);
	}

	public <T> T query(String sql, Map<String, ?> paramMap,
			ResultSetExtractor<T> rse) throws DataAccessException {
		return namedParameterJdbcTemplate.query(getSql(sql,paramMap), paramMap, rse);
	}

	public <T> T query(String sql, ResultSetExtractor<T> rse)
			throws DataAccessException {
		return namedParameterJdbcTemplate.query(getSql(sql), rse);
	}

	public void query(String sql, SqlParameterSource paramSource,
			RowCallbackHandler rch) throws DataAccessException {
		namedParameterJdbcTemplate.query(getSql(sql), paramSource, rch);
	}

	public void query(String sql, Map<String, ?> paramMap,
			RowCallbackHandler rch) throws DataAccessException {
		namedParameterJdbcTemplate.query(getSql(sql,paramMap), paramMap, rch);
	}

	public void query(String sql, RowCallbackHandler rch)
			throws DataAccessException {
		namedParameterJdbcTemplate.query(getSql(sql), rch);
	}

	public <T> List<T> query(String sql, SqlParameterSource paramSource,
			RowMapper<T> rowMapper) throws DataAccessException {
		return namedParameterJdbcTemplate.query(getSql(sql), paramSource, rowMapper);
	}

	public <T> List<T> query(String sql, Map<String, ?> paramMap,
			RowMapper<T> rowMapper) throws DataAccessException {
		return namedParameterJdbcTemplate.query(getSql(sql,paramMap), paramMap, rowMapper);
	}

	public <T> List<T> query(String sql, RowMapper<T> rowMapper)
			throws DataAccessException {
		return namedParameterJdbcTemplate.query(getSql(sql), rowMapper);
	}

	public <T> T queryForObject(String sql, SqlParameterSource paramSource,
			RowMapper<T> rowMapper) throws DataAccessException {
		return namedParameterJdbcTemplate.queryForObject(getSql(sql), paramSource, rowMapper);
	}

	public <T> T queryForObject(String sql, Map<String, ?> paramMap,
			RowMapper<T> rowMapper) throws DataAccessException {
		return namedParameterJdbcTemplate.queryForObject(getSql(sql,paramMap), paramMap, rowMapper);
	}

	public <T> T queryForObject(String sql, SqlParameterSource paramSource,
			Class<T> requiredType) throws DataAccessException {
		return namedParameterJdbcTemplate.queryForObject(getSql(sql), paramSource, requiredType);
	}

	public <T> T queryForObject(String sql, Map<String, ?> paramMap,
			Class<T> requiredType) throws DataAccessException {
		return namedParameterJdbcTemplate.queryForObject(getSql(sql,paramMap), paramMap, requiredType);
	}

	public Map<String, Object> queryForMap(String sql,
			SqlParameterSource paramSource) throws DataAccessException {
		return namedParameterJdbcTemplate.queryForMap(getSql(sql), paramSource);
	}

	public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap)
			throws DataAccessException {
		return namedParameterJdbcTemplate.queryForMap(getSql(sql,paramMap), paramMap);
	}

	public <T> List<T> queryForList(String sql, SqlParameterSource paramSource,
			Class<T> elementType) throws DataAccessException {
		return namedParameterJdbcTemplate.queryForList(getSql(sql), paramSource, elementType);
	}

	public <T> List<T> queryForList(String sql, Map<String, ?> paramMap,
			Class<T> elementType) throws DataAccessException {
		return namedParameterJdbcTemplate.queryForList(getSql(sql,paramMap), paramMap, elementType);
	}

	public List<Map<String, Object>> queryForList(String sql,
			SqlParameterSource paramSource) throws DataAccessException {
		return namedParameterJdbcTemplate.queryForList(getSql(sql), paramSource);
	}

	public List<Map<String, Object>> queryForList(String sql,
			Map<String, ?> paramMap) throws DataAccessException {
		return namedParameterJdbcTemplate.queryForList(getSql(sql,paramMap), paramMap);
	}

	public SqlRowSet queryForRowSet(String sql, SqlParameterSource paramSource)
			throws DataAccessException {
		return namedParameterJdbcTemplate.queryForRowSet(getSql(sql), paramSource);
	}

	public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap)
			throws DataAccessException {
		return namedParameterJdbcTemplate.queryForRowSet(getSql(sql,paramMap), paramMap);
	}

	public int update(String sql, SqlParameterSource paramSource)
			throws DataAccessException {
		return namedParameterJdbcTemplate.update(getSql(sql), paramSource);
	}

	public int update(String sql, Map<String, ?> paramMap)
			throws DataAccessException {
		return namedParameterJdbcTemplate.update(getSql(sql,paramMap), paramMap);
	}

	public int update(String sql, SqlParameterSource paramSource,
			KeyHolder generatedKeyHolder) throws DataAccessException {
		return namedParameterJdbcTemplate.update(getSql(sql), paramSource, generatedKeyHolder);
	}

	public int update(String sql, SqlParameterSource paramSource,
			KeyHolder generatedKeyHolder, String[] keyColumnNames)
			throws DataAccessException {
		return namedParameterJdbcTemplate.update(getSql(sql), paramSource, generatedKeyHolder,keyColumnNames);
	}

	public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
		return namedParameterJdbcTemplate.batchUpdate(getSql(sql), batchValues);
	}

	public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
		return namedParameterJdbcTemplate.batchUpdate(getSql(sql), batchArgs);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		sqlMap = createProperties();
		conf.setTemplateLoader(new MapTemplateLoader(sqlMap));
		Assert.notNull(namedParameterJdbcTemplate,"namedParameterJdbcTemplate must be not null");
	}

	private Properties createProperties() throws IOException {
		return mergeProperties();
	}

}
