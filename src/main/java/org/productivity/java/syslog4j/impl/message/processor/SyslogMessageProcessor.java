package org.productivity.java.syslog4j.impl.message.processor;

import org.productivity.java.syslog4j.SyslogMessageProcessorIF;

/**
 * SyslogMessageProcessor wraps AbstractSyslogMessageProcessor.
 * 
 * <p>Those wishing to replace (or improve upon) this implementation
 * can write a custom SyslogMessageProcessorIF and set it per
 * instance via the SyslogIF.setMessageProcessor(..) method or set it globally
 * via the SyslogMessageProcessor.setDefault(..) method.</p>
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: SyslogMessageProcessor.java,v 1.6 2009/07/22 15:54:23 cvs Exp $
 */
public class SyslogMessageProcessor extends AbstractSyslogMessageProcessor {
	private static final long serialVersionUID = -5644793707211410841L;

	private static final SyslogMessageProcessorIF INSTANCE = new SyslogMessageProcessor();

	protected static SyslogMessageProcessorIF defaultInstance = INSTANCE;

	public static void setDefault(final SyslogMessageProcessorIF messageProcessor) {
		if (messageProcessor != null) {
			defaultInstance = messageProcessor;
		}
	}

	public static SyslogMessageProcessorIF getDefault() {
		return defaultInstance;
	}
}
