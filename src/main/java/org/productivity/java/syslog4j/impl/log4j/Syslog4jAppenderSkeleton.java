package org.productivity.java.syslog4j.impl.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.impl.AbstractSyslogConfigIF;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
 * Syslog4jAppenderSkeleton provides an extensible Log4j Appender wrapper for Syslog4j.
 * 
 * <p>Classes which inherit Syslog4jAppenderSkeleton must implement the "initialize()" method,
 * which sets up Syslog4j for use by the Log4j Appender.</p>
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: Syslog4jAppenderSkeleton.java,v 1.5 2009/06/06 19:25:23 cvs Exp $
 */
public abstract class Syslog4jAppenderSkeleton extends AppenderSkeleton implements SyslogConstants {
	private static final long serialVersionUID = -3767028915396332270L;

	protected SyslogIF syslog = null;

	protected String ident = null;
	protected String protocol = null;
	protected String facility = null;
	protected String host = null;
	protected String port = null;
	protected String charSet = null;
	protected String threaded = null;
	protected String threadLoopInterval = null;
	protected String splitMessageBeginText = null;
	protected String splitMessageEndText = null;
	protected String maxMessageLength = null;
	protected String maxShutdownWait = null;
	protected String writeRetries = null;
	protected String truncateMessage = null;

	protected boolean initialized = false;

	public abstract String initialize() throws SyslogRuntimeException;

	protected static boolean isTrueOrOn(final String value) {
		if (value != null) {
			if ("true".equalsIgnoreCase(value.trim()) || "on".equalsIgnoreCase(value.trim())) {
				return true;

			} else if ("false".equalsIgnoreCase(value.trim()) || "off".equalsIgnoreCase(value.trim())) {
				return false;
			} else {
				LogLog.error("Value \"" + value + "\" not true, on, false, or off -- assuming false");
			}
		}

		return false;
	}

	public void _initialize() {
		final String initializedProtocol = initialize();

		if (initializedProtocol != null && this.protocol == null) {
			this.protocol = initializedProtocol;
		}

		if (this.protocol != null) {
			try {
				this.syslog = Syslog.getInstance(this.protocol);
				if (this.host != null) {
					this.syslog.getConfig().setHost(this.host);
				}
				if (this.facility != null) {
					this.syslog.getConfig().setFacility(SyslogUtility.getFacility(this.facility));
				}
				if (this.port != null) {
					try {
						final int i = Integer.parseInt(this.port);
						this.syslog.getConfig().setPort(i);
					} catch (final NumberFormatException nfe) {
						LogLog.error(nfe.toString());
					}
				}
				if (this.charSet != null) {
					this.syslog.getConfig().setCharSet(this.charSet);
				}
				if (this.ident != null) {
					this.syslog.getConfig().setIdent(this.ident);
				}
				if (this.truncateMessage != null && !"".equals(this.truncateMessage.trim())) {
					this.syslog.getConfig().setTruncateMessage(isTrueOrOn(this.truncateMessage));
				}
				if (this.maxMessageLength != null && this.maxMessageLength.length() > 0) {
					try {
						final int i = Integer.parseInt(this.maxMessageLength.trim());
						this.syslog.getConfig().setMaxMessageLength(i);						
					} catch (final NumberFormatException nfe) {
						LogLog.error(nfe.toString());
					}
				}
				if (this.syslog.getConfig() instanceof AbstractSyslogConfigIF) {
					final AbstractSyslogConfigIF abstractSyslogConfig = (AbstractSyslogConfigIF) this.syslog.getConfig();

					if (this.threaded != null && !"".equals(this.threaded.trim())) {
						abstractSyslogConfig.setThreaded(isTrueOrOn(this.threaded));
					}

					if (this.threadLoopInterval != null && this.threadLoopInterval.length() > 0) {
						try {
							final long l = Long.parseLong(this.threadLoopInterval.trim());
							abstractSyslogConfig.setThreadLoopInterval(l);

						} catch (final NumberFormatException nfe) {
							LogLog.error(nfe.toString());
						}
					}

					if (this.splitMessageBeginText != null) {
						abstractSyslogConfig.setSplitMessageBeginText(SyslogUtility.getBytes(abstractSyslogConfig,this.splitMessageBeginText));
					}

					if (this.splitMessageEndText != null) {
						abstractSyslogConfig.setSplitMessageEndText(SyslogUtility.getBytes(abstractSyslogConfig,this.splitMessageEndText));
					}

					if (this.maxShutdownWait != null && this.maxShutdownWait.length() > 0) {
						try {
							final int i = Integer.parseInt(this.maxShutdownWait.trim());
							abstractSyslogConfig.setMaxShutdownWait(i);
						} catch (final NumberFormatException nfe) {
							LogLog.error(nfe.toString());
						}
					}

					if (this.writeRetries != null && this.writeRetries.length() > 0) {
						try {
							final int i = Integer.parseInt(this.writeRetries.trim());
							abstractSyslogConfig.setWriteRetries(i);
						} catch (final NumberFormatException nfe) {
							LogLog.error(nfe.toString());
						}
					}
				}

				this.initialized = true;
			} catch (final SyslogRuntimeException sre) {
				LogLog.error(sre.toString());
			}
		}
	}

	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(final String protocol) {
		this.protocol = protocol;
	}

