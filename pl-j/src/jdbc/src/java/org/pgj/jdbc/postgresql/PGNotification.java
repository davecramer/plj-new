/*-------------------------------------------------------------------------
 *
 * PGNotification.java
 *    This interface defines public PostgreSQL extention for Notifications
 *
 * Copyright (c) 2003, PostgreSQL Global Development Group
 *
 * IDENTIFICATION
 *	  $PostgreSQL: /cvsroot/pgsql-server/src/interfaces/jdbc/org/postgresql/PGNotification.java,v 1.3 2003/03/07 18:39:41 barry Exp $
 *
 *-------------------------------------------------------------------------
 */
package org.pgj.jdbc.postgresql;


public interface PGNotification
{
	/**
	 * Returns name of this notification
	 * @since 7.3
	 */
	public String getName();

	/**
	 * Returns the process id of the backend process making this notification
	 * @since 7.3
	 */
	public int getPID();

	/**
	 * Returns additional information from the notifying process.
	 * Currently, this feature is unimplemented and always returns
	 * an empty String.
	 */
	public String getParameter();

}

