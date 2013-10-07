package org.productivity.java.syslog4j.server.impl;

import java.util.ArrayList;
import java.util.List;

import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;

/**
 * AbstractSyslogServerConfig provides a base abstract implementation of the SyslogServerConfigIF
 * configuration interface.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: AbstractSyslogServerConfig.java,v 1.6 2009/07/22 15:54:23 cvs Exp $
 */
public abstract class AbstractSyslogServerConfig implements SyslogServerConfigIF {
	private static final long serialVersionUID = -215164495715299196L;

	public abstract Class<? extends SyslogServerIF> getSyslogServerClass();

	protected String charSet = CHAR_SET_DEFAULT;

	protected int shutdownWait = SyslogConstants.SERVER_SHUTDOWN_WAIT_DEFAULT;

	protected List<SyslogServerEventHandlerIF> eventHandlers = new ArrayList<SyslogServerEventHandlerIF>();

	protected boolean useStructuredData = USE_STRUCTURED_DATA_DEFAULT;

	public String getCharSet() {
		return this.charSet;
	}

	public void setCharSet(final String charSet) {
		this.charSet = charSet;
	}

	public int getShutdownWait() {
		return this.shutdownWait;
	}

	public void setShutdownWait(final int shutdownWait) {
		this.shutdownWait = shutdownWait;
	}

	public List<SyslogServerEventHandlerIF> getEventHandlers() {
		return this.eventHandlers;
	}

	public void addEventHandler(final SyslogServerEventHandlerIF eventHandler) {
		this.eventHandlers.add(eventHandler);
	}

	public void insertEventHandler(final int pos, final SyslogServerEventHandlerIF eventHandler) {
		this.eventHandlers.add(pos, eventHandler);
	}

	public void removeEventHandler(final SyslogServerEventHandlerIF eventHandler) {
		this.eventHandlers.remove(eventHandler);
	}

	public void removeAllEventHandlers() {
		this.eventHandlers.clear();
	}

	public boolean isUseStructuredData() {
		return useStructuredData;
	}

	public void setUseStructuredData(final boolean useStructuredData) {
		this.useStructuredData = useStructuredData;
	}	
}
