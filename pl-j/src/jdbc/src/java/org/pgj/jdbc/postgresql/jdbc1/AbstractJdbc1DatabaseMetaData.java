
package org.pgj.jdbc.postgresql.jdbc1;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import org.pgj.jdbc.postgresql.Driver;
import org.pgj.jdbc.postgresql.core.BaseStatement;
import org.pgj.jdbc.postgresql.core.Field;

public abstract class AbstractJdbc1DatabaseMetaData {

	private static final String keywords = "abort,acl,add,aggregate,append,archive,"
			+ "arch_store,backward,binary,boolean,change,cluster,"
			+ "copy,database,delimiter,delimiters,do,extend,"
			+ "explain,forward,heavy,index,inherits,isnull,"
			+ "light,listen,load,merge,nothing,notify,"
			+ "notnull,oids,purge,rename,replace,retrieve,"
			+ "returns,rule,recipe,setof,stdin,stdout,store,"
			+ "vacuum,verbose,version";

	protected AbstractJdbc1Connection connection; // The connection association

	// These define various OID's. Hopefully they will stay constant.
	protected static final int iVarcharOid = 1043; // OID for varchar
	protected static final int iBoolOid = 16; // OID for bool
	protected static final int iInt2Oid = 21; // OID for int2
	protected static final int iInt4Oid = 23; // OID for int4
	protected static final int VARHDRSZ = 4; // length for int4

	private int NAMEDATALEN = 0; // length for name datatype
	private int INDEX_MAX_KEYS = 0; // maximum number of keys in an index.

	protected int getMaxIndexKeys() throws SQLException {
		return 0;
	}

	protected int getMaxNameLength() throws SQLException {
		return 0;
	}

	public AbstractJdbc1DatabaseMetaData(AbstractJdbc1Connection conn) {
		this.connection = conn;
	}

	/*
	 * Can all the procedures returned by getProcedures be called
	 * by the current user?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean allProceduresAreCallable() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("allProceduresAreCallable");
		return true; // For now...
	}

	/*
	 * Can all the tables returned by getTable be SELECTed by
	 * the current user?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean allTablesAreSelectable() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("allTablesAreSelectable");
		return true; // For now...
	}

	/*
	 * What is the URL for this database?
	 *
	 * @return the url or null if it cannott be generated
	 * @exception SQLException if a database access error occurs
	 */
	public String getURL() throws SQLException {
		String url = connection.getURL();
		if (Driver.logDebug)
			Driver.debug("getURL " + url);
		return url;
	}

	/*
	 * What is our user name as known to the database?
	 *
	 * @return our database user name
	 * @exception SQLException if a database access error occurs
	 */
	public String getUserName() throws SQLException {
		String userName = connection.getUserName();
		if (Driver.logDebug)
			Driver.debug("getUserName " + userName);
		return userName;
	}

