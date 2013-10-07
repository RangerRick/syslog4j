package org.productivity.java.syslog4j.server;

import java.util.Hashtable;
import java.util.Map;

import org.productivity.java.syslog4j.Syslog4jVersion;
import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfig;
import org.productivity.java.syslog4j.server.impl.net.udp.UDPNetSyslogServerConfig;

/**
 * This class provides a Singleton-based interface for Syslog4j
 * server implementations.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: SyslogServer.java,v 1.11 2009/04/17 02:37:04 cvs Exp $
 */
public class SyslogServer implements SyslogConstants {
	private static final long serialVersionUID = 4350291000082204925L;

	protected static final Map<String, SyslogServerIF> instances = new Hashtable<String, SyslogServerIF>();

	static {
		initialize();
	}

	private SyslogServer() {
	}

	/**
	 * @return Returns the current version identifier for Syslog4j.
	 */
	public static final String getVersion() {
		return Syslog4jVersion.VERSION;
	}

	public static final SyslogServerIF getInstance(final String protocol) throws SyslogRuntimeException {
		final String syslogProtocol = protocol.toLowerCase();

		if (instances.containsKey(syslogProtocol)) {
			return instances.get(syslogProtocol);

		} else {
			throw new SyslogRuntimeException("SyslogServer instance \"" + syslogProtocol + "\" not defined; use \"tcp\" or \"udp\" or call SyslogServer.createInstance(protocol,config) first");
		}
	}

	public static final SyslogServerIF getThreadedInstance(final String protocol) throws SyslogRuntimeException {
		final SyslogServerIF server = getInstance(protocol);

		if (server.getThread() == null) {
			final Thread thread = new Thread(server);
			thread.setName("SyslogServer: " + protocol);
			server.setThread(thread);
			thread.start();
		}

		return server;
	}

	public static final boolean exists(final String protocol) {
		if (protocol == null || "".equals(protocol.trim())) {
			return false;
		}

		return instances.containsKey(protocol.toLowerCase());
	}

	public static final SyslogServerIF createInstance(final String protocol, final SyslogServerConfigIF config) throws SyslogRuntimeException {
		if (protocol == null || "".equals(protocol.trim())) {
			throw new SyslogRuntimeException("Instance protocol cannot be null or empty");
		}

		if (config == null) {
			throw new SyslogRuntimeException("SyslogServerConfig cannot be null");
		}

		final String syslogProtocol = protocol.toLowerCase();
		SyslogServerIF syslogServer = null;

		synchronized(instances) {
			if (instances.containsKey(syslogProtocol)) {
				throw new SyslogRuntimeException("SyslogServer instance \"" + syslogProtocol + "\" already defined.");
			}

			try {
				final Class<? extends SyslogServerIF> syslogClass = config.getSyslogServerClass();
				syslogServer = syslogClass.newInstance();
			} catch (final Exception e) {
				throw new SyslogRuntimeException(e);
			}

			syslogServer.initialize(syslogProtocol,config);

			instances.put(syslogProtocol,syslogServer);
		}

		return syslogServer;
	}

	public static final SyslogServerIF createThreadedInstance(final String protocol, final SyslogServerConfigIF config) throws SyslogRuntimeException {
		createInstance(protocol,config);
		return getThreadedInstance(protocol);
	}

	public synchronized static void initialize() {
		createInstance(UDP,new UDPNetSyslogServerConfig());
		createInstance(TCP,new TCPNetSyslogServerConfig());
	}

	public synchronized static final void shutdown() throws SyslogRuntimeException {
		for (final SyslogServerIF syslogServer : instances.values()) {
			syslogServer.shutdown();
		}
		instances.clear();
	}

	public static void main(final String[] args) throws Exception {
		SyslogServerMain.main(args);
	}
}
