package org.productivity.java.syslog4j.impl.message.modifier.text;

import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageModifierIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;

/**
 * StringCaseSyslogMessageModifier is an implementation of SyslogMessageModifierIF
 * that provides support for shifting a Syslog message to all upper case or all
 * lower case.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: StringCaseSyslogMessageModifier.java,v 1.1 2008/11/12 14:30:53 cvs Exp $
 */
public class StringCaseSyslogMessageModifier implements SyslogMessageModifierIF {
	private static final long serialVersionUID = -8296541340078494285L;

	public static final byte LOWER_CASE = 0; 
	public static final byte UPPER_CASE = 1; 

	public static final StringCaseSyslogMessageModifier LOWER = new StringCaseSyslogMessageModifier(LOWER_CASE);
	public static final StringCaseSyslogMessageModifier UPPER = new StringCaseSyslogMessageModifier(UPPER_CASE);

	protected byte stringCase = LOWER_CASE;  

	public StringCaseSyslogMessageModifier(final byte stringCase) {
		this.stringCase = stringCase;

		if (stringCase < LOWER_CASE || stringCase > UPPER_CASE) {
			throw new SyslogRuntimeException("stringCase must be LOWER_CASE (0) or UPPER_CASE (1)");
		}
	}

	public String modify(final SyslogIF syslog, final SyslogConfigIF syslogConfig, final int facility, final int level, final String message) {
		if (message != null) {
			if (this.stringCase == LOWER_CASE) {
				return message.toLowerCase();

			} else if (this.stringCase == UPPER_CASE) {
				return message.toUpperCase();
			}
		}

		return message;
	}
}
