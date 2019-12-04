package com.github.rapid.common.jdbc.sqlgenerator;

import com.github.rapid.common.jdbc.sqlgenerator.metadata.Table;

public class CacheSqlGenerator implements SqlGenerator{
	private static final String NULL = new String();
	private SqlGenerator delegate;

	private String columnsSql = null;
	private String deleteByPkSql = null;
	private String insertSql = null;
	private String selectByPkSql = null;
	private String updateByPkSql = null;
	private String selectFromSql = null;
	
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
		selectFromSql = delegate.getSelectFromSql();
	}

	public String getColumnsSql() {
		return columnsSql;
	}

	public String getDeleteByPkSql() {
		return deleteByPkSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public String getSelectByPkSql() {
		return selectByPkSql;
	}

	public String getUpdateByPkSql() {
		return updateByPkSql;
	}

	public Table getTable() {
		return delegate.getTable();
	}

	public String getColumnsSql(String columnPrefix) {
		return delegate.getColumnsSql(columnPrefix);
	}

	@Override
	public String getSelectFromSql() {
		return selectFromSql;
	}

}
