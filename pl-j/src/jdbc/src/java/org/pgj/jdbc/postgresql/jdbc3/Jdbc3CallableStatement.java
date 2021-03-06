package org.pgj.jdbc.postgresql.jdbc3;


import java.sql.SQLException;
import java.util.Vector;

import org.pgj.jdbc.postgresql.PGRefCursorResultSet;
import org.pgj.jdbc.postgresql.core.BaseResultSet;
import org.pgj.jdbc.postgresql.core.Field;

public class Jdbc3CallableStatement extends org.pgj.jdbc.postgresql.jdbc3.AbstractJdbc3Statement implements java.sql.CallableStatement
{

	public Jdbc3CallableStatement(Jdbc3Connection connection, String sql) throws SQLException
	{
		super(connection, sql);
	}

	public BaseResultSet createResultSet (Field[] fields, Vector tuples, String status, int updateCount, long insertOID, boolean binaryCursor) throws SQLException
	{
		return new Jdbc3ResultSet(this, fields, tuples, status, updateCount, insertOID, binaryCursor);
	}

  	public PGRefCursorResultSet createRefCursorResultSet (String cursorName) throws SQLException
	{
                return new Jdbc3RefCursorResultSet(this, cursorName);
	}
}