	/*
	 * Is the database in read-only mode?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean isReadOnly() throws SQLException {
		boolean isReadOnly = connection.isReadOnly();
		if (Driver.logDebug)
			Driver.debug("isReadOnly " + isReadOnly);
		return isReadOnly;
	}

	/*
	 * Are NULL values sorted high?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean nullsAreSortedHigh() throws SQLException {
		return true;
	}

	/*
	 * Are NULL values sorted low?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean nullsAreSortedLow() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("nullsAreSortedLow false");
		return false;
	}

	/*
	 * Are NULL values sorted at the start regardless of sort order?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean nullsAreSortedAtStart() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("nullsAreSortedAtStart false");
		return false;
	}

	/*
	 * Are NULL values sorted at the end regardless of sort order?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean nullsAreSortedAtEnd() throws SQLException {
		return true;
	}

	/*
	 * What is the name of this database product - we hope that it is
	 * PostgreSQL, so we return that explicitly.
	 *
	 * @return the database product name
	 * @exception SQLException if a database access error occurs
	 */
	public String getDatabaseProductName() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getDatabaseProductName PostgresSQL");
		return "PostgreSQL";
	}

	/*
	 * What is the version of this database product.
	 *
	 * @return the database version
	 * @exception SQLException if a database access error occurs
	 */
	public String getDatabaseProductVersion() throws SQLException {
		String versionNumber = connection.getDBVersionNumber();
		if (Driver.logDebug)
			Driver.debug("getDatabaseProductVersion " + versionNumber);
		return versionNumber;
	}

	/*
	 * What is the name of this JDBC driver?  If we don't know this
	 * we are doing something wrong!
	 *
	 * @return the JDBC driver name
	 * @exception SQLException why?
	 */
	public String getDriverName() throws SQLException {
		String driverName = "PostgreSQL Native Driver";
		if (Driver.logDebug)
			Driver.debug("getDriverName" + driverName);
		return driverName;
	}

	/*
	 * What is the version string of this JDBC driver?	Again, this is
	 * static.
	 *
	 * @return the JDBC driver name.
	 * @exception SQLException why?
	 */
	public String getDriverVersion() throws SQLException {
		String driverVersion = Driver.getVersion();
		if (Driver.logDebug)
			Driver.debug("getDriverVersion " + driverVersion);
		return driverVersion;
	}

	/*
	 * What is this JDBC driver's major version number?
	 *
	 * @return the JDBC driver major version
	 */
	public int getDriverMajorVersion() {
		int majorVersion = connection.this_driver.getMajorVersion();
		if (Driver.logDebug)
			Driver.debug("getMajorVersion " + majorVersion);
		return majorVersion;
	}

	/*
	 * What is this JDBC driver's minor version number?
	 *
	 * @return the JDBC driver minor version
	 */
	public int getDriverMinorVersion() {
		int minorVersion = connection.this_driver.getMinorVersion();
		if (Driver.logDebug)
			Driver.debug("getMinorVersion " + minorVersion);
		return minorVersion;
	}

	/*
	 * Does the database store tables in a local file?	No - it
	 * stores them in a file on the server.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean usesLocalFiles() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("usesLocalFiles " + false);
		return false;
	}

	/*
	 * Does the database use a file for each table?  Well, not really,
	 * since it doesnt use local files.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean usesLocalFilePerTable() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("usesLocalFilePerTable " + false);
		return false;
	}

	/*
	 * Does the database treat mixed case unquoted SQL identifiers
	 * as case sensitive and as a result store them in mixed case?
	 * A JDBC-Compliant driver will always return false.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsMixedCaseIdentifiers " + false);
		return false;
	}

	/*
	 * Does the database treat mixed case unquoted SQL identifiers as
	 * case insensitive and store them in upper case?
	 *
	 * @return true if so
	 */
	public boolean storesUpperCaseIdentifiers() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("storesUpperCaseIdentifiers " + false);
		return false;
	}

	/*
	 * Does the database treat mixed case unquoted SQL identifiers as
	 * case insensitive and store them in lower case?
	 *
	 * @return true if so
	 */
	public boolean storesLowerCaseIdentifiers() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("storesLowerCaseIdentifiers " + true);
		return true;
	}

	/*
	 * Does the database treat mixed case unquoted SQL identifiers as
	 * case insensitive and store them in mixed case?
	 *
	 * @return true if so
	 */
	public boolean storesMixedCaseIdentifiers() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("storesMixedCaseIdentifiers " + false);
		return false;
	}

	/*
	 * Does the database treat mixed case quoted SQL identifiers as
	 * case sensitive and as a result store them in mixed case?  A
	 * JDBC compliant driver will always return true.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsMixedCaseQuotedIdentifiers " + true);
		return true;
	}

	/*
	 * Does the database treat mixed case quoted SQL identifiers as
	 * case insensitive and store them in upper case?
	 *
	 * @return true if so
	 */
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("storesUpperCaseQuotedIdentifiers " + false);
		return false;
	}

	/*
	 * Does the database treat mixed case quoted SQL identifiers as case
	 * insensitive and store them in lower case?
	 *
	 * @return true if so
	 */
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("storesLowerCaseQuotedIdentifiers " + false);
		return false;
	}

	/*
	 * Does the database treat mixed case quoted SQL identifiers as case
	 * insensitive and store them in mixed case?
	 *
	 * @return true if so
	 */
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("storesMixedCaseQuotedIdentifiers " + false);
		return false;
	}

	/*
	 * What is the string used to quote SQL identifiers?  This returns
	 * a space if identifier quoting isn't supported.  A JDBC Compliant
	 * driver will always use a double quote character.
	 *
	 * @return the quoting string
	 * @exception SQLException if a database access error occurs
	 */
	public String getIdentifierQuoteString() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getIdentifierQuoteString \"");
		return "\"";
	}

	/*
	 * Get a comma separated list of all a database's SQL keywords that
	 * are NOT also SQL92 keywords.
	 *
	 * <p>Within PostgreSQL, the keywords are found in
	 *	src/backend/parser/keywords.c
	 *
	 * <p>For SQL Keywords, I took the list provided at
	 *	<a href="http://web.dementia.org/~shadow/sql/sql3bnf.sep93.txt">
	 * http://web.dementia.org/~shadow/sql/sql3bnf.sep93.txt</a>
	 * which is for SQL3, not SQL-92, but it is close enough for
	 * this purpose.
	 *
	 * @return a comma separated list of keywords we use
	 * @exception SQLException if a database access error occurs
	 */
	public String getSQLKeywords() throws SQLException {
		return keywords;
	}

	public String getNumericFunctions() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getNumericFunctions");
		return "";
	}

	public String getStringFunctions() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getStringFunctions");
		return "";
	}

	public String getSystemFunctions() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getSystemFunctions");
		return "";
	}

	public String getTimeDateFunctions() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getTimeDateFunctions");
		return "";
	}

	/*
	 * This is the string that can be used to escape '_' and '%' in
	 * a search string pattern style catalog search parameters
	 *
	 * @return the string used to escape wildcard characters
	 * @exception SQLException if a database access error occurs
	 */
	public String getSearchStringEscape() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getSearchStringEscape");
		return "\\";
	}

	/*
	 * Get all the "extra" characters that can be used in unquoted
	 * identifier names (those beyond a-zA-Z0-9 and _)
	 *
	 * <p>From the file src/backend/parser/scan.l, an identifier is
	 * {letter}{letter_or_digit} which makes it just those listed
	 * above.
	 *
	 * @return a string containing the extra characters
	 * @exception SQLException if a database access error occurs
	 */
	public String getExtraNameCharacters() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getExtraNameCharacters");
		return "";
	}

	/*
	 * Is "ALTER TABLE" with an add column supported?
	 * Yes for PostgreSQL 6.1
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsAlterTableWithAddColumn " + true);
		return true;
	}

	/*
	 * Is "ALTER TABLE" with a drop column supported?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		return true;
	}

	/*
	 * Is column aliasing supported?
	 *
	 * <p>If so, the SQL AS clause can be used to provide names for
	 * computed columns or to provide alias names for columns as
	 * required.  A JDBC Compliant driver always returns true.
	 *
	 * <p>e.g.
	 *
	 * <br><pre>
	 * select count(C) as C_COUNT from T group by C;
	 *
	 * </pre><br>
	 * should return a column named as C_COUNT instead of count(C)
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsColumnAliasing() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsColumnAliasing " + true);
		return true;
	}

	/*
	 * Are concatenations between NULL and non-NULL values NULL?  A
	 * JDBC Compliant driver always returns true
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean nullPlusNonNullIsNull() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("nullPlusNonNullIsNull " + true);
		return true;
	}

	public boolean supportsConvert() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsConvert " + false);
		return false;
	}

	public boolean supportsConvert(int fromType, int toType)
			throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsConvert " + false);
		return false;
	}

	/*
	 * Are table correlation names supported? A JDBC Compliant
	 * driver always returns true.
	 *
	 * @return true if so; false otherwise
	 * @exception SQLException - if a database access error occurs
	 */
	public boolean supportsTableCorrelationNames() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsTableCorrelationNames " + true);
		return true;
	}

	/*
	 * If table correlation names are supported, are they restricted to
	 * be different from the names of the tables?
	 *
	 * @return true if so; false otherwise
	 * @exception SQLException - if a database access error occurs
	 */
	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsDifferentTableCorrelationNames " + false);
		return false;
	}

	/*
	 * Are expressions in "ORDER BY" lists supported?
	 *
	 * <br>e.g. select * from t order by a + b;
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsExpressionsInOrderBy() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsExpressionsInOrderBy " + true);
		return true;
	}

	/*
	 * Can an "ORDER BY" clause use columns not in the SELECT?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsOrderByUnrelated() throws SQLException {
		return true;
	}

	/*
	 * Is some form of "GROUP BY" clause supported?
	 * I checked it, and yes it is.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsGroupBy() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsGroupBy " + true);
		return true;
	}

	/*
	 * Can a "GROUP BY" clause use columns not in the SELECT?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsGroupByUnrelated() throws SQLException {
		return true;
	}

	/*
	 * Can a "GROUP BY" clause add columns not in the SELECT provided
	 * it specifies all the columns in the SELECT?	Does anyone actually
	 * understand what they mean here?
	 *
	 * (I think this is a subset of the previous function. -- petere)
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsGroupByBeyondSelect() throws SQLException {
		return true;
	}

	/*
	 * Is the escape character in "LIKE" clauses supported?  A
	 * JDBC compliant driver always returns true.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsLikeEscapeClause() throws SQLException {
		return true;
	}

	/*
	 * Are multiple ResultSets from a single execute supported?
	 * Well, I implemented it, but I dont think this is possible from
	 * the back ends point of view.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsMultipleResultSets() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsMultipleResultSets " + false);
		return false;
	}

	/*
	 * Can we have multiple transactions open at once (on different
	 * connections?)
	 * I guess we can have, since Im relying on it.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsMultipleTransactions() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsMultipleTransactions " + true);
		return true;
	}

	/*
	 * Can columns be defined as non-nullable.	A JDBC Compliant driver
	 * always returns true.
	 *
	 * <p>This changed from false to true in v6.2 of the driver, as this
	 * support was added to the backend.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsNonNullableColumns() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsNonNullableColumns true");
		return true;
	}

	/*
	 * Does this driver support the minimum ODBC SQL grammar.  This
	 * grammar is defined at:
	 *
	 * <p><a href="http://www.microsoft.com/msdn/sdk/platforms/doc/odbc/src/intropr.htm">http://www.microsoft.com/msdn/sdk/platforms/doc/odbc/src/intropr.htm</a>
	 *
	 * <p>In Appendix C.  From this description, we seem to support the
	 * ODBC minimal (Level 0) grammar.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsMinimumSQLGrammar() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsMinimumSQLGrammar TRUE");
		return true;
	}

	/*
	 * Does this driver support the Core ODBC SQL grammar.	We need
	 * SQL-92 conformance for this.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsCoreSQLGrammar() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsCoreSQLGrammar FALSE ");
		return false;
	}

	/*
	 * Does this driver support the Extended (Level 2) ODBC SQL
	 * grammar.  We don't conform to the Core (Level 1), so we can't
	 * conform to the Extended SQL Grammar.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsExtendedSQLGrammar() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsExtendedSQLGrammar FALSE");
		return false;
	}

	/*
	 * Does this driver support the ANSI-92 entry level SQL grammar?
	 * All JDBC Compliant drivers must return true. We currently
	 * report false until 'schema' support is added.  Then this
	 * should be changed to return true, since we will be mostly
	 * compliant (probably more compliant than many other databases)
	 * And since this is a requirement for all JDBC drivers we
	 * need to get to the point where we can return true.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		return true;
	}

	/*
	 * Does this driver support the ANSI-92 intermediate level SQL
	 * grammar?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsANSI92IntermediateSQL false ");
		return false;
	}

	/*
	 * Does this driver support the ANSI-92 full SQL grammar?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsANSI92FullSQL() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsANSI92FullSQL false ");
		return false;
	}

	/*
	 * Is the SQL Integrity Enhancement Facility supported?
	 * Our best guess is that this means support for constraints
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsIntegrityEnhancementFacility true ");
		return true;
	}

	/*
	 * Is some form of outer join supported?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsOuterJoins() throws SQLException {
		return true;
	}

	/*
	 * Are full nexted outer joins supported?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsFullOuterJoins() throws SQLException {
		return true;
	}

	/*
	 * Is there limited support for outer joins?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsLimitedOuterJoins() throws SQLException {
		return true;
	}

	/*
	 * What is the database vendor's preferred term for "schema"?
	 * PostgreSQL doesn't have schemas, but when it does, we'll use the
	 * term "schema".
	 *
	 * @return the vendor term
	 * @exception SQLException if a database access error occurs
	 */
	public String getSchemaTerm() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getSchemaTerm schema");
		return "schema";
	}

	/*
	 * What is the database vendor's preferred term for "procedure"?
	 * Traditionally, "function" has been used.
	 *
	 * @return the vendor term
	 * @exception SQLException if a database access error occurs
	 */
	public String getProcedureTerm() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getProcedureTerm function ");
		return "function";
	}

	/*
	 * What is the database vendor's preferred term for "catalog"?
	 *
	 * @return the vendor term
	 * @exception SQLException if a database access error occurs
	 */
	public String getCatalogTerm() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("getCatalogTerm database ");
		return "database";
	}

	/*
	 * Does a catalog appear at the start of a qualified table name?
	 * (Otherwise it appears at the end).
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean isCatalogAtStart() throws SQLException {
		// return true here; we return false for every other catalog function
		// so it won't matter what we return here D.C.
		if (Driver.logDebug)
			Driver.debug("isCatalogAtStart not implemented");
		return true;
	}

	/*
	 * What is the Catalog separator.
	 *
	 * @return the catalog separator string
	 * @exception SQLException if a database access error occurs
	 */
	public String getCatalogSeparator() throws SQLException {
		// Give them something to work with here
		// everything else returns false so it won't matter what we return here D.C.
		if (Driver.logDebug)
			Driver.debug("getCatalogSeparator not implemented ");
		return ".";
	}

	/*
	 * Can a schema name be used in a data manipulation statement?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsSchemasInDataManipulation() throws SQLException {
		return true;
	}

	/*
	 * Can a schema name be used in a procedure call statement?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		return true;
	}

	/*
	 * Can a schema be used in a table definition statement?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		return true;
	}

	/*
	 * Can a schema name be used in an index definition statement?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		return true;
	}

	/*
	 * Can a schema name be used in a privilege definition statement?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		return true;
	}

	/*
	 * Can a catalog name be used in a data manipulation statement?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsCatalogsInDataManipulation false");
		return false;
	}

	/*
	 * Can a catalog name be used in a procedure call statement?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsCatalogsInDataManipulation false");
		return false;
	}

	/*
	 * Can a catalog name be used in a table definition statement?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsCatalogsInTableDefinitions false");
		return false;
	}

	/*
	 * Can a catalog name be used in an index definition?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsCatalogsInIndexDefinitions false");
		return false;
	}

	/*
	 * Can a catalog name be used in a privilege definition statement?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsCatalogsInPrivilegeDefinitions false");
		return false;
	}

	/*
	 * We support cursors for gets only it seems.  I dont see a method
	 * to get a positioned delete.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsPositionedDelete() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsPositionedDelete false");
		return false; // For now...
	}

	/*
	 * Is positioned UPDATE supported?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsPositionedUpdate() throws SQLException {
		if (Driver.logDebug)
			Driver.debug("supportsPositionedUpdate false");
		return false; // For now...
	}

	/*
	 * Is SELECT for UPDATE supported?
	 *
	 * @return true if so; false otherwise
	 * @exception SQLException - if a database access error occurs
	 */
	public boolean supportsSelectForUpdate() throws SQLException {
		return true;
	}

	/*
	 * Are stored procedure calls using the stored procedure escape
	 * syntax supported?
	 *
	 * @return true if so; false otherwise
	 * @exception SQLException - if a database access error occurs
	 */
	public boolean supportsStoredProcedures() throws SQLException {
		return false;
	}

	/*
	 * Are subqueries in comparison expressions supported? A JDBC
	 * Compliant driver always returns true.
	 *
	 * @return true if so; false otherwise
	 * @exception SQLException - if a database access error occurs
	 */
	public boolean supportsSubqueriesInComparisons() throws SQLException {
		return true;
	}

	/*
	 * Are subqueries in 'exists' expressions supported? A JDBC
	 * Compliant driver always returns true.
	 *
	 * @return true if so; false otherwise
	 * @exception SQLException - if a database access error occurs
	 */
	public boolean supportsSubqueriesInExists() throws SQLException {
		return true;
	}

	/*
	 * Are subqueries in 'in' statements supported? A JDBC
	 * Compliant driver always returns true.
	 *
	 * @return true if so; false otherwise
	 * @exception SQLException - if a database access error occurs
	 */
	public boolean supportsSubqueriesInIns() throws SQLException {
		return true;
	}

	/*
	 * Are subqueries in quantified expressions supported? A JDBC
	 * Compliant driver always returns true.
	 *
	 * (No idea what this is, but we support a good deal of
	 * subquerying.)
	 *
	 * @return true if so; false otherwise
	 * @exception SQLException - if a database access error occurs
	 */
	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		return true;
	}

	/*
	 * Are correlated subqueries supported? A JDBC Compliant driver
	 * always returns true.
	 *
	 * (a.k.a. subselect in from?)
	 *
	 * @return true if so; false otherwise
	 * @exception SQLException - if a database access error occurs
	 */
	public boolean supportsCorrelatedSubqueries() throws SQLException {
		return true;
	}

	/*
	 * Is SQL UNION supported?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsUnion() throws SQLException {
		return true; // since 6.3
	}

	/*
	 * Is SQL UNION ALL supported?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsUnionAll() throws SQLException {
		return true;
	}

	/*
	 * In PostgreSQL, Cursors are only open within transactions.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		return false;
	}

	/*
	 * Do we support open cursors across multiple transactions?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		return false;
	}

	/*
	 * Can statements remain open across commits?  They may, but
	 * this driver cannot guarentee that.  In further reflection.
	 * we are talking a Statement object here, so the answer is
	 * yes, since the Statement is only a vehicle to ExecSQL()
	 *
	 * @return true if they always remain open; false otherwise
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		return true;
	}

	/*
	 * Can statements remain open across rollbacks?  They may, but
	 * this driver cannot guarentee that.  In further contemplation,
	 * we are talking a Statement object here, so the answer is yes,
	 * since the Statement is only a vehicle to ExecSQL() in Connection
	 *
	 * @return true if they always remain open; false otherwise
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		return true;
	}

	/*
	 * How many hex characters can you have in an inline binary literal
	 *
	 * @return the max literal length
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxBinaryLiteralLength() throws SQLException {
		return 0; // no limit
	}

	/*
	 * What is the maximum length for a character literal
	 * I suppose it is 8190 (8192 - 2 for the quotes)
	 *
	 * @return the max literal length
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxCharLiteralLength() throws SQLException {
		return 0; // no limit
	}

	/*
	 * Whats the limit on column name length.
	 *
	 * @return the maximum column name length
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxColumnNameLength() throws SQLException {
		return getMaxNameLength();
	}

	/*
	 * What is the maximum number of columns in a "GROUP BY" clause?
	 *
	 * @return the max number of columns
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxColumnsInGroupBy() throws SQLException {
		return 0; // no limit
	}

	/*
	 * What's the maximum number of columns allowed in an index?
	 *
	 * @return max number of columns
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxColumnsInIndex() throws SQLException {
		return getMaxIndexKeys();
	}

	/*
	 * What's the maximum number of columns in an "ORDER BY clause?
	 *
	 * @return the max columns
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxColumnsInOrderBy() throws SQLException {
		return 0; // no limit
	}

	/*
	 * What is the maximum number of columns in a "SELECT" list?
	 *
	 * @return the max columns
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxColumnsInSelect() throws SQLException {
		return 0; // no limit
	}

	/*
	 * What is the maximum number of columns in a table? From the
	 * CREATE TABLE reference page...
	 *
	 * <p>"The new class is created as a heap with no initial data.  A
	 * class can have no more than 1600 attributes (realistically,
	 * this is limited by the fact that tuple sizes must be less than
	 * 8192 bytes)..."
	 *
	 * @return the max columns
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxColumnsInTable() throws SQLException {
		return 1600;
	}

	/*
	 * How many active connection can we have at a time to this
	 * database?  Well, since it depends on postmaster, which just
	 * does a listen() followed by an accept() and fork(), its
	 * basically very high.  Unless the system runs out of processes,
	 * it can be 65535 (the number of aux. ports on a TCP/IP system).
	 * I will return 8192 since that is what even the largest system
	 * can realistically handle,
	 *
	 * @return the maximum number of connections
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxConnections() throws SQLException {
		return 8192;
	}

	/*
	 * What is the maximum cursor name length
	 *
	 * @return max cursor name length in bytes
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxCursorNameLength() throws SQLException {
		return getMaxNameLength();
	}

	/*
	 * Retrieves the maximum number of bytes for an index, including all
	 * of the parts of the index.
	 *
	 * @return max index length in bytes, which includes the composite
	 * of all the constituent parts of the index; a result of zero means
	 * that there is no limit or the limit is not known
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxIndexLength() throws SQLException {
		return 0; // no limit (larger than an int anyway)
	}

	public int getMaxSchemaNameLength() throws SQLException {
		return getMaxNameLength();
	}

	/*
	 * What is the maximum length of a procedure name
	 *
	 * @return the max name length in bytes
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxProcedureNameLength() throws SQLException {
		return getMaxNameLength();
	}

	public int getMaxCatalogNameLength() throws SQLException {
		return getMaxNameLength();
	}

	/*
	 * What is the maximum length of a single row?
	 *
	 * @return max row size in bytes
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxRowSize() throws SQLException {
		return 1073741824; // 1 GB
	}

	/*
	 * Did getMaxRowSize() include LONGVARCHAR and LONGVARBINARY
	 * blobs?  We don't handle blobs yet
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		return false;
	}

	/*
	 * What is the maximum length of a SQL statement?
	 *
	 * @return max length in bytes
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxStatementLength() throws SQLException {
		return 0; // actually whatever fits in size_t
	}

	/*
	 * How many active statements can we have open at one time to
	 * this database?  Basically, since each Statement downloads
	 * the results as the query is executed, we can have many.	However,
	 * we can only really have one statement per connection going
	 * at once (since they are executed serially) - so we return
	 * one.
	 *
	 * @return the maximum
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxStatements() throws SQLException {
		return 1;
	}

	/*
	 * What is the maximum length of a table name
	 *
	 * @return max name length in bytes
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxTableNameLength() throws SQLException {
		return getMaxNameLength();
	}

	/*
	 * What is the maximum number of tables that can be specified
	 * in a SELECT?
	 *
	 * @return the maximum
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxTablesInSelect() throws SQLException {
		return 0; // no limit
	}

	/*
	 * What is the maximum length of a user name
	 *
	 * @return the max name length in bytes
	 * @exception SQLException if a database access error occurs
	 */
	public int getMaxUserNameLength() throws SQLException {
		return getMaxNameLength();
	}


	/*
	 * What is the database's default transaction isolation level?  We
	 * do not support this, so all transactions are SERIALIZABLE.
	 *
	 * @return the default isolation level
	 * @exception SQLException if a database access error occurs
	 * @see Connection
	 */
	public int getDefaultTransactionIsolation() throws SQLException {
		return Connection.TRANSACTION_READ_COMMITTED;
	}

	/*
	 * Are transactions supported?	If not, commit and rollback are noops
	 * and the isolation level is TRANSACTION_NONE.  We do support
	 * transactions.
	 *
	 * @return true if transactions are supported
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsTransactions() throws SQLException {
		return true;
	}

	/*
	 * Does the database support the given transaction isolation level?
	 * We only support TRANSACTION_SERIALIZABLE and TRANSACTION_READ_COMMITTED
	 *
	 * @param level the values are defined in java.sql.Connection
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 * @see Connection
	 */
	public boolean supportsTransactionIsolationLevel(int level)
			throws SQLException {
		if (level == Connection.TRANSACTION_SERIALIZABLE
				|| level == Connection.TRANSACTION_READ_COMMITTED)
			return true;
		else
			return false;
	}

	/*
	 * Are both data definition and data manipulation transactions
	 * supported?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsDataDefinitionAndDataManipulationTransactions()
			throws SQLException {
		return true;
	}

	/*
	 * Are only data manipulation statements withing a transaction
	 * supported?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean supportsDataManipulationTransactionsOnly()
			throws SQLException {
		return false;
	}

	/*
	 * Does a data definition statement within a transaction force
	 * the transaction to commit?  I think this means something like:
	 *
	 * <p><pre>
	 * CREATE TABLE T (A INT);
	 * INSERT INTO T (A) VALUES (2);
	 * BEGIN;
	 * UPDATE T SET A = A + 1;
	 * CREATE TABLE X (A INT);
	 * SELECT A FROM T INTO X;
	 * COMMIT;
	 * </pre><p>
	 *
	 * does the CREATE TABLE call cause a commit?  The answer is no.
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		return false;
	}

	/*
	 * Is a data definition statement within a transaction ignored?
	 *
	 * @return true if so
	 * @exception SQLException if a database access error occurs
	 */
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		return false;
	}

	/**
	 * Escape single quotes with another single quote.
	 */
	protected static String escapeQuotes(String s) {
		StringBuffer sb = new StringBuffer();
		int length = s.length();
		char prevChar = ' ';
		char prevPrevChar = ' ';
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			sb.append(c);
			if (c == '\''
					&& (prevChar != '\\' || (prevChar == '\\' && prevPrevChar == '\\'))) {
				sb.append("'");
			}
			prevPrevChar = prevChar;
			prevChar = c;
		}
		return sb.toString();
	}

	/*
	 * Get a description of stored procedures available in a catalog
	 *
	 * <p>Only procedure descriptions matching the schema and procedure
	 * name criteria are returned.	They are ordered by PROCEDURE_SCHEM
	 * and PROCEDURE_NAME
	 *
	 * <p>Each procedure description has the following columns:
	 * <ol>
	 * <li><b>PROCEDURE_CAT</b> String => procedure catalog (may be null)
	 * <li><b>PROCEDURE_SCHEM</b> String => procedure schema (may be null)
	 * <li><b>PROCEDURE_NAME</b> String => procedure name
	 * <li><b>Field 4</b> reserved (make it null)
	 * <li><b>Field 5</b> reserved (make it null)
	 * <li><b>Field 6</b> reserved (make it null)
	 * <li><b>REMARKS</b> String => explanatory comment on the procedure
	 * <li><b>PROCEDURE_TYPE</b> short => kind of procedure
	 *	<ul>
	 *	  <li> procedureResultUnknown - May return a result
	 *	<li> procedureNoResult - Does not return a result
	 *	<li> procedureReturnsResult - Returns a result
	 *	  </ul>
	 * </ol>
	 *
	 * @param catalog - a catalog name; "" retrieves those without a
	 *	catalog; null means drop catalog name from criteria
	 * @param schemaParrern - a schema name pattern; "" retrieves those
	 *	without a schema - we ignore this parameter
	 * @param procedureNamePattern - a procedure name pattern
	 * @return ResultSet - each row is a procedure description
	 * @exception SQLException if a database access error occurs
	 */
	public java.sql.ResultSet getProcedures(String catalog,
			String schemaPattern, String procedureNamePattern)
			throws SQLException {
		return null;
	}

	/*
	 * Get a description of a catalog's stored procedure parameters
	 * and result columns.
	 *
	 * <p>Only descriptions matching the schema, procedure and parameter
	 * name criteria are returned. They are ordered by PROCEDURE_SCHEM
	 * and PROCEDURE_NAME. Within this, the return value, if any, is
	 * first. Next are the parameter descriptions in call order. The
	 * column descriptions follow in column number order.
	 *
	 * <p>Each row in the ResultSet is a parameter description or column
	 * description with the following fields:
	 * <ol>
	 * <li><b>PROCEDURE_CAT</b> String => procedure catalog (may be null)
	 * <li><b>PROCEDURE_SCHE</b>M String => procedure schema (may be null)
	 * <li><b>PROCEDURE_NAME</b> String => procedure name
	 * <li><b>COLUMN_NAME</b> String => column/parameter name
	 * <li><b>COLUMN_TYPE</b> Short => kind of column/parameter:
	 * <ul><li>procedureColumnUnknown - nobody knows
	 * <li>procedureColumnIn - IN parameter
	 * <li>procedureColumnInOut - INOUT parameter
	 * <li>procedureColumnOut - OUT parameter
	 * <li>procedureColumnReturn - procedure return value
	 * <li>procedureColumnResult - result column in ResultSet
	 * </ul>
	 * <li><b>DATA_TYPE</b> short => SQL type from java.sql.Types
	 * <li><b>TYPE_NAME</b> String => Data source specific type name
	 * <li><b>PRECISION</b> int => precision
	 * <li><b>LENGTH</b> int => length in bytes of data
	 * <li><b>SCALE</b> short => scale
	 * <li><b>RADIX</b> short => radix
	 * <li><b>NULLABLE</b> short => can it contain NULL?
	 * <ul><li>procedureNoNulls - does not allow NULL values
	 * <li>procedureNullable - allows NULL values
	 * <li>procedureNullableUnknown - nullability unknown
	 * <li><b>REMARKS</b> String => comment describing parameter/column
	 * </ol>
	 * @param catalog This is ignored in org.pgj.jdbc.postgresql, advise this is set to null
	 * @param schemaPattern
	 * @param procedureNamePattern a procedure name pattern
	 * @param columnNamePattern a column name pattern, this is currently ignored because postgresql does not name procedure parameters.
	 * @return each row is a stored procedure parameter or column description
	 * @exception SQLException if a database-access error occurs
	 * @see #getSearchStringEscape
	 */
	// Implementation note: This is required for Borland's JBuilder to work
	public java.sql.ResultSet getProcedureColumns(String catalog,
			String schemaPattern, String procedureNamePattern,
			String columnNamePattern) throws SQLException {
		return null;
	}

	/*
	 * Get a description of tables available in a catalog.
	 *
	 * <p>Only table descriptions matching the catalog, schema, table
	 * name and type criteria are returned. They are ordered by
	 * TABLE_TYPE, TABLE_SCHEM and TABLE_NAME.
	 *
	 * <p>Each table description has the following columns:
	 *
	 * <ol>
	 * <li><b>TABLE_CAT</b> String => table catalog (may be null)
	 * <li><b>TABLE_SCHEM</b> String => table schema (may be null)
	 * <li><b>TABLE_NAME</b> String => table name
	 * <li><b>TABLE_TYPE</b> String => table type. Typical types are "TABLE",
	 * "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL
	 * TEMPORARY", "ALIAS", "SYNONYM".
	 * <li><b>REMARKS</b> String => explanatory comment on the table
	 * </ol>
	 *
	 * <p>The valid values for the types parameter are:
	 * "TABLE", "INDEX", "SEQUENCE", "VIEW",
	 * "SYSTEM TABLE", "SYSTEM INDEX", "SYSTEM VIEW",
	 * "SYSTEM TOAST TABLE", "SYSTEM TOAST INDEX",
	 * "TEMPORARY TABLE", and "TEMPORARY VIEW"
	 *
	 * @param catalog a catalog name; For org.pgj.jdbc.postgresql, this is ignored, and
	 * should be set to null
	 * @param schemaPattern a schema name pattern
	 * @param tableNamePattern a table name pattern. For all tables this should be "%"
	 * @param types a list of table types to include; null returns
	 * all types
	 * @return each row is a table description
	 * @exception SQLException if a database-access error occurs.
	 */
	public java.sql.ResultSet getTables(String catalog, String schemaPattern,
			String tableNamePattern, String types[]) throws SQLException {
		return null;
	}

	private static final Hashtable tableTypeClauses;
	static {
		tableTypeClauses = new Hashtable();
		Hashtable ht = new Hashtable();
		tableTypeClauses.put("TABLE", ht);
		ht.put("SCHEMAS", "c.relkind = 'r' AND n.nspname NOT LIKE 'pg\\\\_%'");
		ht
				.put("NOSCHEMAS",
						"c.relkind = 'r' AND c.relname NOT LIKE 'pg\\\\_%'");
		ht = new Hashtable();
		tableTypeClauses.put("VIEW", ht);
		ht.put("SCHEMAS", "c.relkind = 'v' AND n.nspname <> 'pg_catalog'");
		ht
				.put("NOSCHEMAS",
						"c.relkind = 'v' AND c.relname NOT LIKE 'pg\\\\_%'");
		ht = new Hashtable();
		tableTypeClauses.put("INDEX", ht);
		ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname NOT LIKE 'pg\\\\_%'");
		ht
				.put("NOSCHEMAS",
						"c.relkind = 'i' AND c.relname NOT LIKE 'pg\\\\_%'");
		ht = new Hashtable();
		tableTypeClauses.put("SEQUENCE", ht);
		ht.put("SCHEMAS", "c.relkind = 'S'");
		ht.put("NOSCHEMAS", "c.relkind = 'S'");
		ht = new Hashtable();
		tableTypeClauses.put("SYSTEM TABLE", ht);
		ht.put("SCHEMAS", "c.relkind = 'r' AND n.nspname = 'pg_catalog'");
		ht
				.put(
						"NOSCHEMAS",
						"c.relkind = 'r' AND c.relname LIKE 'pg\\\\_%' AND c.relname NOT LIKE 'pg\\\\_toast\\\\_%' AND c.relname NOT LIKE 'pg\\\\_temp\\\\_%'");
		ht = new Hashtable();
		tableTypeClauses.put("SYSTEM TOAST TABLE", ht);
		ht.put("SCHEMAS", "c.relkind = 'r' AND n.nspname = 'pg_toast'");
		ht.put("NOSCHEMAS",
				"c.relkind = 'r' AND c.relname LIKE 'pg\\\\_toast\\\\_%'");
		ht = new Hashtable();
		tableTypeClauses.put("SYSTEM TOAST INDEX", ht);
		ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname = 'pg_toast'");
		ht.put("NOSCHEMAS",
				"c.relkind = 'i' AND c.relname LIKE 'pg\\\\_toast\\\\_%'");
		ht = new Hashtable();
		tableTypeClauses.put("SYSTEM VIEW", ht);
		ht.put("SCHEMAS", "c.relkind = 'v' AND n.nspname = 'pg_catalog' ");
		ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname LIKE 'pg\\\\_%'");
		ht = new Hashtable();
		tableTypeClauses.put("SYSTEM INDEX", ht);
		ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname = 'pg_catalog'");
		ht
				.put(
						"NOSCHEMAS",
						"c.relkind = 'v' AND c.relname LIKE 'pg\\\\_%' AND c.relname NOT LIKE 'pg\\\\_toast\\\\_%' AND c.relname NOT LIKE 'pg\\\\_temp\\\\_%'");
		ht = new Hashtable();
		tableTypeClauses.put("TEMPORARY TABLE", ht);
		ht.put("SCHEMAS",
				"c.relkind = 'r' AND n.nspname LIKE 'pg\\\\_temp\\\\_%' ");
		ht.put("NOSCHEMAS",
				"c.relkind = 'r' AND c.relname LIKE 'pg\\\\_temp\\\\_%' ");
		ht = new Hashtable();
		tableTypeClauses.put("TEMPORARY INDEX", ht);
		ht.put("SCHEMAS",
				"c.relkind = 'i' AND n.nspname LIKE 'pg\\\\_temp\\\\_%' ");
		ht.put("NOSCHEMAS",
				"c.relkind = 'i' AND c.relname LIKE 'pg\\\\_temp\\\\_%' ");
	}

	// These are the default tables, used when NULL is passed to getTables
	// The choice of these provide the same behaviour as psql's \d
	private static final String defaultTableTypes[] = {"TABLE", "VIEW",
			"INDEX", "SEQUENCE", "TEMPORARY TABLE"};

	/*
	 * Get the schema names available in this database.  The results
	 * are ordered by schema name.
	 *
	 * <P>The schema column is:
	 *	<OL>
	 *	<LI><B>TABLE_SCHEM</B> String => schema name
	 *	</OL>
	 *
	 * @return ResultSet each row has a single String column that is a
	 * schema name
	 */
	public java.sql.ResultSet getSchemas() throws SQLException {
		return null;
	}

	/*
	 * Get the catalog names available in this database.  The results
	 * are ordered by catalog name.
	 *
	 * <P>The catalog column is:
	 *	<OL>
	 *	<LI><B>TABLE_CAT</B> String => catalog name
	 *	</OL>
	 *
	 * @return ResultSet each row has a single String column that is a
	 * catalog name
	 */
	public java.sql.ResultSet getCatalogs() throws SQLException {
		return null;
	}

	/*
	 * Get the table types available in this database.	The results
	 * are ordered by table type.
	 *
	 * <P>The table type is:
	 *	<OL>
	 *	<LI><B>TABLE_TYPE</B> String => table type.  Typical types are "TABLE",
	 *			"VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
	 *			"LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	 *	</OL>
	 *
	 * @return ResultSet each row has a single String column that is a
	 * table type
	 */
	public java.sql.ResultSet getTableTypes() throws SQLException {
		return null;
	}

	/*
	 * Get a description of table columns available in a catalog.
	 *
	 * <P>Only column descriptions matching the catalog, schema, table
	 * and column name criteria are returned.  They are ordered by
	 * TABLE_SCHEM, TABLE_NAME and ORDINAL_POSITION.
	 *
	 * <P>Each column description has the following columns:
	 *	<OL>
	 *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
	 *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
	 *	<LI><B>TABLE_NAME</B> String => table name
	 *	<LI><B>COLUMN_NAME</B> String => column name
	 *	<LI><B>DATA_TYPE</B> short => SQL type from java.sql.Types
	 *	<LI><B>TYPE_NAME</B> String => Data source dependent type name
	 *	<LI><B>COLUMN_SIZE</B> int => column size.	For char or date
	 *		types this is the maximum number of characters, for numeric or
	 *		decimal types this is precision.
	 *	<LI><B>BUFFER_LENGTH</B> is not used.
	 *	<LI><B>DECIMAL_DIGITS</B> int => the number of fractional digits
	 *	<LI><B>NUM_PREC_RADIX</B> int => Radix (typically either 10 or 2)
	 *	<LI><B>NULLABLE</B> int => is NULL allowed?
	 *		<UL>
	 *		<LI> columnNoNulls - might not allow NULL values
	 *		<LI> columnNullable - definitely allows NULL values
	 *		<LI> columnNullableUnknown - nullability unknown
	 *		</UL>
	 *	<LI><B>REMARKS</B> String => comment describing column (may be null)
	 *	<LI><B>COLUMN_DEF</B> String => default value (may be null)
	 *	<LI><B>SQL_DATA_TYPE</B> int => unused
	 *	<LI><B>SQL_DATETIME_SUB</B> int => unused
	 *	<LI><B>CHAR_OCTET_LENGTH</B> int => for char types the
	 *		 maximum number of bytes in the column
	 *	<LI><B>ORDINAL_POSITION</B> int => index of column in table
	 *		(starting at 1)
	 *	<LI><B>IS_NULLABLE</B> String => "NO" means column definitely
	 *		does not allow NULL values; "YES" means the column might
	 *		allow NULL values.	An empty string means nobody knows.
	 *	</OL>
	 *
	 * @param catalog a catalog name; "" retrieves those without a catalog
	 * @param schemaPattern a schema name pattern; "" retrieves those
	 * without a schema
	 * @param tableNamePattern a table name pattern
	 * @param columnNamePattern a column name pattern
	 * @return ResultSet each row is a column description
	 * @see #getSearchStringEscape
	 */
	public java.sql.ResultSet getColumns(String catalog, String schemaPattern,
			String tableNamePattern, String columnNamePattern)
			throws SQLException {
		return null;
	}

	/*
	 * Get a description of the access rights for a table's columns.
	 *
	 * <P>Only privileges matching the column name criteria are
	 * returned.  They are ordered by COLUMN_NAME and PRIVILEGE.
	 *
	 * <P>Each privilige description has the following columns:
	 *	<OL>
	 *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
	 *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
	 *	<LI><B>TABLE_NAME</B> String => table name
	 *	<LI><B>COLUMN_NAME</B> String => column name
	 *	<LI><B>GRANTOR</B> => grantor of access (may be null)
	 *	<LI><B>GRANTEE</B> String => grantee of access
	 *	<LI><B>PRIVILEGE</B> String => name of access (SELECT,
	 *		INSERT, UPDATE, REFRENCES, ...)
	 *	<LI><B>IS_GRANTABLE</B> String => "YES" if grantee is permitted
	 *		to grant to others; "NO" if not; null if unknown
	 *	</OL>
	 *
	 * @param catalog a catalog name; "" retrieves those without a catalog
	 * @param schema a schema name; "" retrieves those without a schema
	 * @param table a table name
	 * @param columnNamePattern a column name pattern
	 * @return ResultSet each row is a column privilege description
	 * @see #getSearchStringEscape
	 */
	public java.sql.ResultSet getColumnPrivileges(String catalog,
			String schema, String table, String columnNamePattern)
			throws SQLException {
		return null;
	}

	/*
	 * Get a description of the access rights for each table available
	 * in a catalog.
	 *
	 * This method is currently unimplemented.
	 *
	 * <P>Only privileges matching the schema and table name
	 * criteria are returned.  They are ordered by TABLE_SCHEM,
	 * TABLE_NAME, and PRIVILEGE.
	 *
	 * <P>Each privilige description has the following columns:
	 *	<OL>
	 *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
	 *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
	 *	<LI><B>TABLE_NAME</B> String => table name
	 *	<LI><B>GRANTOR</B> => grantor of access (may be null)
	 *	<LI><B>GRANTEE</B> String => grantee of access
	 *	<LI><B>PRIVILEGE</B> String => name of access (SELECT,
	 *		INSERT, UPDATE, REFRENCES, ...)
	 *	<LI><B>IS_GRANTABLE</B> String => "YES" if grantee is permitted
	 *		to grant to others; "NO" if not; null if unknown
	 *	</OL>
	 *
	 * @param catalog a catalog name; "" retrieves those without a catalog
	 * @param schemaPattern a schema name pattern; "" retrieves those
	 * without a schema
	 * @param tableNamePattern a table name pattern
	 * @return ResultSet each row is a table privilege description
	 * @see #getSearchStringEscape
	 */
	public java.sql.ResultSet getTablePrivileges(String catalog,
			String schemaPattern, String tableNamePattern) throws SQLException {
		return null;
	}

	private static void sortStringArray(String s[]) {
		for (int i = 0; i < s.length - 1; i++) {
			for (int j = i + 1; j < s.length; j++) {
				if (s[i].compareTo(s[j]) > 0) {
					String tmp = s[i];
					s[i] = s[j];
					s[j] = tmp;
				}
			}
		}
	}

	/**
	 * Parse an String of ACLs into a Vector of ACLs.
	 */
	private static Vector parseACLArray(String aclString) {
		Vector acls = new Vector();
		if (aclString == null || aclString.length() == 0) {
			return acls;
		}
		boolean inQuotes = false;
		// start at 1 because of leading "{"
		int beginIndex = 1;
		char prevChar = ' ';
		for (int i = beginIndex; i < aclString.length(); i++) {

			char c = aclString.charAt(i);
			if (c == '"' && prevChar != '\\') {
				inQuotes = !inQuotes;
			} else if (c == ',' && !inQuotes) {
				acls.addElement(aclString.substring(beginIndex, i));
				beginIndex = i + 1;
			}
			prevChar = c;
		}
		// add last element removing the trailing "}"
		acls
				.addElement(aclString.substring(beginIndex,
						aclString.length() - 1));

		// Strip out enclosing quotes, if any.
		for (int i = 0; i < acls.size(); i++) {
			String acl = (String) acls.elementAt(i);
			if (acl.startsWith("\"") && acl.endsWith("\"")) {
				acl = acl.substring(1, acl.length() - 1);
				acls.setElementAt(acl, i);
			}
		}
		return acls;
	}

	/**
	 * Add the user described by the given acl to the Vectors of users
	 * with the privileges described by the acl.
	 */
	private void addACLPrivileges(String acl, Hashtable privileges) {
		int equalIndex = acl.lastIndexOf("=");
		String name = acl.substring(0, equalIndex);
		if (name.length() == 0) {
			name = "PUBLIC";
		}
		String privs = acl.substring(equalIndex + 1);
		for (int i = 0; i < privs.length(); i++) {
			char c = privs.charAt(i);
			String sqlpriv;
			switch (c) {
				case 'a' :
					sqlpriv = "INSERT";
					break;
				case 'r' :
					sqlpriv = "SELECT";
					break;
				case 'w' :
					sqlpriv = "UPDATE";
					break;
				case 'd' :
					sqlpriv = "DELETE";
					break;
				case 'R' :
					sqlpriv = "RULE";
					break;
				case 'x' :
					sqlpriv = "REFERENCES";
					break;
				case 't' :
					sqlpriv = "TRIGGER";
					break;
				// the folloowing can't be granted to a table, but
				// we'll keep them for completeness.
				case 'X' :
					sqlpriv = "EXECUTE";
					break;
				case 'U' :
					sqlpriv = "USAGE";
					break;
				case 'C' :
					sqlpriv = "CREATE";
					break;
				case 'T' :
					sqlpriv = "CREATE TEMP";
					break;
				default :
					sqlpriv = "UNKNOWN";
			}
			Vector usersWithPermission = (Vector) privileges.get(sqlpriv);
			if (usersWithPermission == null) {
				usersWithPermission = new Vector();
				privileges.put(sqlpriv, usersWithPermission);
			}
			usersWithPermission.addElement(name);
		}
	}

	/**
	 * Take the a String representing an array of ACLs and return
	 * a Hashtable mapping the SQL permission name to a Vector of
	 * usernames who have that permission.
	 */
	protected Hashtable parseACL(String aclArray, String owner) {
		if (aclArray == null || aclArray == "") {
			//null acl is a shortcut for owner having full privs
			aclArray = "{" + owner + "=arwdRxt}";
		}
		Vector acls = parseACLArray(aclArray);
		Hashtable privileges = new Hashtable();
		for (int i = 0; i < acls.size(); i++) {
			String acl = (String) acls.elementAt(i);
			addACLPrivileges(acl, privileges);
		}
		return privileges;
	}

	/*
	 * Get a description of a table's optimal set of columns that
	 * uniquely identifies a row. They are ordered by SCOPE.
	 *
	 * <P>Each column description has the following columns:
	 *	<OL>
	 *	<LI><B>SCOPE</B> short => actual scope of result
	 *		<UL>
	 *		<LI> bestRowTemporary - very temporary, while using row
	 *		<LI> bestRowTransaction - valid for remainder of current transaction
	 *		<LI> bestRowSession - valid for remainder of current session
	 *		</UL>
	 *	<LI><B>COLUMN_NAME</B> String => column name
	 *	<LI><B>DATA_TYPE</B> short => SQL data type from java.sql.Types
	 *	<LI><B>TYPE_NAME</B> String => Data source dependent type name
	 *	<LI><B>COLUMN_SIZE</B> int => precision
	 *	<LI><B>BUFFER_LENGTH</B> int => not used
	 *	<LI><B>DECIMAL_DIGITS</B> short  => scale
	 *	<LI><B>PSEUDO_COLUMN</B> short => is this a pseudo column
	 *		like an Oracle ROWID
	 *		<UL>
	 *		<LI> bestRowUnknown - may or may not be pseudo column
	 *		<LI> bestRowNotPseudo - is NOT a pseudo column
	 *		<LI> bestRowPseudo - is a pseudo column
	 *		</UL>
	 *	</OL>
	 *
	 * @param catalog a catalog name; "" retrieves those without a catalog
	 * @param schema a schema name; "" retrieves those without a schema
	 * @param table a table name
	 * @param scope the scope of interest; use same values as SCOPE
	 * @param nullable include columns that are nullable?
	 * @return ResultSet each row is a column description
	 */
	// Implementation note: This is required for Borland's JBuilder to work
	public java.sql.ResultSet getBestRowIdentifier(String catalog,
			String schema, String table, int scope, boolean nullable)
			throws SQLException {
		return null;
	}

	/*
	 * Get a description of a table's columns that are automatically
	 * updated when any value in a row is updated.	They are
	 * unordered.
	 *
	 * <P>Each column description has the following columns:
	 *	<OL>
	 *	<LI><B>SCOPE</B> short => is not used
	 *	<LI><B>COLUMN_NAME</B> String => column name
	 *	<LI><B>DATA_TYPE</B> short => SQL data type from java.sql.Types
	 *	<LI><B>TYPE_NAME</B> String => Data source dependent type name
	 *	<LI><B>COLUMN_SIZE</B> int => precision
	 *	<LI><B>BUFFER_LENGTH</B> int => length of column value in bytes
	 *	<LI><B>DECIMAL_DIGITS</B> short  => scale
	 *	<LI><B>PSEUDO_COLUMN</B> short => is this a pseudo column
	 *		like an Oracle ROWID
	 *		<UL>
	 *		<LI> versionColumnUnknown - may or may not be pseudo column
	 *		<LI> versionColumnNotPseudo - is NOT a pseudo column
	 *		<LI> versionColumnPseudo - is a pseudo column
	 *		</UL>
	 *	</OL>
	 *
	 * @param catalog a catalog name; "" retrieves those without a catalog
	 * @param schema a schema name; "" retrieves those without a schema
	 * @param table a table name
	 * @return ResultSet each row is a column description
	 */
	public java.sql.ResultSet getVersionColumns(String catalog, String schema,
			String table) throws SQLException {
		Field f[] = new Field[8];
		Vector v = new Vector(); // The new ResultSet tuple stuff

		f[0] = new Field(connection, "SCOPE", iInt2Oid, 2);
		f[1] = new Field(connection, "COLUMN_NAME", iVarcharOid,
				getMaxNameLength());
		f[2] = new Field(connection, "DATA_TYPE", iInt2Oid, 2);
		f[3] = new Field(connection, "TYPE_NAME", iVarcharOid,
				getMaxNameLength());
		f[4] = new Field(connection, "COLUMN_SIZE", iInt4Oid, 4);
		f[5] = new Field(connection, "BUFFER_LENGTH", iInt4Oid, 4);
		f[6] = new Field(connection, "DECIMAL_DIGITS", iInt2Oid, 2);
		f[7] = new Field(connection, "PSEUDO_COLUMN", iInt2Oid, 2);

		byte tuple[][] = new byte[8][];

		/* Postgresql does not have any column types that are
		 * automatically updated like some databases' timestamp type.
		 * We can't tell what rules or triggers might be doing, so we
		 * are left with the system columns that change on an update.
		 * An update may change all of the following system columns:
		 * ctid, xmax, xmin, cmax, and cmin.  Depending on if we are
		 * in a transaction and wether we roll it back or not the
		 * only guaranteed change is to ctid. -KJ
		 */

		tuple[0] = null;
		tuple[1] = "ctid".getBytes();
		tuple[2] = Integer.toString(connection.getSQLType("tid")).getBytes();
		tuple[3] = "tid".getBytes();
		tuple[4] = null;
		tuple[5] = null;
		tuple[6] = null;
		tuple[7] = Integer.toString(
				java.sql.DatabaseMetaData.versionColumnPseudo).getBytes();
		v.addElement(tuple);

		/* Perhaps we should check that the given
		 * catalog.schema.table actually exists. -KJ
		 */
		return (ResultSet) ((BaseStatement) connection.createStatement())
				.createResultSet(f, v, "OK", 1, 0, false);
	}

	/*
	 * Get a description of a table's primary key columns.  They
	 * are ordered by COLUMN_NAME.
	 *
	 * <P>Each column description has the following columns:
	 *	<OL>
	 *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
	 *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
	 *	<LI><B>TABLE_NAME</B> String => table name
	 *	<LI><B>COLUMN_NAME</B> String => column name
	 *	<LI><B>KEY_SEQ</B> short => sequence number within primary key
	 *	<LI><B>PK_NAME</B> String => primary key name (may be null)
	 *	</OL>
	 *
	 * @param catalog a catalog name; "" retrieves those without a catalog
	 * @param schema a schema name pattern; "" retrieves those
	 * without a schema
	 * @param table a table name
	 * @return ResultSet each row is a primary key column description
	 */
	public java.sql.ResultSet getPrimaryKeys(String catalog, String schema,
			String table) throws SQLException {
		return null;
	}

	/**
	 *
	 * @param catalog
	 * @param schema
	 * @param primaryTable if provided will get the keys exported by this table
	 * @param foreignTable if provided will get the keys imported by this table
	 * @return ResultSet
	 * @throws SQLException
	 */

	protected java.sql.ResultSet getImportedExportedKeys(String primaryCatalog,
			String primarySchema, String primaryTable, String foreignCatalog,
			String foreignSchema, String foreignTable) throws SQLException {
		return null;
	}

	/*
	 * Get a description of the primary key columns that are
	 * referenced by a table's foreign key columns (the primary keys
	 * imported by a table).  They are ordered by PKTABLE_CAT,
	 * PKTABLE_SCHEM, PKTABLE_NAME, and KEY_SEQ.
	 *
	 * <P>Each primary key column description has the following columns:
	 *	<OL>
	 *	<LI><B>PKTABLE_CAT</B> String => primary key table catalog
	 *		being imported (may be null)
	 *	<LI><B>PKTABLE_SCHEM</B> String => primary key table schema
	 *		being imported (may be null)
	 *	<LI><B>PKTABLE_NAME</B> String => primary key table name
	 *		being imported
	 *	<LI><B>PKCOLUMN_NAME</B> String => primary key column name
	 *		being imported
	 *	<LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be null)
	 *	<LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be null)
	 *	<LI><B>FKTABLE_NAME</B> String => foreign key table name
	 *	<LI><B>FKCOLUMN_NAME</B> String => foreign key column name
	 *	<LI><B>KEY_SEQ</B> short => sequence number within foreign key
	 *	<LI><B>UPDATE_RULE</B> short => What happens to
	 *		 foreign key when primary is updated:
	 *		<UL>
	 *		<LI> importedKeyCascade - change imported key to agree
	 *				 with primary key update
	 *		<LI> importedKeyRestrict - do not allow update of primary
	 *				 key if it has been imported
	 *		<LI> importedKeySetNull - change imported key to NULL if
	 *				 its primary key has been updated
	 *		</UL>
	 *	<LI><B>DELETE_RULE</B> short => What happens to
	 *		the foreign key when primary is deleted.
	 *		<UL>
	 *		<LI> importedKeyCascade - delete rows that import a deleted key
	 *		<LI> importedKeyRestrict - do not allow delete of primary
	 *				 key if it has been imported
	 *		<LI> importedKeySetNull - change imported key to NULL if
	 *				 its primary key has been deleted
	 *		</UL>
	 *	<LI><B>FK_NAME</B> String => foreign key name (may be null)
	 *	<LI><B>PK_NAME</B> String => primary key name (may be null)
	 *	</OL>
	 *
	 * @param catalog a catalog name; "" retrieves those without a catalog
	 * @param schema a schema name pattern; "" retrieves those
	 * without a schema
	 * @param table a table name
	 * @return ResultSet each row is a primary key column description
	 * @see #getExportedKeys
	 */
	public java.sql.ResultSet getImportedKeys(String catalog, String schema,
			String table) throws SQLException {
		return getImportedExportedKeys(null, null, null, catalog, schema, table);
	}

	/*
	 * Get a description of a foreign key columns that reference a
	 * table's primary key columns (the foreign keys exported by a
	 * table).	They are ordered by FKTABLE_CAT, FKTABLE_SCHEM,
	 * FKTABLE_NAME, and KEY_SEQ.
	 *
	 * This method is currently unimplemented.
	 *
	 * <P>Each foreign key column description has the following columns:
	 *	<OL>
	 *	<LI><B>PKTABLE_CAT</B> String => primary key table catalog (may be null)
	 *	<LI><B>PKTABLE_SCHEM</B> String => primary key table schema (may be null)
	 *	<LI><B>PKTABLE_NAME</B> String => primary key table name
	 *	<LI><B>PKCOLUMN_NAME</B> String => primary key column name
	 *	<LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be null)
	 *		being exported (may be null)
	 *	<LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be null)
	 *		being exported (may be null)
	 *	<LI><B>FKTABLE_NAME</B> String => foreign key table name
	 *		being exported
	 *	<LI><B>FKCOLUMN_NAME</B> String => foreign key column name
	 *		being exported
	 *	<LI><B>KEY_SEQ</B> short => sequence number within foreign key
	 *	<LI><B>UPDATE_RULE</B> short => What happens to
	 *		 foreign key when primary is updated:
	 *		<UL>
	 *		<LI> importedKeyCascade - change imported key to agree
	 *				 with primary key update
	 *		<LI> importedKeyRestrict - do not allow update of primary
	 *				 key if it has been imported
	 *		<LI> importedKeySetNull - change imported key to NULL if
	 *				 its primary key has been updated
	 *		</UL>
	 *	<LI><B>DELETE_RULE</B> short => What happens to
	 *		the foreign key when primary is deleted.
	 *		<UL>
	 *		<LI> importedKeyCascade - delete rows that import a deleted key
	 *		<LI> importedKeyRestrict - do not allow delete of primary
	 *				 key if it has been imported
	 *		<LI> importedKeySetNull - change imported key to NULL if
	 *				 its primary key has been deleted
	 *		</UL>
	 *	<LI><B>FK_NAME</B> String => foreign key identifier (may be null)
	 *	<LI><B>PK_NAME</B> String => primary key identifier (may be null)
	 *	</OL>
	 *
	 * @param catalog a catalog name; "" retrieves those without a catalog
	 * @param schema a schema name pattern; "" retrieves those
	 * without a schema
	 * @param table a table name
	 * @return ResultSet each row is a foreign key column description
	 * @see #getImportedKeys
	 */
	public java.sql.ResultSet getExportedKeys(String catalog, String schema,
			String table) throws SQLException {
		return getImportedExportedKeys(catalog, schema, table, null, null, null);
	}

	/*
	 * Get a description of the foreign key columns in the foreign key
	 * table that reference the primary key columns of the primary key
	 * table (describe how one table imports another's key.) This
	 * should normally return a single foreign key/primary key pair
	 * (most tables only import a foreign key from a table once.)  They
	 * are ordered by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and
	 * KEY_SEQ.
	 *
	 * This method is currently unimplemented.
	 *
	 * <P>Each foreign key column description has the following columns:
	 *	<OL>
	 *	<LI><B>PKTABLE_CAT</B> String => primary key table catalog (may be null)
	 *	<LI><B>PKTABLE_SCHEM</B> String => primary key table schema (may be null)
	 *	<LI><B>PKTABLE_NAME</B> String => primary key table name
	 *	<LI><B>PKCOLUMN_NAME</B> String => primary key column name
	 *	<LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be null)
	 *		being exported (may be null)
	 *	<LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be null)
	 *		being exported (may be null)
	 *	<LI><B>FKTABLE_NAME</B> String => foreign key table name
	 *		being exported
	 *	<LI><B>FKCOLUMN_NAME</B> String => foreign key column name
	 *		being exported
	 *	<LI><B>KEY_SEQ</B> short => sequence number within foreign key
	 *	<LI><B>UPDATE_RULE</B> short => What happens to
	 *		 foreign key when primary is updated:
	 *		<UL>
	 *		<LI> importedKeyCascade - change imported key to agree
	 *				 with primary key update
	 *		<LI> importedKeyRestrict - do not allow update of primary
	 *				 key if it has been imported
	 *		<LI> importedKeySetNull - change imported key to NULL if
	 *				 its primary key has been updated
	 *		</UL>
	 *	<LI><B>DELETE_RULE</B> short => What happens to
	 *		the foreign key when primary is deleted.
	 *		<UL>
	 *		<LI> importedKeyCascade - delete rows that import a deleted key
	 *		<LI> importedKeyRestrict - do not allow delete of primary
	 *				 key if it has been imported
	 *		<LI> importedKeySetNull - change imported key to NULL if
	 *				 its primary key has been deleted
	 *		</UL>
	 *	<LI><B>FK_NAME</B> String => foreign key identifier (may be null)
	 *	<LI><B>PK_NAME</B> String => primary key identifier (may be null)
	 *	</OL>
	 *
	 * @param catalog a catalog name; "" retrieves those without a catalog
	 * @param schema a schema name pattern; "" retrieves those
	 * without a schema
	 * @param table a table name
	 * @return ResultSet each row is a foreign key column description
	 * @see #getImportedKeys
	 */
	public java.sql.ResultSet getCrossReference(String primaryCatalog,
			String primarySchema, String primaryTable, String foreignCatalog,
			String foreignSchema, String foreignTable) throws SQLException {
		return getImportedExportedKeys(primaryCatalog, primarySchema,
				primaryTable, foreignCatalog, foreignSchema, foreignTable);
	}

	/*
	 * Get a description of all the standard SQL types supported by
	 * this database. They are ordered by DATA_TYPE and then by how
	 * closely the data type maps to the corresponding JDBC SQL type.
	 *
	 * <P>Each type description has the following columns:
	 *	<OL>
	 *	<LI><B>TYPE_NAME</B> String => Type name
	 *	<LI><B>DATA_TYPE</B> short => SQL data type from java.sql.Types
	 *	<LI><B>PRECISION</B> int => maximum precision
	 *	<LI><B>LITERAL_PREFIX</B> String => prefix used to quote a literal
	 *		(may be null)
	 *	<LI><B>LITERAL_SUFFIX</B> String => suffix used to quote a literal
	 (may be null)
	 *	<LI><B>CREATE_PARAMS</B> String => parameters used in creating
	 *		the type (may be null)
	 *	<LI><B>NULLABLE</B> short => can you use NULL for this type?
	 *		<UL>
	 *		<LI> typeNoNulls - does not allow NULL values
	 *		<LI> typeNullable - allows NULL values
	 *		<LI> typeNullableUnknown - nullability unknown
	 *		</UL>
	 *	<LI><B>CASE_SENSITIVE</B> boolean=> is it case sensitive?
	 *	<LI><B>SEARCHABLE</B> short => can you use "WHERE" based on this type:
	 *		<UL>
	 *		<LI> typePredNone - No support
	 *		<LI> typePredChar - Only supported with WHERE .. LIKE
	 *		<LI> typePredBasic - Supported except for WHERE .. LIKE
	 *		<LI> typeSearchable - Supported for all WHERE ..
	 *		</UL>
	 *	<LI><B>UNSIGNED_ATTRIBUTE</B> boolean => is it unsigned?
	 *	<LI><B>FIXED_PREC_SCALE</B> boolean => can it be a money value?
	 *	<LI><B>AUTO_INCREMENT</B> boolean => can it be used for an
	 *		auto-increment value?
	 *	<LI><B>LOCAL_TYPE_NAME</B> String => localized version of type name
	 *		(may be null)
	 *	<LI><B>MINIMUM_SCALE</B> short => minimum scale supported
	 *	<LI><B>MAXIMUM_SCALE</B> short => maximum scale supported
	 *	<LI><B>SQL_DATA_TYPE</B> int => unused
	 *	<LI><B>SQL_DATETIME_SUB</B> int => unused
	 *	<LI><B>NUM_PREC_RADIX</B> int => usually 2 or 10
	 *	</OL>
	 *
	 * @return ResultSet each row is a SQL type description
	 */
	public java.sql.ResultSet getTypeInfo() throws SQLException {
		return null;
	}

	/*
	 * Get a description of a table's indices and statistics. They are
	 * ordered by NON_UNIQUE, TYPE, INDEX_NAME, and ORDINAL_POSITION.
	 *
	 * <P>Each index column description has the following columns:
	 *	<OL>
	 *	<LI><B>TABLE_CAT</B> String => table catalog (may be null)
	 *	<LI><B>TABLE_SCHEM</B> String => table schema (may be null)
	 *	<LI><B>TABLE_NAME</B> String => table name
	 *	<LI><B>NON_UNIQUE</B> boolean => Can index values be non-unique?
	 *		false when TYPE is tableIndexStatistic
	 *	<LI><B>INDEX_QUALIFIER</B> String => index catalog (may be null);
	 *		null when TYPE is tableIndexStatistic
	 *	<LI><B>INDEX_NAME</B> String => index name; null when TYPE is
	 *		tableIndexStatistic
	 *	<LI><B>TYPE</B> short => index type:
	 *		<UL>
	 *		<LI> tableIndexStatistic - this identifies table statistics that are
	 *			 returned in conjuction with a table's index descriptions
	 *		<LI> tableIndexClustered - this is a clustered index
	 *		<LI> tableIndexHashed - this is a hashed index
	 *		<LI> tableIndexOther - this is some other style of index
	 *		</UL>
	 *	<LI><B>ORDINAL_POSITION</B> short => column sequence number
	 *		within index; zero when TYPE is tableIndexStatistic
	 *	<LI><B>COLUMN_NAME</B> String => column name; null when TYPE is
	 *		tableIndexStatistic
	 *	<LI><B>ASC_OR_DESC</B> String => column sort sequence, "A" => ascending
	 *		"D" => descending, may be null if sort sequence is not supported;
	 *		null when TYPE is tableIndexStatistic
	 *	<LI><B>CARDINALITY</B> int => When TYPE is tableIndexStatisic then
	 *		this is the number of rows in the table; otherwise it is the
	 *		number of unique values in the index.
	 *	<LI><B>PAGES</B> int => When TYPE is  tableIndexStatisic then
	 *		this is the number of pages used for the table, otherwise it
	 *		is the number of pages used for the current index.
	 *	<LI><B>FILTER_CONDITION</B> String => Filter condition, if any.
	 *		(may be null)
	 *	</OL>
	 *
	 * @param catalog a catalog name; "" retrieves those without a catalog
	 * @param schema a schema name pattern; "" retrieves those without a schema
	 * @param table a table name
	 * @param unique when true, return only indices for unique values;
	 *	   when false, return indices regardless of whether unique or not
	 * @param approximate when true, result is allowed to reflect approximate
	 *	   or out of data values; when false, results are requested to be
	 *	   accurate
	 * @return ResultSet each row is an index column description
	 */
	// Implementation note: This is required for Borland's JBuilder to work
	public java.sql.ResultSet getIndexInfo(String catalog, String schema,
			String tableName, boolean unique, boolean approximate)
			throws SQLException {
		return null;
	}

	/**
	 * Tokenize based on words not on single characters.
	 */
	private static Vector tokenize(String input, String delimiter) {
		Vector result = new Vector();
		int start = 0;
		int end = input.length();
		int delimiterSize = delimiter.length();

		while (start < end) {
			int delimiterIndex = input.indexOf(delimiter, start);
			if (delimiterIndex < 0) {
				result.addElement(input.substring(start));
				break;
			} else {
				String token = input.substring(start, delimiterIndex);
				result.addElement(token);
				start = delimiterIndex + delimiterSize;
			}
		}
		return result;
	}


}