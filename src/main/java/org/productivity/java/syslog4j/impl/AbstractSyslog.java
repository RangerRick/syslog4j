package org.productivity.java.syslog4j.impl;

import java.util.ArrayList;
import java.util.List;

import org.productivity.java.syslog4j.SyslogBackLogHandlerIF;
import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageIF;
import org.productivity.java.syslog4j.SyslogMessageModifierIF;
import org.productivity.java.syslog4j.SyslogMessageProcessorIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.impl.message.processor.SyslogMessageProcessor;
import org.productivity.java.syslog4j.impl.message.processor.structured.StructuredSyslogMessageProcessor;
import org.productivity.java.syslog4j.impl.message.structured.StructuredSyslogMessage;
import org.productivity.java.syslog4j.impl.message.structured.StructuredSyslogMessageIF;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
 * AbstractSyslog provides a base abstract implementation of the SyslogIF.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: AbstractSyslog.java,v 1.22 2009/07/25 18:42:47 cvs Exp $
 */
public abstract class AbstractSyslog implements SyslogIF {
	private static final long serialVersionUID = 2172748338818145012L;

	protected String syslogProtocol = null;

	protected AbstractSyslogConfigIF syslogConfig = null;

	protected SyslogMessageProcessorIF syslogMessageProcessor = null;
	protected SyslogMessageProcessorIF structuredSyslogMessageProcessor = null;

	protected Object backLogStatusSyncObject = new Object();

	protected boolean backLogStatus = false;
	protected List<SyslogBackLogHandlerIF> notifiedBackLogHandlers = new ArrayList<SyslogBackLogHandlerIF>();

	protected boolean getBackLogStatus() {
		synchronized (this.backLogStatusSyncObject) {
			return this.backLogStatus;
		}
	}

	/**
	 * @param backLogStatus - true if in a "down" backLog state, false if in an "up" (operational) non-backLog state
	 */
	public void setBackLogStatus(final boolean backLogStatus) {
		if (this.backLogStatus != backLogStatus) {
			synchronized (this.backLogStatusSyncObject) {
				if (!backLogStatus) {
					for(int i=0; i<this.notifiedBackLogHandlers.size(); i++) {
						((SyslogBackLogHandlerIF) this.notifiedBackLogHandlers.get(i)).up(this);
					}

					this.notifiedBackLogHandlers.clear();
				}

				this.backLogStatus = backLogStatus;
			}
		}
	}

	public void initialize(final String protocol, final SyslogConfigIF config) throws SyslogRuntimeException {
		this.syslogProtocol = protocol;

		try {
			this.syslogConfig = (AbstractSyslogConfigIF) config;
		} catch (final ClassCastException cce) {
			throw new SyslogRuntimeException("provided config must implement AbstractSyslogConfigIF");
		}

		initialize();
	}

	public SyslogMessageProcessorIF getMessageProcessor() {
		if (this.syslogMessageProcessor == null) {
			this.syslogMessageProcessor = SyslogMessageProcessor.getDefault();
		}

		return this.syslogMessageProcessor;
	}

	public SyslogMessageProcessorIF getStructuredMessageProcessor() {
		if (this.structuredSyslogMessageProcessor == null) {
			this.structuredSyslogMessageProcessor = StructuredSyslogMessageProcessor.getDefault();
		}

		return this.structuredSyslogMessageProcessor;
	}

	public void setMessageProcessor(final SyslogMessageProcessorIF messageProcessor) {
		this.syslogMessageProcessor = messageProcessor;
	}

	public void setStructuredMessageProcessor(final SyslogMessageProcessorIF messageProcessor) {
		this.structuredSyslogMessageProcessor = messageProcessor;
	}

	public String getProtocol() {
		return this.syslogProtocol;
	}

	public AbstractSyslogConfigIF getConfig() {
		return this.syslogConfig;
	}

