package org.productivity.java.syslog4j.impl.message.modifier.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageModifierIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.util.Base64;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
 * HashSyslogMessageModifier is an implementation of SyslogMessageModifierIF
 * that provides support for Java Cryptographic hashes (MD5, SHA1, SHA256, etc.).
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: HashSyslogMessageModifier.java,v 1.3 2008/11/21 15:29:15 cvs Exp $
 */
public class HashSyslogMessageModifier implements SyslogMessageModifierIF {
	private static final long serialVersionUID = -2822805778987840429L;

	protected HashSyslogMessageModifierConfig config = null;

	public static final HashSyslogMessageModifier createMD5() {
		return new HashSyslogMessageModifier(HashSyslogMessageModifierConfig.createMD5());
	}

	public static final HashSyslogMessageModifier createSHA1() {
		return new HashSyslogMessageModifier(HashSyslogMessageModifierConfig.createSHA1());
	}

	public static final HashSyslogMessageModifier createSHA160() {
		return createSHA1();
	}

	public static final HashSyslogMessageModifier createSHA256() {
		return new HashSyslogMessageModifier(HashSyslogMessageModifierConfig.createSHA256());
	}

	public static final HashSyslogMessageModifier createSHA384() {
		return new HashSyslogMessageModifier(HashSyslogMessageModifierConfig.createSHA384());
	}

	public static final HashSyslogMessageModifier createSHA512() {
		return new HashSyslogMessageModifier(HashSyslogMessageModifierConfig.createSHA512());
	}

	public HashSyslogMessageModifier(final HashSyslogMessageModifierConfig config) throws SyslogRuntimeException {
		this.config = config;

		if (this.config == null) {
			throw new SyslogRuntimeException("Hash config object cannot be null");			
		}

		if (this.config.getHashAlgorithm() == null) {
			throw new SyslogRuntimeException("Hash algorithm cannot be null");			
		}

		try {
			MessageDigest.getInstance(config.getHashAlgorithm());
		} catch (final NoSuchAlgorithmException nsae) {
			throw new SyslogRuntimeException(nsae);			
		}
	}

	protected MessageDigest obtainMessageDigest() {
		MessageDigest digest = null;

		try {
			digest = MessageDigest.getInstance(this.config.getHashAlgorithm());
		} catch (final NoSuchAlgorithmException nsae) {
			throw new SyslogRuntimeException(nsae);
		}

		return digest;
	}

	public HashSyslogMessageModifierConfig getConfig() {
		return this.config;
	}

	public String modify(final SyslogIF syslog, final SyslogConfigIF syslogConfig, final int facility, final int level, final String message) {
		final byte[] messageBytes = SyslogUtility.getBytes(syslogConfig,message);

		final MessageDigest digest = obtainMessageDigest();
		final byte[] digestBytes = digest.digest(messageBytes);

		final String digestString = Base64.encodeBytes(digestBytes,Base64.DONT_BREAK_LINES);

		final StringBuffer buffer = new StringBuffer(message);

		buffer.append(this.config.getPrefix());
		buffer.append(digestString);
		buffer.append(this.config.getSuffix());

		return buffer.toString();
	}
}
