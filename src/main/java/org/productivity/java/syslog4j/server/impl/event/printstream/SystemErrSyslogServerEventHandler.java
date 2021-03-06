package org.productivity.java.syslog4j.server.impl.event.printstream;

import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;

public class SystemErrSyslogServerEventHandler extends PrintStreamSyslogServerEventHandler {
	private static final long serialVersionUID = 2289382437617938598L;

	public static SyslogServerEventHandlerIF create() {
		return new SystemErrSyslogServerEventHandler();
	}
	
	public SystemErrSyslogServerEventHandler() {
		super(System.err);
	}
}