	public void log(final int level, final String message) {
		if (this.syslogConfig.isUseStructuredData()) {
			final StructuredSyslogMessageIF structuredMessage = new StructuredSyslogMessage(null,null,message);
			log(getStructuredMessageProcessor(),level,structuredMessage.createMessage());
		}

		log(getMessageProcessor(),level,message);
	}

	public void log(final int level, final SyslogMessageIF message) {
		if (message instanceof StructuredSyslogMessageIF) {
			log(getStructuredMessageProcessor(),level,message.createMessage());
		} else {
			log(getMessageProcessor(),level,message.createMessage());
		}
	}

	public void debug(final String message) {
		log(LEVEL_DEBUG,message);
	}

	public void notice(final String message) {
		log(LEVEL_NOTICE,message);
	}

	public void info(final String message) {
		log(LEVEL_INFO,message);
	}

	public void warn(final String message) {
		log(LEVEL_WARN,message);
	}

	public void error(final String message) {
		log(LEVEL_ERROR,message);
	}

	public void critical(final String message) {
		log(LEVEL_CRITICAL,message);
	}

	public void alert(final String message) {
		log(LEVEL_ALERT,message);
	}

	public void emergency(final String message) {
		log(LEVEL_EMERGENCY,message);
	}

	public void debug(final SyslogMessageIF message) {
		log(LEVEL_DEBUG,message);
	}

	public void notice(final SyslogMessageIF message) {
		log(LEVEL_NOTICE,message);
	}

	public void info(final SyslogMessageIF message) {
		log(LEVEL_INFO,message);
	}

	public void warn(final SyslogMessageIF message) {
		log(LEVEL_WARN,message);
	}

	public void error(final SyslogMessageIF message) {
		log(LEVEL_ERROR,message);
	}

	public void critical(final SyslogMessageIF message) {
		log(LEVEL_CRITICAL,message);
	}

	public void alert(final SyslogMessageIF message) {
		log(LEVEL_ALERT,message);
	}

	public void emergency(final SyslogMessageIF message) {
		log(LEVEL_EMERGENCY,message);
	}

	protected String prefixMessage(final String message, final String suffix) {
		final String ident = this.syslogConfig.getIdent();
		return ((ident == null || "".equals(ident.trim())) ? "" : (ident + suffix)) + message;
	}

	public void log(final SyslogMessageProcessorIF messageProcessor, final int level, final String message) {
		String _message = null;

		if (this.syslogConfig.isIncludeIdentInMessageModifier()) {
			_message = prefixMessage(message,IDENT_SUFFIX_DEFAULT);
			_message = modifyMessage(level,_message);

		} else {
			_message = modifyMessage(level,message);
			_message = prefixMessage(_message,IDENT_SUFFIX_DEFAULT);
		}

		try {
			write(messageProcessor, level,_message);
		} catch (final SyslogRuntimeException sre) {
			if (sre.getCause() != null) {
				backLog(level,_message,sre.getCause());
			} else {
				backLog(level,_message,sre);
			}

			if (this.syslogConfig.isThrowExceptionOnWrite()) {
				throw sre;
			}
		}
	}

