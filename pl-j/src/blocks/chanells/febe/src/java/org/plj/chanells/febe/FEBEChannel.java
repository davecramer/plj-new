/*
 * Created on Jan 18, 2004
 */

package org.plj.chanells.febe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.cornerstone.services.sockets.ServerSocketFactory;
import org.apache.avalon.cornerstone.services.sockets.SocketManager;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.pgj.Channel;
import org.pgj.Client;
import org.pgj.CommunicationException;
import org.pgj.messages.Log;
import org.pgj.messages.Message;
import org.pgj.messages.Result;
import org.pgj.messages.SQL;
import org.pgj.messages.TupleResult;
import org.pgj.typemapping.MappingException;
import org.pgj.typemapping.TypeMapper;
import org.plj.chanells.febe.core.Encoding;
import org.plj.chanells.febe.core.PGStream;
import org.plj.chanells.febe.msg.CallMessageFactory;
import org.plj.chanells.febe.msg.ErrorMessageFactory;
import org.plj.chanells.febe.msg.LogMessageFactory;
import org.plj.chanells.febe.msg.MessageFactory;
import org.plj.chanells.febe.msg.ResultMessageFactory;
import org.plj.chanells.febe.msg.TriggerCallRequestMessageFactory;
import org.plj.chanells.febe.msg.TupleResultMessageFactory;
import org.plj.chanells.febe.msg.jdbc.SQLMessageFactory;

/**
 * A chanell built on FE/BE protocoll basing on the PostgreSQL JDBC team`s
 * implementation.
 * 
 * @author Laszlo Hornyak
 * @version 0.1
 * 
 * @avalon.component name="FEBEChannel" lifestyle="singleton" 
 * @avalon.service type="org.pgj.Channel"
 * 
 * @dna.component
 * @dna.service type="org.pgj.Channel"
 * @mx.component
 * 
 */
