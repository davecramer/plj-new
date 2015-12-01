/*
 * Created on Sep 2, 2004
 */
package org.codehaus.plj.febe.msg.sql;

import java.io.IOException;

import org.codehaus.plj.CommunicationException;
import org.codehaus.plj.febe.Encoding;
import org.codehaus.plj.febe.PGStream;
import org.codehaus.plj.messages.Message;
import org.codehaus.plj.messages.SQLPrepare;
import org.codehaus.plj.typemapping.MappingException;

/**
 * @author Laszlo Hornyak
 */
public class PrepareMessageFactory extends AbstractSQLMessageFactory {

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.jdbc.AbstractSQLMessageFactory#getSQLType()
	 */
	public int getSQLType() {
		return SQLTYPE_PREPARE;
	}

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.MessageFactory#getMessage(org.plj.chanells.febe.core.PGStream, org.plj.chanells.febe.core.Encoding)
	 */
	public Message getMessage(PGStream stream, Encoding encoding)
			throws IOException, MappingException, CommunicationException {
		throw new CommunicationException("can't get a message of that kind from the DBMS");
	}

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.MessageFactory#sendMessage(org.codehaus.plj.messages.Message, org.plj.chanells.febe.core.PGStream)
	 */
	public void sendMessage(Message msg, PGStream stream) throws IOException,
			MappingException, CommunicationException {
		SQLPrepare prepare = (SQLPrepare)msg;
		byte[] prep = prepare.getStatement().getBytes();
		stream.SendInteger(prep.length,4);
		stream.Send(prep);

		int sz = prepare.getParamtypes().size();
		stream.SendInteger(sz, 4);

		for(int i = 0; i< sz; i++){
			String tp = (String) prepare.getParamtypes().get(i);
			byte[] typ = tp.getBytes();
			stream.SendInteger(typ.length, 4);
			stream.Send(typ);
		}
	}

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.MessageFactory#getHandledClassname()
	 */
	public String getHandledClassname() {
		return SQLPrepare.class.getName();
	}

}
