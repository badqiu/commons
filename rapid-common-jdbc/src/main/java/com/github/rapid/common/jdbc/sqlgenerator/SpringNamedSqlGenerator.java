package com.github.rapid.common.jdbc.sqlgenerator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.github.rapid.common.jdbc.sqlgenerator.metadata.Column;
import com.github.rapid.common.jdbc.sqlgenerator.metadata.Table;


/**
 * spring的命名参数sql生成工具类
 * @see Table
 * @author badqiu
 *
 */
public class SpringNamedSqlGenerator implements SqlGenerator{
	Table table;

	public SpringNamedSqlGenerator(Table table) {
		super();
		this.table = table;
	}

	public List<Column> getColumns() {
		return table.getColumns();
	}

	public String getTableName() {
		return table.getTableName();
	}

	public List<Column> getPrimaryKeyColumns() {
		return table.getPrimaryKeyColumns();
	}

	public Table getTable() {
		return table;
	}

	public boolean isMultiPrimaryKey() {
		return getPrimaryKeyColumns().size() > 1;
	}

	public boolean isSinglePrimaryKey() {
		return getPrimaryKeyColumns().size() == 1;
	}

	public String getInsertSql() {
		List<String> insertColumns = new ArrayList(getColumns().size());
		List<String> insertPlaceholderColumns = new ArrayList(getColumns().size());
		for(int i = 0; i < getColumns().size(); i++) {
			Column c = getColumns().get(i);
			if(c.isInsertable()) {
				insertColumns.add(c.getSqlName());
				insertPlaceholderColumns.add(getColumnPlaceholder(c));
			}
		}
		
		StringBuilder sb = new StringBuilder("INSERT INTO ").append(getTableName()).append(" (");
		
		sb.append(StringUtils.join(insertColumns.iterator(), ","));
		sb.append(" ) VALUES ( ");

		sb.append(StringUtils.join(insertPlaceholderColumns.iterator(), ","));
		sb.append(" ) ");
		return sb.toString();
	}

	public String getDeleteByPkSql() {
			return getDeleteByMultiPkSql();
//		if(isMultiPrimaryKey()) {
//			return getDeleteByMultiPkSql();
//		}else if(isSinglePrimaryKey()) {
//			return getDeleteBySinglePkSql();
//		}
//		throw new IllegalStateException("not found primary key config on table:"+table.getTableName());
	}

	public String getSelectByPkSql() {
		return getSelectByMultiPkSql();
//		if(isMultiPrimaryKey()) {
//			return getSelectByMultiPkSql();
//		}else if(isSinglePrimaryKey()) {
//			return getSelectBySinglePkSql();
//		}
//		throw new IllegalStateException("not found primary key config on table:"+table.getTableName());
	}

	public String getUpdateByPkSql() {
		if(getPrimaryKeyColumns().size() == 0) {
			throw new IllegalStateException("not found primary key config on table:"+table.getTableName());
		}
		
		StringBuilder sb = new StringBuilder("UPDATE ").append(getTableName()).append(" SET ");
		
		sb.append(StringUtils.join(getUpdateColumns().iterator(), ","));
		sb.append(" WHERE ");

		List<Column> primaryKeyColumns = getPrimaryKeyColumns();
		appendWhereEqString(sb, primaryKeyColumns);
		
		Column versionColumn = getVersionColumn();
		if(versionColumn != null) {
			sb.append(" AND "+versionColumn.getSqlName() + " = " + getColumnPlaceholder(versionColumn));
		}
		return sb.toString();
	}

	private Column getVersionColumn() {
		List<Column> columns = getColumns();
		for(Column c : columns) {
			if(c.isVersion()) {
				return c;
			}
		}
		return null;
	}

	private List getUpdateColumns() {
		List<Column> columns = getColumns();
		List updateColumns = new ArrayList(columns.size());
		for(int i = 0; i < columns.size(); i++) {
			Column c = columns.get(i);
			if(c.isUpdatable() && !c.isPrimaryKey()) {
				if(c.isVersion()) {
					updateColumns.add(c.getSqlName() + " = "+getColumnPlaceholder(c) + " + 1 ");
				}else {
					updateColumns.add(c.getSqlName() + " = "+getColumnPlaceholder(c));
				}
			}
		}
		return updateColumns;
	}

	public String getDeleteByMultiPkSql() {
		StringBuilder sb = new StringBuilder("DELETE FROM ").append(getTableName());

		sb.append(" WHERE ");

		List<Column> primaryKeyColumns = getPrimaryKeyColumns();
		appendWhereEqString(sb, primaryKeyColumns);
		return sb.toString();
	}


	public String getDeleteBySinglePkSql() {
		checkIsSinglePrimaryKey();
		List<Column> primaryKeyColumns = getPrimaryKeyColumns();

		StringBuilder sb = new StringBuilder("DELETE FROM ").append(getTableName());

		sb.append(" WHERE ");

		sb.append(getSinglePrimaryKeyWhere());
		return sb.toString();
	}

	public String getSelectByMultiPkSql() {
		StringBuilder sb = new StringBuilder(getSelectFromSql()+" WHERE ");
		List<Column> primaryKeyColumns = getPrimaryKeyColumns();
		appendWhereEqString(sb, primaryKeyColumns);
		return sb.toString();
	}

	public String getSelectBySinglePkSql() {
		checkIsSinglePrimaryKey();
		List<Column> primaryKeyColumns = getPrimaryKeyColumns();

		StringBuilder sb = new StringBuilder(getSelectFromSql()+" WHERE ");
		sb.append(getSinglePrimaryKeyWhere());
		return sb.toString();
	}

	@Override
	public String getSelectFromSql() {
		return "SELECT "+getColumnsSql() + " FROM " + getTableName() + " ";
	}
	
	public String getColumnsSql() {
		return getColumnsSql(null);
	}

	public String getColumnsSql(String columnPrefix) {
		String realPrefix = StringUtils.isBlank(columnPrefix) ? "" : columnPrefix+".";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < getColumns().size(); i++) {
			Column c = getColumns().get(i);
			sb.append(realPrefix+c.getSqlName()+" " + c.getPropertyName());
			if(i < getColumns().size() - 1)
				sb.append(",");
		}
		return sb.toString();
	}
	
	private void appendWhereEqString(StringBuilder sb, List<Column> columns) {
		for(int i = 0; i < columns.size(); i++) {
			Column c = columns.get(i);
			sb.append(c.getSqlName()+" = "+getColumnPlaceholder(c));
			if(i < columns.size() - 1)
				sb.append(" AND ");
		}
	}
	
//	public void whereEqString(Map where) {
//		List<>
//		for(int i = 0; i < columns.size(); i++) {
//			Column c = columns.get(i);
//			sb.append(c.getSqlName()+" = "+getColumnPlaceholder(c));
//			if(i < columns.size() - 1)
//				sb.append(" AND ");
//		}
//	}
	
	protected String getColumnPlaceholder(Column c) {
		return ":"+c.getPropertyName();
	}

	protected String getSinglePrimaryKeyWhere() {
		Column c = getPrimaryKeyColumns().get(0);
		return c.getSqlName()+" = ?";
	}

	private void checkIsSinglePrimaryKey() {
		if(getPrimaryKeyColumns().size() != 1) {
			throw new IllegalStateException("expected single primary key on table:"+getTableName()+",but was primary keys:"+getPrimaryKeyColumns());
		}
	}

}
