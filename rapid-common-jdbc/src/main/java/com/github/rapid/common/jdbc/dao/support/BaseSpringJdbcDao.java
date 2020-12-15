package com.github.rapid.common.jdbc.dao.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DB2SequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import com.github.rapid.common.beanutils.BeanUtils;
import com.github.rapid.common.beanutils.PropertyUtils;
import com.github.rapid.common.jdbc.dialect.Dialect;
import com.github.rapid.common.jdbc.sqlgenerator.ExtNamedJdbcTemplate;
import com.github.rapid.common.jdbc.support.OffsetLimitResultSetExtractor;
import com.github.rapid.common.util.MapUtil;
import com.github.rapid.common.util.SqlRemoveUtil;
import com.github.rapid.common.util.page.Page;
import com.github.rapid.common.util.page.PageQuery;
import com.github.rapid.common.util.page.Paginator;
/**
 * Spring的JDBC基类
 * @author badqiu
 *
 */
public abstract class BaseSpringJdbcDao extends JdbcDaoSupport {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected ExtNamedJdbcTemplate extNamedJdbcTemplate;
	
	private Dialect dialect;
	
	protected Class getEntityClass(){
		throw new UnsupportedOperationException("not yet implements");
	}
	
	public void setDialect(Dialect d) {
		this.dialect = d;
	}
	
	@Override
	protected void initTemplateConfig() {
		super.initTemplateConfig();
		if(extNamedJdbcTemplate == null) {
			extNamedJdbcTemplate = new ExtNamedJdbcTemplate(getJdbcTemplate());
		}
	}
	
	public NamedParameterJdbcOperations getNamedParameterJdbcTemplate() {
		return extNamedJdbcTemplate;
	}
	
