package org.productivity.java.syslog4j.impl.net.tcp.pool;

import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.impl.AbstractSyslogWriter;
import org.productivity.java.syslog4j.impl.net.tcp.TCPNetSyslog;
import org.productivity.java.syslog4j.impl.pool.AbstractSyslogPoolFactory;
import org.productivity.java.syslog4j.impl.pool.generic.GenericSyslogPoolFactory;

/**
* PooledTCPNetSyslog is an extension of TCPNetSyslog which provides support
* for Apache Commons Pool.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: PooledTCPNetSyslog.java,v 1.5 2008/12/10 04:30:15 cvs Exp $
*/
public class PooledTCPNetSyslog extends TCPNetSyslog {
	private static final long serialVersionUID = -7326179264489675177L;
	protected AbstractSyslogPoolFactory poolFactory = null;

	public void initialize() throws SyslogRuntimeException {
		super.initialize();
		this.poolFactory = createSyslogPoolFactory();
		this.poolFactory.initialize(this);
	}
	
	protected AbstractSyslogPoolFactory createSyslogPoolFactory() {
		return new GenericSyslogPoolFactory();
	}

	public AbstractSyslogWriter getWriter() {
		try {
			return this.poolFactory.borrowSyslogWriter();
		} catch (final Exception e) {
			throw new SyslogRuntimeException(e);
		}
	}

	public void returnWriter(final AbstractSyslogWriter syslogWriter) {
		try {
			this.poolFactory.returnSyslogWriter(syslogWriter);
		} catch (final Exception e) {
			throw new SyslogRuntimeException(e);
		}
	}

	public void flush() throws SyslogRuntimeException {
		try {
			this.poolFactory.clear();
		} catch (final Exception e) {
			//
		}
	}

	public void shutdown() throws SyslogRuntimeException {
		try {
			this.poolFactory.close();
			
		} catch (final Exception e) {
			//
		}
	}
}
