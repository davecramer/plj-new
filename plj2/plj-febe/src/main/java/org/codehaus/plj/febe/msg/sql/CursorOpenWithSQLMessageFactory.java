/*
 * Created on Dec 26, 2004
 */
package org.codehaus.plj.febe.msg.sql;

import java.io.IOException;

import org.codehaus.plj.CommunicationException;
import org.codehaus.plj.febe.Encoding;
import org.codehaus.plj.febe.PGStream;
import org.codehaus.plj.messages.Message;
import org.codehaus.plj.messages.SQLCursorOpenWithSQL;
import org.codehaus.plj.typemapping.MappingException;


/**
 * Sends an SQLCursorOpenWithSQL message.
 * @author Laszlo Hornyak
 */
public class CursorOpenWithSQLMessageFactory extends AbstractSQLMessageFactory {

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.jdbc.AbstractSQLMessageFactory#getSQLType()
	 */
	public int getSQLType() {
		return SQLTYPE_CURSOR_OPEN;
	}

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.MessageFactory#getMessage(org.plj.chanells.febe.core.PGStream, org.plj.chanells.febe.core.Encoding)
	 */
	public Message getMessage(PGStream stream, Encoding encoding)
			throws IOException, MappingException, CommunicationException {
		throw new CommunicationException("Cursor open can't be received.");
	}

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.MessageFactory#sendMessage(org.codehaus.plj.messages.Message, org.plj.chanells.febe.core.PGStream)
	 */
	public void sendMessage(Message msg, PGStream stream) throws IOException,
			MappingException, CommunicationException {
		SQLCursorOpenWithSQL s = (SQLCursorOpenWithSQL)msg;
		byte[] b = s.getCursorName().getBytes();
		stream.SendInteger(b.length, 4);
		stream.Send(b);
		b = s.getQuery().getBytes();
		stream.SendInteger(b.length, 4);
		stream.Send(b);
	}

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.MessageFactory#getHandledClassname()
	 */
	public String getHandledClassname() {
		return SQLCursorOpenWithSQL.class.getName();
	}

}
