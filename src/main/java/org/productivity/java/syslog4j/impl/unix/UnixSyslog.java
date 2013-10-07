package org.productivity.java.syslog4j.impl.unix;

import org.productivity.java.syslog4j.SyslogMessageProcessorIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.impl.AbstractSyslog;
import org.productivity.java.syslog4j.impl.AbstractSyslogWriter;
import org.productivity.java.syslog4j.util.OSDetectUtility;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * UnixSyslog is an extension of AbstractSyslog that provides support for
 * Unix-based syslog clients.
 * 
 * <p>This class requires the JNA (Java Native Access) library to directly
 * access the native C libraries utilized on Unix platforms.</p>
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: UnixSyslog.java,v 1.24 2009/07/22 15:54:23 cvs Exp $
 */
public class UnixSyslog extends AbstractSyslog {
	private static final long serialVersionUID = 8963867437780176934L;
	protected UnixSyslogConfig unixSyslogConfig = null; 

	protected interface CLibrary extends Library {
		public void openlog(String ident, int option, int facility);
		public void syslog(int priority, String format, String message);
		public void closelog();
	}

	protected static int currentFacility = -1;
	protected static boolean openlogCalled = false;

	protected static CLibrary libraryInstance = null;

	protected static synchronized void loadLibrary(final UnixSyslogConfig config) throws SyslogRuntimeException {
		if (!OSDetectUtility.isUnix()) {
			throw new SyslogRuntimeException("UnixSyslog not supported on non-Unix platforms");
		}

		if (libraryInstance == null) {
			libraryInstance = (CLibrary) Native.loadLibrary(config.getLibrary(),CLibrary.class);
		}
	}

	public void initialize() throws SyslogRuntimeException {
		try {
			this.unixSyslogConfig = (UnixSyslogConfig) this.syslogConfig;
		} catch (final ClassCastException cce) {
			throw new SyslogRuntimeException("config must be of type UnixSyslogConfig");
		}

		loadLibrary(this.unixSyslogConfig);	
	}

	protected static void write(final int level, final String message, final UnixSyslogConfig config) throws SyslogRuntimeException {
		synchronized(libraryInstance) {
			if (currentFacility != config.getFacility()) {
				if (openlogCalled) {
					libraryInstance.closelog();
					openlogCalled = false;
				}

				currentFacility = config.getFacility();
			}

			if (!openlogCalled) {
				String ident = config.getIdent();

				if (ident != null && "".equals(ident.trim())) {
					ident = null;
				}

				libraryInstance.openlog(ident,config.getOption(),currentFacility);
				openlogCalled = true;
			}

			final int priority = currentFacility | level;
			libraryInstance.syslog(priority,"%s",message);
		}
	}

	protected void write(final byte[] message) throws SyslogRuntimeException {
		// NO-OP
	}

	public void log(final SyslogMessageProcessorIF messageProcessor, final int level, final String message) {
		write(level,message,this.unixSyslogConfig);
	}

	public void flush() throws SyslogRuntimeException {
		synchronized(libraryInstance) {
			libraryInstance.closelog();
			openlogCalled = false;
		}
	}

	public void shutdown() throws SyslogRuntimeException {
		flush();
	}

	public AbstractSyslogWriter getWriter() {
		return null;
	}

	public void returnWriter(final AbstractSyslogWriter syslogWriter) {
	}

	protected String prefixMessage(final String message, final String suffix) {
		return message;
	}
}
