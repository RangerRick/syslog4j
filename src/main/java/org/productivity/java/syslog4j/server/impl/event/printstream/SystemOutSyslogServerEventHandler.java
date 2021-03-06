package org.productivity.java.syslog4j.server.impl.event.printstream;

import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;

public class SystemOutSyslogServerEventHandler extends PrintStreamSyslogServerEventHandler {
	private static final long serialVersionUID = 1690551409588182601L;

	public static SyslogServerEventHandlerIF create() {
		return new SystemOutSyslogServerEventHandler();
	}
	
	public SystemOutSyslogServerEventHandler() {
		super(System.out);
	}
}