	protected void append(final LoggingEvent event) {
		if (!this.initialized) {
			_initialize();
		}

		if (this.initialized) {
			final int level = event.getLevel().getSyslogEquivalent();

			if (this.layout != null) {
				this.syslog.log(level,this.layout.format(event));
			} else {
				this.syslog.log(level,event.getRenderedMessage());
			}
		}
	}

	public void close() {
		if (this.syslog != null) {
			this.syslog.flush();
		}
	}

	public String getFacility() {
		return this.facility;
	}

	public void setFacility(final String facility) {
		this.facility = facility;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(final String port) {
		this.port = port;
	}

	public String getCharSet() {
		return this.charSet;
	}

	public void setCharSet(final String charSet) {
		this.charSet = charSet;
	}

	public String getIdent() {
		return this.ident;
	}

	public void setIdent(final String ident) {
		this.ident = ident;
	}

	public String getThreaded() {
		return this.threaded;
	}

	public void setThreaded(final String threaded) {
		this.threaded = threaded;
	}

	public boolean requiresLayout() {
		return false;
	}

	public String getThreadLoopInterval() {
		return this.threadLoopInterval;
	}

	public void setThreadLoopInterval(final String threadLoopInterval) {
		this.threadLoopInterval = threadLoopInterval;
	}

	public String getSplitMessageBeginText() {
		return this.splitMessageBeginText;
	}

	public void setSplitMessageBeginText(final String splitMessageBeginText) {
		this.splitMessageBeginText = splitMessageBeginText;
	}

	public String getSplitMessageEndText() {
		return this.splitMessageEndText;
	}

	public void setSplitMessageEndText(final String splitMessageEndText) {
		this.splitMessageEndText = splitMessageEndText;
	}

	public String getMaxMessageLength() {
		return this.maxMessageLength;
	}

	public void setMaxMessageLength(final String maxMessageLength) {
		this.maxMessageLength = maxMessageLength;
	}

	public String getMaxShutdownWait() {
		return this.maxShutdownWait;
	}

	public void setMaxShutdownWait(final String maxShutdownWait) {
		this.maxShutdownWait = maxShutdownWait;
	}

	public String getWriteRetries() {
		return this.writeRetries;
	}

	public void setWriteRetries(final String writeRetries) {
		this.writeRetries = writeRetries;
	}

	public String getTruncateMessage() {
		return this.truncateMessage;
	}

	public void setTruncateMessage(final String truncateMessage) {
		this.truncateMessage = truncateMessage;
	}
}
