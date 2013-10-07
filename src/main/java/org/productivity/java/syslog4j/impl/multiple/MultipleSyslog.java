package org.productivity.java.syslog4j.impl.multiple;

import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageIF;
import org.productivity.java.syslog4j.SyslogMessageProcessorIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;

/**
 * MultipleSyslog is an aggregator Syslog implementation for allowing a single
 * Syslog call to send to multiple Syslog implementations.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: MultipleSyslog.java,v 1.9 2009/07/22 15:54:23 cvs Exp $
 */
public class MultipleSyslog implements SyslogIF {
	private static final long serialVersionUID = -1751449551390496609L;

	protected String syslogProtocol = null;
	protected MultipleSyslogConfig multipleSyslogConfig = null;

	public void initialize(final String protocol, final SyslogConfigIF config) throws SyslogRuntimeException {
		this.syslogProtocol = protocol;

		try {
			this.multipleSyslogConfig = (MultipleSyslogConfig) config;
		} catch (final ClassCastException cce) {
			throw new SyslogRuntimeException("config must be of type MultipleSyslogConfig");
		}
	}

	public SyslogConfigIF getConfig() {
		return this.multipleSyslogConfig;
	}

	public void debug(final String message) {
		log(SyslogConstants.LEVEL_DEBUG,message);
	}

	public void debug(final SyslogMessageIF message) {
		log(SyslogConstants.LEVEL_DEBUG,message);
	}

	public void critical(final String message) {
		log(SyslogConstants.LEVEL_CRITICAL,message);
	}

	public void critical(final SyslogMessageIF message) {
		log(SyslogConstants.LEVEL_CRITICAL,message);
	}

	public void error(final String message) {
		log(SyslogConstants.LEVEL_ERROR,message);
	}

	public void error(final SyslogMessageIF message) {
		log(SyslogConstants.LEVEL_ERROR,message);
	}

	public void alert(final String message) {
		log(SyslogConstants.LEVEL_ALERT,message);
	}

	public void alert(final SyslogMessageIF message) {
		log(SyslogConstants.LEVEL_ALERT,message);
	}

	public void notice(final String message) {
		log(SyslogConstants.LEVEL_NOTICE,message);
	}

	public void notice(final SyslogMessageIF message) {
		log(SyslogConstants.LEVEL_NOTICE,message);
	}

	public void emergency(final String message) {
		log(SyslogConstants.LEVEL_EMERGENCY,message);
	}

	public void emergency(final SyslogMessageIF message) {
		log(SyslogConstants.LEVEL_EMERGENCY,message);
	}

	public void info(final String message) {
		log(SyslogConstants.LEVEL_INFO,message);
	}

	public void info(final SyslogMessageIF message) {
		log(SyslogConstants.LEVEL_INFO,message);
	}

	public void warn(final String message) {
		log(SyslogConstants.LEVEL_WARN,message);
	}

	public void warn(final SyslogMessageIF message) {
		log(SyslogConstants.LEVEL_WARN,message);
	}

	public void log(final int level, final String message) {
		for(int i=0; i<this.multipleSyslogConfig.getProtocols().size(); i++) {
			final String protocol = this.multipleSyslogConfig.getProtocols().get(i);
			final SyslogIF syslog = Syslog.getInstance(protocol);

			syslog.log(level,message);
		}
	}

	public void log(final int level, final SyslogMessageIF message) {
		for(int i=0; i<this.multipleSyslogConfig.getProtocols().size(); i++) {
			final String protocol = this.multipleSyslogConfig.getProtocols().get(i);
			final SyslogIF syslog = Syslog.getInstance(protocol);

			syslog.log(level,message);
		}
	}

	public void flush() throws SyslogRuntimeException {
		for(int i=0; i<this.multipleSyslogConfig.getProtocols().size(); i++) {
			final String protocol = this.multipleSyslogConfig.getProtocols().get(i);
			final SyslogIF syslog = Syslog.getInstance(protocol);

			syslog.flush();
		}
	}

	public void shutdown() throws SyslogRuntimeException {
		for(int i=0; i<this.multipleSyslogConfig.getProtocols().size(); i++) {
			final String protocol = this.multipleSyslogConfig.getProtocols().get(i);
			final SyslogIF syslog = Syslog.getInstance(protocol);

			syslog.shutdown();
		}
	}

	public void backLog(final int level, final String message, final Throwable reasonThrowable) {
		// MultipleSyslog is an aggregator; backLog state will be handled by individual Syslog protocols
	}

	public void backLog(final int level, final String message, final String reason) {
		// MultipleSyslog is an aggregator; backLog state will be handled by individual Syslog protocols
	}

	public void setMessageProcessor(final SyslogMessageProcessorIF messageProcessor) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public SyslogMessageProcessorIF getMessageProcessor() {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public String getProtocol() {
		return this.syslogProtocol;
	}
}
