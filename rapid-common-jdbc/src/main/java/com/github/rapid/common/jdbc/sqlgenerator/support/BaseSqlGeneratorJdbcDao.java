package com.github.rapid.common.jdbc.sqlgenerator.support;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DB2SequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

import com.github.rapid.common.jdbc.sqlgenerator.CacheSqlGenerator;
import com.github.rapid.common.jdbc.sqlgenerator.SpringNamedSqlGenerator;
import com.github.rapid.common.jdbc.sqlgenerator.SqlGenerator;
import com.github.rapid.common.jdbc.sqlgenerator.metadata.Column;
import com.github.rapid.common.jdbc.sqlgenerator.metadata.MetadataCreateUtils;
import com.github.rapid.common.jdbc.sqlgenerator.metadata.Table;

/**
 * 基于SqlGenerator及Spring的jdbc基类. 通过该类，只要子类返回 
 * getEntityClass()方法，即可生成jdbc 增删改查代码
 * 
 * 
 * @author badqiu
 *
 */
public abstract class BaseSqlGeneratorJdbcDao<E,PK extends Serializable> extends JdbcDaoSupport {

	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	//根据table对象可以创建生成增删改查的sql的工具
	protected Table table;
	protected SqlGenerator sqlGenerator;
	
	protected Class<E> entityClass = null;
	
	@SuppressWarnings("all")
	public BaseSqlGeneratorJdbcDao() {
		entityClass = (Class)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		table = MetadataCreateUtils.createTable(getEntityClass());
		sqlGenerator = new CacheSqlGenerator(new SpringNamedSqlGenerator(table));
	}
	
	public Class<E> getEntityClass() {
		return entityClass;
	}
	
	protected void checkDaoConfig() {
		super.checkDaoConfig();
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
	}
	
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
	
	public Object getIdentifierPropertyValue(Object entity) {
		try {
			return PropertyUtils.getProperty(entity, getIdentifierPropertyName());
		} catch (Exception e) {
			throw new IllegalStateException("cannot get property value on entityClass:"+entity.getClass()+" by propertyName:"+getIdentifierPropertyName(),e);
		}
	}
	
	public void setIdentifierProperty(Object entity, Object id) {
		try {
			BeanUtils.setProperty(entity, getIdentifierPropertyName(), id);
		} catch (Exception e) {
			throw new IllegalStateException("cannot set property value:"+id+" on entityClass:"+entity.getClass()+" by propertyName:"+getIdentifierPropertyName(),e);
		}
	}
	
	/**
	 * 根据sqlserver,mysql自动生成ID插入数据,生成的ID会返回在entity属性中
	 */
	protected int insertWithIdentity(Object entity) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = getNamedParameterJdbcTemplate().update(getSqlGenerator().getInsertSql(), new BeanPropertySqlParameterSource(entity) , keyHolder);
		setIdentifierProperty(entity, keyHolder.getKey().longValue());
		return result;
	}
	
	/**
	 * 根据 mysql auto_incremnt自动生成ID插入数据,生成的ID会返回在entity属性中
	 * @param entity
	 * @param insertSql
	 * @return the number of rows affected
	 */
	protected int insertWithAutoIncrement(Object entity) {
		return insertWithIdentity(entity);
	}
	
	/**
	 * 
	 * @param entity
	 * @param sequenceIncrementer
	 * @param insertSql
	 * @return the number of rows affected
	 */
	protected int insertWithSequence(Object entity,AbstractSequenceMaxValueIncrementer sequenceIncrementer) {
		Long id = sequenceIncrementer.nextLongValue();
		setIdentifierProperty(entity, id);
		return getNamedParameterJdbcTemplate().update(getSqlGenerator().getInsertSql(), new BeanPropertySqlParameterSource(entity));
	}
	/**
	 * 根据db2 sequence生成ID插入数据,生成的ID会返回在entity属性中
	 * @param entity
	 * @param sequenceName
	 * @param insertSql
	 * @return the number of rows affected
	 */
	protected int insertWithDB2Sequence(Object entity,String sequenceName) {
		return insertWithSequence(entity, new DB2SequenceMaxValueIncrementer(getDataSource(),sequenceName));
	}
	
	/**
	 * 根据oracle sequence生成ID插入数据,生成的ID会返回在entity属性中
	 * @param entity
	 * @param sequenceName
	 * @param insertSql
	 * @return the number of rows affected
	 */
	protected int insertWithOracleSequence(Object entity,String sequenceName) {
		return insertWithSequence(entity, new OracleSequenceMaxValueIncrementer(getDataSource(),sequenceName));
	}
	
	/**
	 * 根据UUID生成ID插入数据,生成的ID会返回在entity属性中
	 * @param entity
	 * @param insertSql
	 * @return the number of rows affected
	 */
	protected int insertWithUUID(Object entity) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		setIdentifierProperty(entity, uuid);
		return getNamedParameterJdbcTemplate().update(getSqlGenerator().getInsertSql(), new BeanPropertySqlParameterSource(entity));
	}
	/**
	 * 手工分配ID插入
	 * @param entity
	 * @param insertSql
	 */
	protected int insertWithAssigned(Object entity) {
		return getNamedParameterJdbcTemplate().update(getSqlGenerator().getInsertSql(), new BeanPropertySqlParameterSource(entity));
	}

	public int insert(E entity) {
		if(table.isHasGeneratedPk()) {
			return insertWithAutoIncrement(entity);
		}else {
			return insertWithAssigned(entity);
		}
	}
	
	@SuppressWarnings("all")
	public E getById(PK id) {
		List list = null;
		if(getSqlGenerator().getTable().getPrimaryKeyCount() > 1) {
			list = getNamedParameterJdbcTemplate().query(getSqlGenerator().getSelectByPkSql(), new BeanPropertySqlParameterSource(id), new BeanPropertyRowMapper(getEntityClass()));
		}else if(getSqlGenerator().getTable().getPrimaryKeyCount() == 1){
			list = getJdbcTemplate().query(getSqlGenerator().getSelectByPkSql(), new Object[]{id},new BeanPropertyRowMapper(getEntityClass()));
		}else {
			throw new IllegalStateException("not found primary key on table:"+getSqlGenerator().getTable().getTableName());
		}
		return (E)DataAccessUtils.singleResult(list);
	}

	public int deleteById(PK id) {
		if(getSqlGenerator().getTable().getPrimaryKeyCount() > 1) {
			return getNamedParameterJdbcTemplate().update(getSqlGenerator().getDeleteByPkSql(),new BeanPropertySqlParameterSource(id));
		}else if(getSqlGenerator().getTable().getPrimaryKeyCount() == 1){
			return getJdbcTemplate().update(getSqlGenerator().getDeleteByPkSql(), id);
		}else {
			throw new IllegalStateException("not found primary key on table:"+getSqlGenerator().getTable().getTableName());
		}
	}
	
	public int update(E entity) {
		String sql = getSqlGenerator().getUpdateByPkSql();
		return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}
	
	
	/**
	 * 得到生成增删改查的sql生成工具
	 * @return
	 */
	public SqlGenerator getSqlGenerator() {
		return sqlGenerator;
	}
	
	/**
	 * 得到主键字段
	 * @return
	 */
	public String getIdentifierPropertyName() {
		List<Column> primaryKeyColumns = getSqlGenerator().getTable().getPrimaryKeyColumns();
		if(primaryKeyColumns.isEmpty()) {
			throw new IllegalStateException("not found primary key on table:"+getSqlGenerator().getTable().getTableName());
		}
		return primaryKeyColumns.get(0).getPropertyName();
	}
	
}