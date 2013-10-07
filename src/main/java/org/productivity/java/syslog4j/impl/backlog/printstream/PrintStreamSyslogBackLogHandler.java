package org.productivity.java.syslog4j.impl.backlog.printstream;

import java.io.PrintStream;

import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.impl.backlog.AbstractSyslogBackLogHandler;

/**
 * PrintStreamSyslogBackLogHandler provides a last-chance mechanism to log messages that fail
 * (for whatever reason) within the rest of Syslog to a PrintStream.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: PrintStreamSyslogBackLogHandler.java,v 1.1 2009/01/24 22:00:18 cvs Exp $
 */
public class PrintStreamSyslogBackLogHandler extends AbstractSyslogBackLogHandler {
	protected PrintStream printStream = null;
	protected boolean appendLinefeed = false;

	public PrintStreamSyslogBackLogHandler(final PrintStream printStream) {
		this.printStream = printStream;

		initialize();
	}

	public PrintStreamSyslogBackLogHandler(final PrintStream printStream, final boolean appendLinefeed) {
		this.printStream = printStream;
		this.appendLinefeed = appendLinefeed;

		initialize();
	}

	public PrintStreamSyslogBackLogHandler(final PrintStream printStream, final boolean appendLinefeed, boolean appendReason) {
		this.printStream = printStream;
		this.appendLinefeed = appendLinefeed;
		this.appendReason = appendReason;

		initialize();
	}

	public void initialize() throws SyslogRuntimeException {
		if (this.printStream == null) {
			throw new SyslogRuntimeException("PrintStream cannot be null");
		}
	}

	public void down(final SyslogIF syslog, final String reason) {
		this.printStream.println(syslog.getProtocol() + ": DOWN" + (reason != null && !"".equals(reason.trim()) ? " (" + reason + ")" : ""));
	}

	public void up(final SyslogIF syslog) {
		this.printStream.println(syslog.getProtocol() + ": UP");
	}

	public void log(final SyslogIF syslog, final int level, final String message, final String reason) {
		final String combinedMessage = combine(syslog,level,message,reason);

		if (this.appendLinefeed) {
			this.printStream.println(combinedMessage);

		} else {
			this.printStream.print(combinedMessage);
		}
	}
}
