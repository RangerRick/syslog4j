package org.productivity.java.syslog4j.impl.message.modifier.checksum;

import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.productivity.java.syslog4j.impl.message.modifier.AbstractSyslogMessageModifierConfig;

/**
 * ChecksumSyslogMessageModifierConfig is an implementation of AbstractSyslogMessageModifierConfig
 * that provides configuration for ChecksumSyslogMessageModifier.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: ChecksumSyslogMessageModifierConfig.java,v 1.1 2008/11/10 04:38:37 cvs Exp $
 */
public class ChecksumSyslogMessageModifierConfig extends AbstractSyslogMessageModifierConfig {
	private static final long serialVersionUID = -3355109603143491962L;
	protected Checksum checksum = null;

	public static final ChecksumSyslogMessageModifierConfig createCRC32() {
		return new ChecksumSyslogMessageModifierConfig(new CRC32());
	}

	public static final ChecksumSyslogMessageModifierConfig createADLER32() {
		return new ChecksumSyslogMessageModifierConfig(new Adler32());
	}

	public ChecksumSyslogMessageModifierConfig(final Checksum checksum) {
		this.checksum = checksum;
	}

	public Checksum getChecksum() {
		return this.checksum;
	}

	public void setChecksum(final Checksum checksum) {
		this.checksum = checksum;
	}
}
