package org.productivity.java.syslog4j.impl.net.tcp;

import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.impl.AbstractSyslogWriter;
import org.productivity.java.syslog4j.impl.net.AbstractNetSyslogConfig;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
* TCPNetSyslogConfig is an extension of AbstractNetSyslogConfig that provides
* configuration support for TCP/IP-based syslog clients.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: TCPNetSyslogConfig.java,v 1.17 2009/03/29 17:38:58 cvs Exp $
*/
public class TCPNetSyslogConfig extends AbstractNetSyslogConfig implements TCPNetSyslogConfigIF {
	private static final long serialVersionUID = 4417463267744860981L;

	public static byte[] SYSTEM_DELIMITER_SEQUENCE = null;
	
	static {
		final String delimiterSequence = System.getProperty("line.separator");
		SYSTEM_DELIMITER_SEQUENCE = delimiterSequence.getBytes();
		
		if (SYSTEM_DELIMITER_SEQUENCE == null || SYSTEM_DELIMITER_SEQUENCE.length < 1) {
			SYSTEM_DELIMITER_SEQUENCE = SyslogConstants.TCP_DELIMITER_SEQUENCE_DEFAULT;
		}
	}
	
	protected byte[] delimiterSequence = SYSTEM_DELIMITER_SEQUENCE;
	
	protected boolean persistentConnection = TCP_PERSISTENT_CONNECTION_DEFAULT;
	
	protected boolean soLinger = TCP_SO_LINGER_DEFAULT;
	protected int soLingerSeconds = TCP_SO_LINGER_SECONDS_DEFAULT; 
	
	protected boolean keepAlive = TCP_KEEP_ALIVE_DEFAULT;
	
	protected boolean reuseAddress = TCP_REUSE_ADDRESS_DEFAULT;
	
	protected boolean setBufferSize = TCP_SET_BUFFER_SIZE_DEFAULT;
	
	public TCPNetSyslogConfig() {
		initialize();
	}
	
	protected void initialize() {
	}

	public TCPNetSyslogConfig(final int facility, final String host, final int port) {
		super(facility, host, port);
		initialize();
	}

	public TCPNetSyslogConfig(final int facility, final String host) {
		super(facility, host);
		initialize();
	}

	public TCPNetSyslogConfig(final int facility) {
		super(facility);
		initialize();
	}

	public TCPNetSyslogConfig(final String host, final int port) {
		super(host, port);
		initialize();
	}

	public TCPNetSyslogConfig(final String host) {
		super(host);
		initialize();
	}

	public Class<? extends SyslogIF> getSyslogClass() {
		return TCPNetSyslog.class;
	}
	
	public byte[] getDelimiterSequence() {
		return this.delimiterSequence;
	}

	public void setDelimiterSequence(final byte[] delimiterSequence) {
		this.delimiterSequence = delimiterSequence;
	}

	public void setDelimiterSequence(final String delimiterSequence) {
		this.delimiterSequence = SyslogUtility.getBytes(this,delimiterSequence);
	}

	public boolean isPersistentConnection() {
		return this.persistentConnection;
	}

	public void setPersistentConnection(final boolean persistentConnection) {
		this.persistentConnection = persistentConnection;
	}

	public boolean isSoLinger() {
		return this.soLinger;
	}

	public void setSoLinger(final boolean soLinger) {
		this.soLinger = soLinger;
	}

	public int getSoLingerSeconds() {
		return this.soLingerSeconds;
	}

	public void setSoLingerSeconds(final int soLingerSeconds) {
		this.soLingerSeconds = soLingerSeconds;
	}

	public boolean isKeepAlive() {
		return this.keepAlive;
	}

	public void setKeepAlive(final boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isReuseAddress() {
		return this.reuseAddress;
	}

	public void setReuseAddress(final boolean reuseAddress) {
		this.reuseAddress = reuseAddress;
	}

	public boolean isSetBufferSize() {
		return this.setBufferSize;
	}

	public void setSetBufferSize(final boolean setBufferSize) {
		this.setBufferSize = setBufferSize;
	}

	public Class<? extends AbstractSyslogWriter> getSyslogWriterClass() {
		return TCPNetSyslogWriter.class;
	}
}
