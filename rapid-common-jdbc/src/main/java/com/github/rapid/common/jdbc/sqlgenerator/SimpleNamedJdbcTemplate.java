package com.github.rapid.common.jdbc.sqlgenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * 简货版本的NamedParameterJdbcTemplate
 * 
 * @author badqiu
 *
 */
public class SimpleNamedJdbcTemplate extends NamedParameterJdbcTemplate {

	public SimpleNamedJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	public SimpleNamedJdbcTemplate(JdbcOperations classicJdbcTemplate) {
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
	
	public SqlRowSet queryForRowSet(String sql, Object paramSource)throws DataAccessException {
		return super.queryForRowSet(sql, new BeanPropertySqlParameterSource(paramSource));
	}
	
	public List<Map<String, Object>> queryForList(String sql,Object paramSource) throws DataAccessException {
		return super.queryForList(sql, new BeanPropertySqlParameterSource(paramSource));
	}
	
	public <T> List<T> queryForList(String sql, Object paramSource,Class<T> elementType) throws DataAccessException {
		return super.queryForList(sql, new BeanPropertySqlParameterSource(paramSource), elementType);
	}
	
	@Override
	public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
		return super.batchUpdate(sql, batchArgs);
	}
	
	private Map<Class,BeanPropertyRowMapper> beanPropertyRowMapperCache = new HashMap();
	private BeanPropertyRowMapper getBeanPropertyRowMapper(Class rowMappedClass) {
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