public class FEBEChannel
		implements
			Channel,
			Configurable,
			Initializable,
			Serviceable,
			LogEnabled,
			Startable {

	private ServerSocket serverSocket = null;
	private TypeMapper typeMapper = null;
	/**
	 * The encoding object of the chanell.
	 */
	private Encoding defaultEncoding = null;
	private Map messageFactoryMap = new HashMap();
	private int port = 0;
	String socketFactoryName = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pgj.Channel#getConnection(int)
	 */
	public Client getConnection(int timeout) {
		logger.debug("waiting for incoming TCP connection");
		try {
			//TODO: timeout is ignored ...
			FEBEClient client;
			Socket sock = serverSocket.accept();
			client = new FEBEClient();
			PGStream stream = new PGStream(sock, logger);
			client.setStream(stream);
			client.setChannel(this);
			client.setTypeMapper(typeMapper);
			return client;
		} catch (IOException e) {
			logger.fatalError("GetConnection, accept", e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pgj.Channel#receiveFromRDBMS(org.pgj.Client)
	 */
	public Message receiveFromRDBMS(Client client)
			throws CommunicationException, MappingException {
		try {
			synchronized (client) {
				PGStream stream = ((FEBEClient) client).getStream();
				Encoding encoding = ((FEBEClient) client).getEncoding();
				if (encoding == null) {
					logger
							.warn("ancodign was null, fallback to hardcoded default");
					encoding = Encoding.defaultEncoding();
				}
				//the length??
				stream.Receive(4);
				int msgtype = stream.ReceiveChar();
				Character type = new Character((char) msgtype);
				logger.debug("message type:" + type);
				MessageFactory factory = (MessageFactory) messageFactoryMap
						.get(type);
				if (factory != null) {
					logger.debug("handling with "
							+ factory.getClass().getName());
					Message ret = factory.getMessage(stream, encoding);
					ret.setClient(client);
					return ret;
				}
					//debug block -->
				try {
					while (true) {
						logger.debug(new Character((char) stream.ReceiveChar())
								.toString());
					}
				} catch (RuntimeException e1) {
					logger.debug("client gone.");
				}
				// <- debug block
				throw new CommunicationException("Unhandled message type:"
						+ type);
			}
		} catch (IOException e) {
			logger.error("I/O exception on receiveing from RDBMS", e);
			throw new CommunicationException("Error at receiveFromRDBMS", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pgj.Channel#sendToRDBMS(org.pgj.messages.Message)
	 */
	public void sendToRDBMS(Message msg) throws CommunicationException, MappingException {
		FEBEClient client = (FEBEClient) msg.getClient();
		synchronized (client) {
			PGStream stream = client.getStream();
			//byte[] hdr = {0, 0, 0, 0};
			try {
				//stream.Send(hdr);
				Character type = null;
				if (msg instanceof org.pgj.messages.Error) {
					type = new Character('E');
				} else if (msg instanceof Result) {
					type = new Character('R');
				} else if (msg instanceof Log) {
					type = new Character('L');
				} else if (msg instanceof TupleResult) {
					type = new Character('U');
				} else if (msg instanceof SQL){
					type = new Character('S');
				}
				if (type == null)
					throw new CommunicationException(
							"unhandled type of message");
				MessageFactory factory = (MessageFactory) messageFactoryMap
						.get(type);
				stream.SendChar(type.charValue());
				logger.debug(msg.toString());
				factory.sendMessage(msg, stream);
				stream.flush();
			} catch (IOException e) {
				throw new CommunicationException(
						"could not send message to DB", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
	 */
	public void configure(Configuration arg0) throws ConfigurationException {
		logger.debug("configuring");
		//TODO: this should work other way.
		defaultEncoding = Encoding.getEncoding(arg0.getChild(
				"database-encoding").getValue(), arg0.getChild(
				"passed-encoding").getValue());
		port = arg0.getChild("port").getValueAsInteger();
		socketFactoryName = arg0.getChild("socket-factory-name").getValue();
		logger.debug("configured");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.activity.Initializable#initialize()
	 */
	public void initialize() throws Exception {
		logger.debug("listening at " + port);
		serverSocket = serverSocketFactory.createServerSocket(port);
		//init messageFactories
		messageFactoryMap.put(new Character(
				(char) ErrorMessageFactory.MESSAGE_HEADER_ERROR),
				new ErrorMessageFactory(logger));
		messageFactoryMap.put(new Character(
				(char) CallMessageFactory.MESSAGE_HEADER_CALL),
				new CallMessageFactory(typeMapper, logger));
		messageFactoryMap.put(new Character(
				(char) LogMessageFactory.MESSAGE_HEADER_LOG),
				new LogMessageFactory(logger));
		messageFactoryMap.put(new Character(
				(char) ResultMessageFactory.MESSAGE_HEADER_RESULT),
				new ResultMessageFactory(logger, typeMapper));
		messageFactoryMap
				.put(
						new Character(
								(char) TriggerCallRequestMessageFactory.MESSAGE_HEADER_TRIGGER),
						new TriggerCallRequestMessageFactory(logger, typeMapper));
		messageFactoryMap.put(new Character(
				(char) TupleResultMessageFactory.MESSAGE_HEADER_TUPLERESULT),
				new TupleResultMessageFactory());
		messageFactoryMap.put(new Character(
				(char) SQLMessageFactory.MESSAGE_HEADER_SQL),
				new SQLMessageFactory(logger));
	}
	private ServerSocketFactory serverSocketFactory;

	/**
	 * @see org.apache.avalon.framework.service.Serviceable#service(org.apache.avalon.framework.service.ServiceManager)
	 * @avalon.dependency key="socket-manager" type="org.apache.avalon.cornerstone.services.sockets.SocketManager"
	 * @avalon.dependency key="type-mapper" type="org.pgj.typemapping.TypeMapper"
	 * 
	 * @dna.dependency key="socket-manager" type="org.apache.avalon.cornerstone.services.sockets.SocketManager" optional="false"
	 * @dna.dependency key="type-mapper" type="org.pgj.typemapping.TypeMapper" optional="false"
	 * 
	 */
	public void service(ServiceManager arg0) throws ServiceException {
		logger.debug("servicing");
		SocketManager manager = (SocketManager) arg0.lookup("socket-manager");
		try {
			serverSocketFactory = manager
					.getServerSocketFactory("plain" /* socketFactoryName */);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), "", e);
		}
		typeMapper = (TypeMapper) arg0.lookup("type-mapper");
		logger.debug("serviced");
	}
	private Logger logger = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.LogEnabled#enableLogging(org.apache.avalon.framework.logger.Logger)
	 */
	public void enableLogging(Logger arg0) {
		logger = arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.activity.Startable#start()
	 */
	public void start() throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.activity.Startable#stop()
	 */
	public void stop() throws Exception {
		logger.warn("closing connection");
		serverSocket.close();
	}
}