	protected void write(final SyslogMessageProcessorIF messageProcessor, final int level, final String message) throws SyslogRuntimeException {
		final String header = messageProcessor.createSyslogHeader(this.syslogConfig.getFacility(),level,this.syslogConfig.isSendLocalTimestamp(),this.syslogConfig.isSendLocalName());

		int messageLen = message.length();
		final int headerLen = header.length();
		final int availableLen = this.syslogConfig.getMaxMessageLength() - headerLen;

		final byte[] h = SyslogUtility.getBytes(this.syslogConfig,header);
		final byte[] m = SyslogUtility.getBytes(this.syslogConfig,message);

		if (this.syslogConfig.isTruncateMessage()) {
			if (availableLen > 0 && messageLen > availableLen) {
				messageLen = availableLen;
			}
		}

		if (messageLen <= availableLen) {
			write(messageProcessor.createPacketData(h,m,0,messageLen));

		} else {
			final byte[] splitBeginText = this.syslogConfig.getSplitMessageBeginText();
			final byte[] splitEndText = this.syslogConfig.getSplitMessageEndText();

			int pos = 0;
			int left = messageLen;

			while(left > 0) {
				final boolean firstTime = (pos == 0);

				final boolean doSplitBeginText = splitBeginText != null && !firstTime;
				final boolean doSplitEndText = splitBeginText != null && (firstTime || (left > (availableLen - splitBeginText.length)));

				int actualAvailableLen = availableLen;

				actualAvailableLen -= (splitBeginText != null && doSplitBeginText) ? splitBeginText.length : 0;
				actualAvailableLen -= (splitEndText != null && doSplitEndText) ? splitEndText.length : 0;

				if (actualAvailableLen > left) {
					actualAvailableLen = left;
				}

				if (actualAvailableLen < 0) {
					throw new SyslogRuntimeException("Message length < 0; recommendation: increase the size of maxMessageLength");
				}

				final byte[] data = messageProcessor.createPacketData(h,m,pos,actualAvailableLen,doSplitBeginText ? splitBeginText : null,doSplitEndText ? splitEndText : null);

				write(data);

				pos += actualAvailableLen;
				left -= actualAvailableLen;
			}
		}
	}

	protected abstract void initialize() throws SyslogRuntimeException;

	protected abstract void write(byte[] message) throws SyslogRuntimeException;

	protected String modifyMessage(final int level, final String message) {
		final List<SyslogMessageModifierIF> _messageModifiers = this.syslogConfig.getMessageModifiers();

		if (_messageModifiers == null || _messageModifiers.size() < 1) {
			return message;
		}

		String _message = message;

		final int facility = this.syslogConfig.getFacility();

		for(int i=0; i<_messageModifiers.size(); i++) {
			final SyslogMessageModifierIF messageModifier = (SyslogMessageModifierIF) _messageModifiers.get(i);
			_message = messageModifier.modify(this, this.syslogConfig, facility, level, _message);
		}

		return _message;
	}

	public void backLog(final int level, final String message, final Throwable reasonThrowable) {
		backLog(level,message,reasonThrowable != null ? reasonThrowable.toString() : "UNKNOWN");
	}

	public void backLog(final int level, final String message, final String reason) {
		final boolean status = getBackLogStatus();

		if (!status) {
			setBackLogStatus(true);
		}

		final List<SyslogBackLogHandlerIF> backLogHandlers = this.syslogConfig.getBackLogHandlers();

		for(int i=0; i<backLogHandlers.size(); i++) {
			final SyslogBackLogHandlerIF backLogHandler = (SyslogBackLogHandlerIF) backLogHandlers.get(i);

			try {
				if (!status) {
					backLogHandler.down(this, reason);
					this.notifiedBackLogHandlers.add(backLogHandler);
				}

				backLogHandler.log(this,level,message,reason);
				break;

			} catch (final Exception e) {
				// Ignore this Exception and go onto next backLogHandler
			}
		}
	}

	public abstract AbstractSyslogWriter getWriter();

	public abstract void returnWriter(AbstractSyslogWriter syslogWriter);

	public Thread createWriterThread(final AbstractSyslogWriter syslogWriter) {
		final Thread newWriterThread = new Thread(syslogWriter);
		syslogWriter.setThread(newWriterThread);
		newWriterThread.setName("SyslogWriter: " + getProtocol());
		newWriterThread.start();

		return newWriterThread;
	}


	public AbstractSyslogWriter createWriter(){
		final Class<? extends AbstractSyslogWriter> clazz = this.syslogConfig.getSyslogWriterClass();

		try {
			AbstractSyslogWriter newWriter = clazz.newInstance();
			newWriter.initialize(this);
			return newWriter;
		} catch (final InstantiationException e) {
			if (this.syslogConfig.isThrowExceptionOnInitialize()) {
				throw new SyslogRuntimeException(e);
			}
		} catch (final IllegalAccessException e) {
			if (this.syslogConfig.isThrowExceptionOnInitialize()) {
				throw new SyslogRuntimeException(e);
			}
		}

		return null;
	}
}
