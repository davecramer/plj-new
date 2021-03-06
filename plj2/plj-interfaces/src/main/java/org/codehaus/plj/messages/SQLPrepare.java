/*
 * Created on Jun 29, 2003
 */
package org.codehaus.plj.messages;

import java.util.ArrayList;

/**
 * Message to prepare an SQL statement for execution.
 * The RDBMS returns a result message to describe the created plan.
 * For use in JDBC drivers.
 * 
 * @author Laszlo Hornyak
 */
public class SQLPrepare extends SQL {

	/** The statement to prepare */
	private String statement = null;

	/** Parameter types */
	private ArrayList paramtypes = new ArrayList();

	/**
	 * @return
	 */
	public String getStatement() {
		return statement;
	}

	/**
	 * @param string
	 */
	public void setStatement(String string) {
		statement = string;
	}

	public ArrayList getParamtypes() {
		return paramtypes;
	}
	public void setParamtypes(ArrayList paramtypes) {
		this.paramtypes = paramtypes;
	}
}