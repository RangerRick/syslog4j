package org.productivity.java.syslog4j.impl;

import java.util.ArrayList;
import java.util.List;

import org.productivity.java.syslog4j.SyslogBackLogHandlerIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageModifierIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.impl.backlog.printstream.SystemErrSyslogBackLogHandler;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
 * AbstractSyslog provides a base abstract implementation of the SyslogConfigIF
 * configuration interface.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: AbstractSyslogConfig.java,v 1.19 2009/07/22 15:54:23 cvs Exp $
 */
public abstract class AbstractSyslogConfig implements AbstractSyslogConfigIF {
	private static final long serialVersionUID = -3675957966842092272L;

	protected final static List<SyslogBackLogHandlerIF> defaultBackLogHandlers = new ArrayList<SyslogBackLogHandlerIF>();

	static {
		defaultBackLogHandlers.add(new SystemErrSyslogBackLogHandler());
	}

	protected int facility = SYSLOG_FACILITY_DEFAULT;

	protected String charSet = CHAR_SET_DEFAULT;

	protected String ident = "";

	protected boolean sendLocalTimestamp = SEND_LOCAL_TIMESTAMP_DEFAULT;
	protected boolean sendLocalName = SEND_LOCAL_NAME_DEFAULT;

	protected boolean includeIdentInMessageModifier = INCLUDE_IDENT_IN_MESSAGE_MODIFIER_DEFAULT;
	protected boolean throwExceptionOnWrite = THROW_EXCEPTION_ON_WRITE_DEFAULT;
	protected boolean throwExceptionOnInitialize = THROW_EXCEPTION_ON_INITIALIZE_DEFAULT;

	protected int maxMessageLength = MAX_MESSAGE_LENGTH_DEFAULT;
	protected byte[] splitMessageBeginText = SPLIT_MESSAGE_BEGIN_TEXT_DEFAULT.getBytes();
	protected byte[] splitMessageEndText = SPLIT_MESSAGE_END_TEXT_DEFAULT.getBytes();

	protected List<SyslogMessageModifierIF> messageModifiers = null;
	protected List<SyslogBackLogHandlerIF> backLogHandlers = null;

	protected boolean threaded = THREADED_DEFAULT;
	protected long threadLoopInterval = THREAD_LOOP_INTERVAL_DEFAULT;

	protected int writeRetries = WRITE_RETRIES_DEFAULT;
	protected int maxShutdownWait = MAX_SHUTDOWN_WAIT_DEFAULT;

	protected boolean truncateMessage = TRUNCATE_MESSAGE_DEFAULT;
	protected boolean useStructuredData = USE_STRUCTURED_DATA_DEFAULT;

	public abstract Class<? extends SyslogIF> getSyslogClass();

	public String getCharSet() {
		return this.charSet;
	}

	public void setCharSet(final String charSet) {
		this.charSet = charSet;
	}

	public boolean isThrowExceptionOnWrite() {
		return this.throwExceptionOnWrite;
	}

	public void setThrowExceptionOnWrite(final boolean throwExceptionOnWrite) {
		this.throwExceptionOnWrite = throwExceptionOnWrite;
	}

	public boolean isThrowExceptionOnInitialize() {
		return this.throwExceptionOnInitialize;
	}

	public void setThrowExceptionOnInitialize(final boolean throwExceptionOnInitialize) {
		this.throwExceptionOnInitialize = throwExceptionOnInitialize;
	}

	public byte[] getSplitMessageBeginText() {
		return this.splitMessageBeginText;
	}

	public void setSplitMessageBeginText(final byte[] splitMessageBeginText) {
		this.splitMessageBeginText = splitMessageBeginText;
	}

	public void setSplitMessageBeginText(final String splitMessageBeginText) throws SyslogRuntimeException {
		this.splitMessageBeginText = SyslogUtility.getBytes(this,splitMessageBeginText);
	}

	public byte[] getSplitMessageEndText() {
		return this.splitMessageEndText;
	}

	public void setSplitMessageEndText(final byte[] splitMessageEndText) {
		this.splitMessageEndText = splitMessageEndText;
	}

	public void setSplitMessageEndText(final String splitMessageEndText) throws SyslogRuntimeException {
		this.splitMessageEndText = SyslogUtility.getBytes(this,splitMessageEndText);
	}

	public int getMaxMessageLength() {
		return this.maxMessageLength;
	}

	public void setMaxMessageLength(final int maxMessageLength) {
		this.maxMessageLength = maxMessageLength;
	}

	public boolean isSendLocalTimestamp() {
		return this.sendLocalTimestamp;
	}

	public void setSendLocalTimestamp(final boolean sendLocalTimestamp) {
		this.sendLocalTimestamp = sendLocalTimestamp;
	}

	public boolean isSendLocalName() {
		return this.sendLocalName;
	}

	public void setSendLocalName(final boolean sendLocalName) {
		this.sendLocalName = sendLocalName;
	}

	public int getFacility() {
		return this.facility;
	}

	public void setFacility(final int facility) {
		this.facility = facility;
	}

	public void setFacility(final String facilityName) {
		this.facility = SyslogUtility.getFacility(facilityName);
	}

	public String getIdent() {
		return this.ident;
	}

	public void setIdent(final String ident) {
		this.ident = ident;
	}

