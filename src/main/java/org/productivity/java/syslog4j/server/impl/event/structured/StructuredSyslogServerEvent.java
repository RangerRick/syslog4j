package org.productivity.java.syslog4j.server.impl.event.structured;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.impl.message.structured.StructuredSyslogMessage;
import org.productivity.java.syslog4j.server.impl.event.SyslogServerEvent;

/**
 * SyslogServerStructuredEvent provides an implementation of the
 * SyslogServerEventIF interface that supports receiving of structured syslog
 * messages, as defined in:
 * 
 * <p>
 * http://tools.ietf.org/html/draft-ietf-syslog-protocol-23#section-6
 * </p>
 * 
 * <p>
 * Syslog4j is licensed under the Lesser GNU Public License v2.1. A copy of the
 * LGPL license is available in the META-INF folder in all distributions of
 * Syslog4j and in the base directory of the "doc" ZIP.
 * </p>
 * 
 * @author Manish Motwani
 * @version $Id: StructuredSyslogServerEvent.java,v 1.2 2009/07/25 18:42:47 cvs Exp $
 */
public class StructuredSyslogServerEvent extends SyslogServerEvent {
	private static final long serialVersionUID = 1676499796406044315L;

	private String applicationName = SyslogConstants.STRUCTURED_DATA_APP_NAME_DEFAULT_VALUE;
	private String processId = null;

	public StructuredSyslogServerEvent(byte[] message, int length, InetAddress inetAddress) {
		super(message, length, inetAddress);
	}

	protected void parseApplicationName() {
		int i = this.message.indexOf(' ');

		if (i > -1) {
			this.applicationName = this.message.substring(0, i).trim();
			this.message = this.message.substring(i + 1);
			parseProcessId();
		}

		if (SyslogConstants.STRUCTURED_DATA_NILVALUE.equals(this.applicationName))
			this.applicationName = null;
	}

	protected void parseProcessId() {
		int i = this.message.indexOf(' ');

		if (i > -1) {
			this.processId = this.message.substring(0, i).trim();
			this.message = this.message.substring(i + 1);
		}

		if (SyslogConstants.STRUCTURED_DATA_NILVALUE.equals(this.processId)) {
			this.processId = null;
		}
	}

	protected void parseDate() {
		// skip VERSION field
		int i = this.message.indexOf(' ');
		this.message = this.message.substring(i + 1);

		// parse the date
		i = this.message.indexOf(' ');

		if (i > -1) {
			String dateString = this.message.substring(0, i).trim();

			if (dateString.length() == 25 && dateString.lastIndexOf(':') == 22) {
				// Remove the colon from the timezone so it can be parsed by the
				// parser
				String finalDateStr = dateString.substring(0, 22)
						+ dateString.substring(23, 25);

				DateFormat dateFormat = new SimpleDateFormat(
						SyslogConstants.STRUCTURED_DATA_MESSAGE_DATEFORMAT);
				try {
					this.date = dateFormat.parse(finalDateStr);
				} catch (ParseException pe) {
					this.date = new Date();
				}

				this.message = this.message.substring(i + 1);
				
			} else {
				// Not structured date format, try super one
				super.parseDate();
				return;
			}
		}

		parseHost();
	}

	protected void parseHost() {
		int i = this.message.indexOf(' ');

		if (i > -1) {
			this.host = this.message.substring(0, i).trim();
			this.message = this.message.substring(i + 1);
		}

		if (SyslogConstants.STRUCTURED_DATA_NILVALUE.equals(this.host)) {
			this.host = null;
		}

		parseApplicationName();
	}

	public String getApplicationName() {
		return applicationName;
	}

	public String getProcessId() {
		return processId;
	}

	public StructuredSyslogMessage getStructuredMessage() {
		try {
			return StructuredSyslogMessage.fromString(getMessage());

		} catch (IllegalArgumentException e) {
			// throw new SyslogRuntimeException(
			// "Message received is not a valid structured message: "
			// + getMessage(), e);
			return new StructuredSyslogMessage(null,null,getMessage());
		}
	}
}
