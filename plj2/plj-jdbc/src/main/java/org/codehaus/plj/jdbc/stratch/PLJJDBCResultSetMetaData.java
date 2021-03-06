/*
 * Created on Jun 12, 2003
 */

package org.codehaus.plj.jdbc.stratch;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * ResultSetMetadata.
 * 
 * @author Laszlo Hornyak
 */
public class PLJJDBCResultSetMetaData implements ResultSetMetaData {

	private int column_count = 0;
	boolean[] autoincrement = null;
	boolean[] casesensitive = null;
	boolean[] searchable = null;
	boolean[] nullable = null;
	PLJJDBCResultSet resultSet = null;

	/**
	 * @param chanell
	 * @param cursorName
	 */
	public PLJJDBCResultSetMetaData(PLJJDBCResultSet resultSet) {
		this.resultSet = resultSet;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getColumnCount()
	 */
	public int getColumnCount() throws SQLException {
		return column_count;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#isAutoIncrement(int)
	 */
	public boolean isAutoIncrement(int column) throws SQLException {
		return autoincrement[column];
	}

	/**
	 * @see java.sql.ResultSetMetaData#isCaseSensitive(int)
	 */
	public boolean isCaseSensitive(int column) throws SQLException {
		return casesensitive[column];
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#isSearchable(int)
	 */
	public boolean isSearchable(int column) throws SQLException {
		return true;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#isCurrency(int)
	 */
	public boolean isCurrency(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#isNullable(int)
	 */
	public int isNullable(int column) throws SQLException {
		if (nullable == null) {
			return columnNullableUnknown;
		}
		return nullable[column] ? columnNullable : columnNoNulls;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#isSigned(int)
	 */
	public boolean isSigned(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getColumnDisplaySize(int)
	 */
	public int getColumnDisplaySize(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getColumnLabel(int)
	 */
	public String getColumnLabel(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getColumnName(int)
	 */
	public String getColumnName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getSchemaName(int)
	 */
	public String getSchemaName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getPrecision(int)
	 */
	public int getPrecision(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getScale(int)
	 */
	public int getScale(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getTableName(int)
	 */
	public String getTableName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getCatalogName(int)
	 */
	public String getCatalogName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getColumnType(int)
	 */
	public int getColumnType(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getColumnTypeName(int)
	 */
	public String getColumnTypeName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#isReadOnly(int)
	 */
	public boolean isReadOnly(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#isWritable(int)
	 */
	public boolean isWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#isDefinitelyWritable(int)
	 */
	public boolean isDefinitelyWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSetMetaData#getColumnClassName(int)
	 */
	public String getColumnClassName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
}
