package org.productivity.java.syslog4j.impl.message.modifier.text;

import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageModifierIF;

/**
* SuffixSyslogMessageModifier is an implementation of SyslogMessageModifierIF
* that provides support for adding static text to the end of a Syslog message.
* 
* <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
* of the LGPL license is available in the META-INF folder in all
* distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
* 
* @author &lt;syslog4j@productivity.org&gt;
* @version $Id: SuffixSyslogMessageModifier.java,v 1.3 2008/11/14 04:31:59 cvs Exp $
*/
public class SuffixSyslogMessageModifier implements SyslogMessageModifierIF {
	private static final long serialVersionUID = 7620910883030615540L;
	protected String suffix = null;
	protected String delimiter = " ";

	public SuffixSyslogMessageModifier() {
		//
	}

	public SuffixSyslogMessageModifier(final String suffix) {
		this.suffix = suffix;
	}

	public SuffixSyslogMessageModifier(final String suffix, final String delimiter) {
		this.suffix = suffix;
		if (delimiter != null) {
			this.delimiter = delimiter;
		}
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	public String modify(final SyslogIF syslog, final SyslogConfigIF syslogConfig, final int facility, final int level, final String message) {
		if (this.suffix == null || "".equals(this.suffix.trim())) {
			return message;
		}

		return message + this.delimiter + this.suffix;
	}
}
