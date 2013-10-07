package org.productivity.java.syslog4j.server.impl;

import java.net.InetAddress;

import org.productivity.java.syslog4j.SyslogCharSetIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.impl.event.SyslogServerEvent;
import org.productivity.java.syslog4j.server.impl.event.structured.StructuredSyslogServerEvent;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
 * AbstractSyslogServer provides a base abstract implementation of the SyslogServerIF.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: AbstractSyslogServer.java,v 1.7 2009/07/22 15:54:23 cvs Exp $
 */
public abstract class AbstractSyslogServer implements SyslogServerIF {
	protected String syslogProtocol = null;
	protected AbstractSyslogServerConfig syslogServerConfig = null;
	protected Thread thread = null;

	protected boolean shutdown = false;

	public void initialize(final String protocol, final SyslogServerConfigIF config) throws SyslogRuntimeException {
		this.syslogProtocol = protocol;

		try {
			this.syslogServerConfig = (AbstractSyslogServerConfig) config;
		} catch (final ClassCastException cce) {
			throw new SyslogRuntimeException(cce);
		}

		initialize();
	}

	public String getProtocol() {
		return this.syslogProtocol;
	}

	public SyslogServerConfigIF getConfig() {
		return this.syslogServerConfig;
	}

	protected abstract void initialize() throws SyslogRuntimeException;

	public void shutdown() throws SyslogRuntimeException {
		this.shutdown = true;
	}

	public Thread getThread() {
		return this.thread;
	}

	public void setThread(final Thread thread) {
		this.thread = thread;
	}

	protected static boolean isStructuredMessage(final SyslogCharSetIF syslogCharSet, final byte[] receiveData) {
		final String msg = SyslogUtility.newString(syslogCharSet, receiveData);
		final int idx = msg.indexOf('>');

		if (idx != -1) {
			// If there's a numerical VERSION field after the <priority>, return true.
			if (msg.length() > idx + 1 && Character.isDigit(msg.charAt(idx + 1))) {
				return true;
			}
		}

		return false;
	}

	protected static SyslogServerEventIF createEvent(final SyslogServerConfigIF serverConfig, final byte[] lineBytes, final int lineBytesLength, final InetAddress inetAddr) {
		if (serverConfig.isUseStructuredData() && AbstractSyslogServer.isStructuredMessage(serverConfig,lineBytes)) {
			return new StructuredSyslogServerEvent(lineBytes,lineBytesLength,inetAddr);
		} else {
			return new SyslogServerEvent(lineBytes,lineBytesLength,inetAddr);
		}		
	}
}
