package org.pgj.jdbc.postgresql.jdbc3;


import java.sql.SQLException;
import java.util.Vector;

import org.pgj.jdbc.postgresql.core.BaseStatement;
import org.pgj.jdbc.postgresql.core.Field;

/* $Header: /home/projects/plj/scm-cvs/pl-j/src/jdbc/src/java/org/pgj/jdbc/postgresql/jdbc3/Jdbc3ResultSet.java,v 1.1 2004-08-01 11:30:27 kocka Exp $
 * This class implements the java.sql.ResultSet interface for JDBC3.
 * However most of the implementation is really done in
 * org.pgj.jdbc.postgresql.jdbc3.AbstractJdbc3ResultSet or one of it's parents
 */
public class Jdbc3ResultSet extends org.pgj.jdbc.postgresql.jdbc3.AbstractJdbc3ResultSet implements java.sql.ResultSet
{

	public Jdbc3ResultSet(BaseStatement statement, Field[] fields, Vector tuples, String status, int updateCount, long insertOID, boolean binaryCursor)
	{
		super(statement, fields, tuples, status, updateCount, insertOID, binaryCursor);
	}

	public java.sql.ResultSetMetaData getMetaData() throws SQLException
	{
		return new Jdbc3ResultSetMetaData(rows, fields);
	}

	public java.sql.Clob getClob(int i) throws SQLException
	{
		wasNullFlag = (this_row[i - 1] == null);
		if (wasNullFlag)
			return null;

		return new Jdbc3Clob(connection, getInt(i));
	}

	public java.sql.Blob getBlob(int i) throws SQLException
	{
		wasNullFlag = (this_row[i - 1] == null);
		if (wasNullFlag)
			return null;

		return new Jdbc3Blob(connection, getInt(i));
	}

}

