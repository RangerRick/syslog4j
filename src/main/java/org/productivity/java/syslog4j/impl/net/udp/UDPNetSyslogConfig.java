package org.productivity.java.syslog4j.impl.net.udp;

import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.impl.net.AbstractNetSyslogConfig;

/**
* UDPNetSyslogConfig is an extension of AbstractNetSyslogConfig that provides
* configuration support for UDP/IP-based syslog clients.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: UDPNetSyslogConfig.java,v 1.6 2008/11/14 04:32:00 cvs Exp $
*/
public class UDPNetSyslogConfig extends AbstractNetSyslogConfig {
	private static final long serialVersionUID = -9195573331600151173L;

	public UDPNetSyslogConfig() {
		super();
	}

	public UDPNetSyslogConfig(final int facility, final String host, final int port) {
		super(facility,host,port);
	}

	public UDPNetSyslogConfig(final int facility, final String host) {
		super(facility,host);
	}

	public UDPNetSyslogConfig(final int facility) {
		super(facility);
	}

	public UDPNetSyslogConfig(final String host, final int port) {
		super(host,port);
	}

	public UDPNetSyslogConfig(final String host) {
		super(host);
	}

	public Class<? extends SyslogIF> getSyslogClass() {
		return UDPNetSyslog.class;
	}
}
