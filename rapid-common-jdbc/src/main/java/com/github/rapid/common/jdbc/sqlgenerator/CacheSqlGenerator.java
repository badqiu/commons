package com.github.rapid.common.jdbc.sqlgenerator;

import com.github.rapid.common.jdbc.sqlgenerator.metadata.Table;

public class CacheSqlGenerator implements SqlGenerator{
	private static final String NULL = new String();
	private SqlGenerator delegate;

	public CacheSqlGenerator(SqlGenerator delegate) {
		setSqlGenerator(delegate);
	}

	private void setSqlGenerator(SqlGenerator delegate) {
		this.delegate = delegate;
		
		columnsSql = delegate.getColumnsSql();
		deleteByPkSql = delegate.getDeleteByPkSql();
		insertSql = delegate.getInsertSql();
		selectByPkSql = delegate.getSelectByPkSql();
		updateByPkSql = delegate.getUpdateByPkSql();
	}

	private String columnsSql = null;
	public String getColumnsSql() {
		return columnsSql;
	}

	private String deleteByPkSql = null;
	public String getDeleteByPkSql() {
		return deleteByPkSql;
	}

	private String insertSql = null;
	public String getInsertSql() {
		return insertSql;
	}

	private String selectByPkSql = null;
	public String getSelectByPkSql() {
		return selectByPkSql;
	}

	private String updateByPkSql = null;
	public String getUpdateByPkSql() {
		return updateByPkSql;
	}

	public Table getTable() {
		return delegate.getTable();
	}

	public String getColumnsSql(String columnPrefix) {
		return delegate.getColumnsSql(columnPrefix);
	}

}
