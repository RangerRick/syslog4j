package org.productivity.java.syslog4j.impl.message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.productivity.java.syslog4j.SyslogMessageIF;

/**
 * AbstractSyslogMessage provides support for turning POJO (Plain Ol'
 * Java Objects) into Syslog messages.
 * 
 * <p>More information on the PCI DSS specification is available here:</p>
 * 
 * <p>https://www.pcisecuritystandards.org/security_standards/pci_dss.shtml</p>
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: AbstractSyslogMessage.java,v 1.2 2009/04/17 02:37:04 cvs Exp $
 */
public abstract class AbstractSyslogMessage implements SyslogMessageIF {
	private static final long serialVersionUID = -6663220650989046939L;

	public static final String UNDEFINED = "undefined";

	public static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

	public static final char DEFAULT_DELIMITER = ' ';
	public static final String DEFAULT_REPLACE_DELIMITER = "_";

	protected char getDelimiter() {
		return DEFAULT_DELIMITER;
	}

	protected String getReplaceDelimiter() {
		return DEFAULT_REPLACE_DELIMITER;
	}

	protected String getDateFormat() {
		return DEFAULT_DATE_FORMAT;
	}

	protected String getTimeFormat() {
		return DEFAULT_TIME_FORMAT;
	}

	protected String generateDate() {
		return new SimpleDateFormat(getDateFormat()).format(new Date());
	}

	protected String generateTime() {
		return new SimpleDateFormat(getTimeFormat()).format(new Date());
	}

	protected String[] generateDateAndTime(final Date date) {
		final String[] dateAndTime = new String[2];

		dateAndTime[0] = new SimpleDateFormat(getDateFormat()).format(date);
		dateAndTime[1] = new SimpleDateFormat(getTimeFormat()).format(date);

		return dateAndTime;
	}

	protected String generateLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException uhe) {
			//
		}
		return UNDEFINED;
	}

	protected boolean nullOrEmpty(final String value) {
		return (value == null || "".equals(value.trim()));
	}

	protected String replaceDelimiter(final String fieldName, final String fieldValue, final char delimiter, final String replaceDelimiter) {
		if (replaceDelimiter == null || replaceDelimiter.length() < 1 || fieldValue == null || fieldValue.length() < 1) {
			return fieldValue;
		}

		return fieldValue.replaceAll("\\" + delimiter, replaceDelimiter);
	}

	public abstract String createMessage();
}
