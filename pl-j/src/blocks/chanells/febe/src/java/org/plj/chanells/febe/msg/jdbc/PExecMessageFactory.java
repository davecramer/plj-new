/*
 * Created on Sep 11, 2004
 */
package org.plj.chanells.febe.msg.jdbc;

import java.io.IOException;

import org.pgj.CommunicationException;
import org.pgj.messages.Message;
import org.pgj.messages.SQLExecute;
import org.pgj.typemapping.Field;
import org.pgj.typemapping.MappingException;
import org.plj.chanells.febe.core.Encoding;
import org.plj.chanells.febe.core.PGStream;


/**
 * Sends exec plan messages.
 * @author Laszlo Hornyak
 */
class PExecMessageFactory extends AbstractSQLMessageFactory{

	
	
	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.jdbc.AbstractSQLMessageFactory#getSQLType()
	 */
	public int getSQLType() {
		return SQLTYPE_PEXECUTE;
	}

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.MessageFactory#getMessage(org.plj.chanells.febe.core.PGStream, org.plj.chanells.febe.core.Encoding)
	 */
	public Message getMessage(PGStream stream, Encoding encoding) throws IOException, MappingException, CommunicationException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.MessageFactory#sendMessage(org.pgj.messages.Message, org.plj.chanells.febe.core.PGStream)
	 */
	public void sendMessage(Message msg, PGStream stream) throws IOException, MappingException, CommunicationException {
		SQLExecute exec= (SQLExecute)msg;
		stream.SendInteger(exec.getPlanid(), 4);
		stream.SendInteger(exec.getAction(), 4);
		Field[] flds = exec.getParams();
		stream.SendInteger(flds.length, 4);
		for(int i = 0; i < flds.length; i++){
			if(flds[i]==null || flds[i].isNull()) {
				stream.SendChar('N');
			} else {
				stream.SendChar('D');
				byte[] tname = flds[i].rdbmsType().getBytes();
				stream.SendInteger(tname.length, 4);
				stream.Send(tname);
				byte[] data = flds[i].get();
				stream.SendInteger(data.length, 4);
				stream.Send(data);
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see org.plj.chanells.febe.msg.MessageFactory#getHandledClassname()
	 */
	public String getHandledClassname() {
		return SQLExecute.class.getName();
	}

}
