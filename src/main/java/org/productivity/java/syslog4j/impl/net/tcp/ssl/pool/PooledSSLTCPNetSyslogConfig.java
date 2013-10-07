package org.productivity.java.syslog4j.impl.net.tcp.ssl.pool;

import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.impl.AbstractSyslogWriter;
import org.productivity.java.syslog4j.impl.net.tcp.pool.PooledTCPNetSyslogConfig;
import org.productivity.java.syslog4j.impl.net.tcp.ssl.SSLTCPNetSyslog;
import org.productivity.java.syslog4j.impl.net.tcp.ssl.SSLTCPNetSyslogConfigIF;
import org.productivity.java.syslog4j.impl.net.tcp.ssl.SSLTCPNetSyslogWriter;

/**
 * PooledSSLTCPNetSyslogConfig is an extension of PooledTCPNetSyslogConfig
 * which provides configuration support for the Apache Commons Pool.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: PooledSSLTCPNetSyslogConfig.java,v 1.2 2009/03/29 17:38:58 cvs Exp $
 */
public class PooledSSLTCPNetSyslogConfig extends PooledTCPNetSyslogConfig implements SSLTCPNetSyslogConfigIF {
	private static final long serialVersionUID = 8332665717604608110L;

	protected String keyStore = null;
	protected String keyStorePassword = null;

	protected String trustStore = null;
	protected String trustStorePassword = null;

	public PooledSSLTCPNetSyslogConfig() {
		super();
	}

	public PooledSSLTCPNetSyslogConfig(final int facility, final String host, final int port) {
		super(facility, host, port);
	}

	public PooledSSLTCPNetSyslogConfig(final int facility, final String host) {
		super(facility, host);
	}

	public PooledSSLTCPNetSyslogConfig(final int facility) {
		super(facility);
	}

	public PooledSSLTCPNetSyslogConfig(final String host, final int port) {
		super(host, port);
	}

	public PooledSSLTCPNetSyslogConfig(final String host) {
		super(host);
	}

	public String getKeyStore() {
		return this.keyStore;
	}

	public void setKeyStore(final String keyStore) {
		this.keyStore = keyStore;
	}

	public String getKeyStorePassword() {
		return this.keyStorePassword;
	}

	public void setKeyStorePassword(final String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public String getTrustStore() {
		return this.trustStore;
	}

	public void setTrustStore(final String trustStore) {
		this.trustStore = trustStore;
	}

	public String getTrustStorePassword() {
		return this.trustStorePassword;
	}

	public void setTrustStorePassword(final String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	public Class<? extends SyslogIF> getSyslogClass() {
		return SSLTCPNetSyslog.class;
	}

	public Class<? extends AbstractSyslogWriter> getSyslogWriterClass() {
		return SSLTCPNetSyslogWriter.class;
	}
}
