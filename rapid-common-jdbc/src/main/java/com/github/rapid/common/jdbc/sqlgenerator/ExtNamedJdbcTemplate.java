package com.github.rapid.common.jdbc.sqlgenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * 扩展方法 版本的NamedParameterJdbcTemplate，更容易使用
 * 
 * @author badqiu
 *
 */
public class ExtNamedJdbcTemplate extends NamedParameterJdbcTemplate {

	public ExtNamedJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	public ExtNamedJdbcTemplate(JdbcOperations classicJdbcTemplate) {
		super(classicJdbcTemplate);
	}
	
	public int update(String sql, Object paramSource) throws DataAccessException {
		return super.update(sql, new BeanPropertySqlParameterSource(paramSource));
	}
	
	public int update(String sql, Object paramSource,KeyHolder generatedKeyHolder) throws DataAccessException {
		return super.update(sql, new BeanPropertySqlParameterSource(paramSource), generatedKeyHolder);
	}
	
	public int update(String sql, Object paramSource,KeyHolder generatedKeyHolder, String[] keyColumnNames)throws DataAccessException {
		return super.update(sql, new BeanPropertySqlParameterSource(paramSource), generatedKeyHolder, keyColumnNames);
	}

	public <T> T query(String sql, Object paramSource,ResultSetExtractor<T> rse) throws DataAccessException {
		return super.query(sql, new BeanPropertySqlParameterSource(paramSource), rse);
	}
	
	public void query(String sql, Object paramSource,RowCallbackHandler rch) throws DataAccessException {
		super.query(sql, new BeanPropertySqlParameterSource(paramSource), rch);
	}
	
	public <T> List<T> query(String sql, Object paramSource,RowMapper<T> rowMapper) throws DataAccessException {
		return super.query(sql, new BeanPropertySqlParameterSource(paramSource), rowMapper);
	}
	
	public <T> List<T> query(String sql, Object paramSource,Class<?> rowMappedClass) throws DataAccessException {
		return super.query(sql, new BeanPropertySqlParameterSource(paramSource), getBeanPropertyRowMapper(rowMappedClass));
	}

	public <T> T queryOne(String sql, Map paramMap,RowMapper<T> rowMapper) throws DataAccessException {
		return DataAccessUtils.singleResult(super.query(sql, new MapSqlParameterSource(paramMap), rowMapper));
	}
	
	public <T> T queryOne(String sql, Object paramSource,RowMapper<T> rowMapper) throws DataAccessException {
		return DataAccessUtils.singleResult(super.query(sql, new BeanPropertySqlParameterSource(paramSource), rowMapper));
	}
	
	public <T> T queryOne(String sql, Object paramSource,Class<?> rowMappedClass) throws DataAccessException {
		return (T)DataAccessUtils.singleResult(super.query(sql, new BeanPropertySqlParameterSource(paramSource), getBeanPropertyRowMapper(rowMappedClass)));
	}
	
	public SqlRowSet queryForRowSet(String sql, Object paramSource)throws DataAccessException {
		return super.queryForRowSet(sql, new BeanPropertySqlParameterSource(paramSource));
	}
	
	public List<Map<String, Object>> queryForList(String sql,Object paramSource) throws DataAccessException {
		return super.queryForList(sql, new BeanPropertySqlParameterSource(paramSource));
	}
	
	public <T> List<T> queryForList(String sql, Object paramSource,Class<T> elementType) throws DataAccessException {
		return super.queryForList(sql, new BeanPropertySqlParameterSource(paramSource), elementType);
	}
	
	public int[] batchUpdate(String sql, Object[] batchArgs) {
		if (batchArgs == null || batchArgs.length <= 0) {
			return new int[] {0};
		}
		
		BeanPropertySqlParameterSource[] args = new BeanPropertySqlParameterSource[batchArgs.length];
		for(int i = 0; i < batchArgs.length; i++) {
			Object object = batchArgs[i];
			args[i] = new BeanPropertySqlParameterSource(object);
		}
		return super.batchUpdate(sql, args);
	}
	
	public <T> T execute(String sql, Object paramSource,PreparedStatementCallback<T> action) throws DataAccessException {
		return super.execute(sql, new BeanPropertySqlParameterSource(paramSource), action);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<Class,BeanPropertyRowMapper> beanPropertyRowMapperCache = new HashMap();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected BeanPropertyRowMapper getBeanPropertyRowMapper(Class rowMappedClass) {
		BeanPropertyRowMapper result = beanPropertyRowMapperCache.get(rowMappedClass);
		if(result == null) {
			result = new BeanPropertyRowMapper(rowMappedClass);
			synchronized (beanPropertyRowMapperCache) {
				beanPropertyRowMapperCache.put(rowMappedClass, result);
			}
		}
		return result;
	}

}