	public ExtNamedJdbcTemplate getExtNamedJdbcTemplate() {
		return extNamedJdbcTemplate;
	}
	
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcOperations namedParameterJdbcTemplate) {
		this.extNamedJdbcTemplate = new ExtNamedJdbcTemplate(namedParameterJdbcTemplate.getJdbcOperations());
	}

	protected void checkSingleRowAffected(String sql,int rowsAffected) throws JdbcUpdateAffectedIncorrectNumberOfRowsException {
		checkRowAffected(sql,rowsAffected,1);
	}
	
	/**
	 * 检查update调用的rowsAffected必须为正确的行数
	 * @param sql
	 * @param rowsAffected
	 * @param requiredRowsAffected
	 * @throws JdbcUpdateAffectedIncorrectNumberOfRowsException
	 */
	public void checkRowAffected(String sql,int rowsAffected,int requiredRowsAffected) throws JdbcUpdateAffectedIncorrectNumberOfRowsException {
		if (requiredRowsAffected > 0 && rowsAffected != requiredRowsAffected) {
			throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(sql, requiredRowsAffected, rowsAffected);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <E> Page<E> pageQuery(String sql, PageQuery pageQuery,RowMapper<E> rowMapper) {
		Map<String ,Object> paramMap = new HashMap<String ,Object> (PropertyUtils.describe(pageQuery));
		int totalItems = queryTotalItems(sql, paramMap);
		return pageQuery(sql,paramMap,totalItems,pageQuery.getPageSize(),pageQuery.getPage(),rowMapper);
	}
	
	public  <E> Page<E> pageQuery(String sql, Map<String ,Object> paramMap,int pageSize, int pageNumber, RowMapper<E> rowMapper) {
		int totalItems = queryTotalItems(sql, paramMap);
		return pageQuery(sql,paramMap,totalItems,pageSize,pageNumber,rowMapper);
	}
	
	public  <E> List<E> pageQueryForList(String sql, Map<String ,Object> paramMap,int pageSize, int pageNumber, RowMapper<E> rowMapper) {
		Paginator paginator = new Paginator(pageNumber, pageSize, Integer.MAX_VALUE);
		List<E> list = queryForListByStartRowAndPageSize(sql, paramMap,paginator.getOffset(),pageSize,rowMapper);
		return list;
	}
	
	public  <E> List<E> pageQueryForList(String sql, Map<String ,Object> paramMap,PageQuery pageQuery, RowMapper<E> rowMapper) {
		Paginator paginator = new Paginator(pageQuery.getPage(), pageQuery.getPageSize(), Integer.MAX_VALUE);
		List<E> list = queryForListByStartRowAndPageSize(sql, paramMap,paginator.getOffset(),pageQuery.getPageSize(),rowMapper);
		return list;
	}
	
	private <E> Page<E>  pageQuery(String sql, Map<String ,Object> paramMap, final int totalItems,int pageSize, int pageNumber, RowMapper<E> rowMapper) {
		if(totalItems <= 0) {
			return new Page<E>(new Paginator(pageNumber,pageSize,0));
		}
		Paginator paginator = new Paginator(pageNumber, pageSize, totalItems);
		List<E> list = queryForListByStartRowAndPageSize(sql, paramMap,paginator.getOffset(),pageSize,rowMapper);
		return new Page<E>(list,paginator);
	}
	
	private int queryTotalItems(String querySql,Map<String,Object> paramMap) {
		String removedOrderByQuery = "select count(*) from (" + SqlRemoveUtil.removeOrders(querySql) + ") c";//FIXME 未处理group by的 select count(*) from (subquery)
		return queryForInt(removedOrderByQuery,new MapSqlParameterSource(paramMap));
	}
	
	public int queryForInt(String sql, SqlParameterSource paramSource) throws DataAccessException {
		Number number = getNamedParameterJdbcTemplate().queryForObject(sql, paramSource, Number.class);
		return (number != null ? number.intValue() : 0);
	}
	
	static final String LIMIT_PLACEHOLDER = ":__limit";
	static final String OFFSET_PLACEHOLDER = ":__offset";
	@SuppressWarnings("unchecked")
	protected <E> List<E> queryForListByStartRowAndPageSize(String sql, final Map<String,Object> paramMap, int startRow,int pageSize, final RowMapper<E> rowMapper) {
		//支持limit查询
		if(dialect.supportsLimit()) {
			paramMap.put(LIMIT_PLACEHOLDER.substring(1), pageSize);
			
			//支持limit及offset.则完全使用数据库分页
			if(dialect.supportsLimitOffset()) {
				paramMap.put(OFFSET_PLACEHOLDER.substring(1), startRow);
				sql = dialect.getLimitString(sql,startRow,OFFSET_PLACEHOLDER,pageSize,LIMIT_PLACEHOLDER);
				startRow = 0;
			}else {
				//不支持offset,则在后面查询中使用游标配合limit分页
				sql = dialect.getLimitString(sql, 0,null, pageSize,LIMIT_PLACEHOLDER);
			}
			
			pageSize = Integer.MAX_VALUE;
		}
		
		return (List<E>)getNamedParameterJdbcTemplate().query(sql, paramMap, new OffsetLimitResultSetExtractor(startRow,pageSize,rowMapper));
	}
	
	///// insert with start
	/**
	 * 适用sqlserver:identity,mysql:auto_increment 自动生成主键
	 */
	public int insertWithGeneratedKey(Object entity, String insertSql) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int affectedRows = getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity) , keyHolder);
		if (keyHolder.getKey() != null) {
			setIdentifierProperty(entity, keyHolder.getKey().longValue());
		}
		return affectedRows;
	}
	
	public int insertWithIdentity(Object entity,String insertSql) {
		return insertWithGeneratedKey(entity, insertSql);
	}
	
	public int insertWithAutoIncrement(Object entity,String insertSql) {
		return insertWithIdentity(entity,insertSql);
	}

	public int insertWithSequence(Object entity,AbstractSequenceMaxValueIncrementer sequenceIncrementer,String insertSql) {
		Long id = sequenceIncrementer.nextLongValue();
		setIdentifierProperty(entity, id);
		return getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity));
	}
	
	public int insertWithDB2Sequence(Object entity,String sequenceName,String insertSql) {
		return insertWithSequence(entity, new DB2SequenceMaxValueIncrementer(getDataSource(),sequenceName), insertSql);
	}
	
	public int insertWithOracleSequence(Object entity,String sequenceName,String insertSql) {
		return insertWithSequence(entity, new OracleSequenceMaxValueIncrementer(getDataSource(),sequenceName), insertSql);
	}
	
	public int insertWithUUID(Object entity,String insertSql) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		setIdentifierProperty(entity, uuid);
		return getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity));
	}
	/**
	 * 手工分配ID插入
	 * @param entity
	 * @param insertSql
	 */
	public int insertWithAssigned(Object entity,String insertSql) {
		return getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity));
	}
	///// insert with end
	
	/**
	 * 得到主键对应的property
	 */
	protected String getIdentifierPropertyName() {
		throw new UnsupportedOperationException("not yet implements");
	}
	
	/**
	 * 设置实体的主键值
	 */
	public void setIdentifierProperty(Object entity, Object id) {
		try {
			BeanUtils.setProperty(entity, getIdentifierPropertyName(), id);
		} catch (Exception e) {
			throw new IllegalStateException("cannot set property value:"+id+" on entityClass:"+entity.getClass()+" by propertyName:"+getIdentifierPropertyName(),e);
		}
	}
	
	/**
	 * 得到实体的主键值
	 */
	public Object getIdentifierPropertyValue(Object entity) {
		try {
			return PropertyUtils.getProperty(entity, getIdentifierPropertyName());
		} catch (Exception e) {
			throw new IllegalStateException("cannot get property value on entityClass:"+entity.getClass()+" by propertyName:"+getIdentifierPropertyName(),e);
		}
	}
	
	/**
	 * 构建SQL使用
	 * @param sqlSegments
	 * @return
	 */
	public static String sql(String... sqlSegments) {
		return StringUtils.join(sqlSegments," ");
	}
	
	/**
	 * 构建Map工具使用
	 */
	public static Map newMap(Object... args) {
		return MapUtil.newMap(args);
	}
	
}
