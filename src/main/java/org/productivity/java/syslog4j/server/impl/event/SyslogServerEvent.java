package org.productivity.java.syslog4j.server.impl.event;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
 * SyslogServerEvent provides an implementation of the SyslogServerEventIF interface.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: SyslogServerEvent.java,v 1.3 2009/04/10 00:05:02 cvs Exp $
 */
public class SyslogServerEvent implements SyslogServerEventIF {
	private static final long serialVersionUID = -5173009646610124827L;

	public static final String DATE_FORMAT = "MMM dd HH:mm:ss yyyy";

	protected String charSet = SyslogConstants.CHAR_SET_DEFAULT;
	protected byte[] raw = null;
	protected Date date = null;
	protected int level = -1;
	protected int facility = -1;
	protected String host = null;
	protected String message = null;
	protected InetAddress inetAddress = null;

	public SyslogServerEvent(final byte[] message, final int length, final InetAddress inetAddress) {
		this.inetAddress = inetAddress;
		this.raw = new byte[length];
		System.arraycopy(message,0,this.raw,0,length);

		parse();
	}

	protected void parseHost() {
		final int i = this.message.indexOf(' ');

		String hostAddress = null;
		String hostName = null;

		if (i > -1) {
			final String providedHost = this.message.substring(0,i).trim();
			hostAddress = this.inetAddress.getHostAddress();

			if (providedHost.equalsIgnoreCase(hostAddress)) {
				this.host = hostAddress;
				this.message = this.message.substring(i+1);
			}

			if (this.host == null) {
				hostName = this.inetAddress.getHostName();

				if (providedHost.equalsIgnoreCase(hostName)) {
					this.host = hostName;
					this.message = this.message.substring(i+1);
				}
			}

			if (this.host == null) {
				this.host = (hostName != null) ? hostName : hostAddress;
			}

		}
	}

	protected void parseDate() {
		if (this.message.length() >= 16 && this.message.charAt(3) == ' ' && this.message.charAt(6) == ' ') {
			final String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
			final String originalDate = this.message.substring(0,15) + " " + year;
			final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

			try {
				this.date = dateFormat.parse(originalDate);
				this.message = this.message.substring(16);
			} catch (ParseException pe) {
				this.date = new Date();
			}
		}

		parseHost();
	}

	protected void parsePriority() {
		if (this.message.charAt(0) == '<') {
			int i = this.message.indexOf(">"); 

			if (i <= 4 && i > -1) {
				final String priorityStr = this.message.substring(1,i);

				int priority = 0;
				try {
					priority = Integer.parseInt(priorityStr);
					this.facility = (priority >> 3) << 3;
					this.level = priority - this.facility;

					this.message = this.message.substring(i+1);

					parseDate();

				} catch (final NumberFormatException nfe) {
					//
				}
			}
		}
	}

	protected void parse() {
		this.message = SyslogUtility.newString(this,this.raw);

		parsePriority();
	}

	public int getFacility() {
		return this.facility;
	}

	public void setFacility(final int facility) {
		this.facility = facility;
	}

	public byte[] getRaw() {
		return this.raw;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCharSet() {
		return this.charSet;
	}

	public void setCharSet(final String charSet) {
		this.charSet = charSet;
	}
}
