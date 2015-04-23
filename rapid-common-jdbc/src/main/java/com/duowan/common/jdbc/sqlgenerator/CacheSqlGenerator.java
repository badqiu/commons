package com.duowan.common.jdbc.sqlgenerator;

import com.duowan.common.jdbc.sqlgenerator.metadata.Table;

public class CacheSqlGenerator implements SqlGenerator{
	private static final String NULL = new String();
	private SqlGenerator delegate;

	public CacheSqlGenerator(SqlGenerator delegate) {
		super();
		this.delegate = delegate;
	}

	private String columnsSql = NULL;
	public String getColumnsSql() {
		if(columnsSql == NULL) {
			columnsSql = delegate.getColumnsSql();
		}
		return columnsSql;
	}

	private String deleteByPkSql = NULL;
	public String getDeleteByPkSql() {
		if(deleteByPkSql == NULL) {
			deleteByPkSql = delegate.getDeleteByPkSql();
		}
		return deleteByPkSql;
	}

	private String insertSql = NULL;
	public String getInsertSql() {
		if(insertSql == NULL) {
			insertSql = delegate.getInsertSql();
		}
		return insertSql;
	}

	private String selectByPkSql = NULL;
	public String getSelectByPkSql() {
		if(selectByPkSql == NULL) {
			selectByPkSql = delegate.getSelectByPkSql();
		}
		return selectByPkSql;
	}

	private String updateByPkSql = NULL;
	public String getUpdateByPkSql() {
		if(updateByPkSql == NULL) {
			updateByPkSql = delegate.getUpdateByPkSql();
		}
		return updateByPkSql;
	}

	public Table getTable() {
		return delegate.getTable();
	}

	public String getColumnsSql(String columnPrefix) {
		return delegate.getColumnsSql(columnPrefix);
	}

}