	protected synchronized List<SyslogMessageModifierIF> _getMessageModifiers() {
		if (this.messageModifiers == null) {
			this.messageModifiers = new ArrayList<SyslogMessageModifierIF>();
		}

		return this.messageModifiers;
	}

	public void addMessageModifier(final SyslogMessageModifierIF messageModifier) {
		if (messageModifier == null) {
			return;
		}

		final List<SyslogMessageModifierIF> _messageModifiers = _getMessageModifiers();

		synchronized (_messageModifiers) {
			_messageModifiers.add(messageModifier);
		}
	}

	public void insertMessageModifier(final int index, final SyslogMessageModifierIF messageModifier) {
		if (messageModifier == null) {
			return;
		}

		final List<SyslogMessageModifierIF> _messageModifiers = _getMessageModifiers();

		synchronized (_messageModifiers) {
			try {
				_messageModifiers.add(index,messageModifier);
			} catch (final IndexOutOfBoundsException ioobe) {
				throw new SyslogRuntimeException(ioobe);
			}
		}
	}

	public void removeMessageModifier(final SyslogMessageModifierIF messageModifier) {
		if (messageModifier == null) {
			return;
		}

		final List<SyslogMessageModifierIF> _messageModifiers = _getMessageModifiers();

		synchronized (_messageModifiers) {
			_messageModifiers.remove(messageModifier);
		}
	}

	public List<SyslogMessageModifierIF> getMessageModifiers() {
		return this.messageModifiers;
	}

	public void setMessageModifiers(final List<SyslogMessageModifierIF> messageModifiers) {
		this.messageModifiers = messageModifiers;
	}	

	public void removeAllMessageModifiers() {
		if (this.messageModifiers == null || this.messageModifiers.isEmpty()) {
			return;
		}

		this.messageModifiers.clear();
	}

	protected synchronized List<SyslogBackLogHandlerIF> _getBackLogHandlers() {
		if (this.backLogHandlers == null) {
			this.backLogHandlers = new ArrayList<SyslogBackLogHandlerIF>();
		}

		return this.backLogHandlers;
	}

	public void addBackLogHandler(final SyslogBackLogHandlerIF backLogHandler) {
		if (backLogHandler == null) {
			return;
		}

		final List<SyslogBackLogHandlerIF> _backLogHandlers = _getBackLogHandlers();

		synchronized (_backLogHandlers) {
			_backLogHandlers.add(backLogHandler);
		}
	}

	public void insertBackLogHandler(final int index, final SyslogBackLogHandlerIF backLogHandler) {
		if (backLogHandler == null) {
			return;
		}

		final List<SyslogBackLogHandlerIF> _backLogHandlers = _getBackLogHandlers();

		synchronized (_backLogHandlers) {
			try {
				_backLogHandlers.add(index,backLogHandler);
			} catch (final IndexOutOfBoundsException ioobe) {
				throw new SyslogRuntimeException(ioobe);
			}
		}
	}

	public void removeBackLogHandler(final SyslogBackLogHandlerIF backLogHandler) {
		if (backLogHandler == null) {
			return;
		}

		final List<SyslogBackLogHandlerIF> _backLogHandlers = _getBackLogHandlers();

		synchronized (_backLogHandlers) {
			_backLogHandlers.remove(backLogHandler);
		}
	}

	public List<SyslogBackLogHandlerIF> getBackLogHandlers() {
		if (this.backLogHandlers == null || this.backLogHandlers.size() < 1) {
			return defaultBackLogHandlers;
		}

		return this.backLogHandlers;
	}

	public void setBackLogHandlers(final List<SyslogBackLogHandlerIF> backLogHandlers) {
		this.backLogHandlers = backLogHandlers;
	}	

	public void removeAllBackLogHandlers() {
		if (this.backLogHandlers == null || this.backLogHandlers.isEmpty()) {
			return;
		}

		this.backLogHandlers.clear();
	}

	public boolean isIncludeIdentInMessageModifier() {
		return this.includeIdentInMessageModifier;
	}

	public void setIncludeIdentInMessageModifier(final boolean includeIdentInMessageModifier) {
		this.includeIdentInMessageModifier = includeIdentInMessageModifier;
	}

	public boolean isThreaded() {
		return this.threaded;
	}

	public void setThreaded(final boolean threaded) {
		this.threaded = threaded;
	}

	public long getThreadLoopInterval() {
		return this.threadLoopInterval;
	}

	public void setThreadLoopInterval(final long threadLoopInterval) {
		this.threadLoopInterval = threadLoopInterval;
	}

	public int getMaxShutdownWait() {
		return this.maxShutdownWait;
	}

	public void setMaxShutdownWait(final int maxShutdownWait) {
		this.maxShutdownWait = maxShutdownWait;
	}

	public int getWriteRetries() {
		return this.writeRetries;
	}

	public void setWriteRetries(final int writeRetries) {
		this.writeRetries = writeRetries;
	}

	public boolean isTruncateMessage() {
		return this.truncateMessage;
	}

	public void setTruncateMessage(final boolean truncateMessage) {
		this.truncateMessage = truncateMessage;
	}

	public boolean isUseStructuredData() {
		return this.useStructuredData;
	}

	public void setUseStructuredData(final boolean useStructuredData) {
		this.useStructuredData = useStructuredData;
	}

	public Class<? extends AbstractSyslogWriter> getSyslogWriterClass() {
		return null;
	}
}
