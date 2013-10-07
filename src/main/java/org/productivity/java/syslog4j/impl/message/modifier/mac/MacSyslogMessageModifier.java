package org.productivity.java.syslog4j.impl.message.modifier.mac;

import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Mac;

import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageModifierIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.util.Base64;
import org.productivity.java.syslog4j.util.SyslogUtility;

/**
 * MacSyslogMessageModifier is an implementation of SyslogMessageModifierIF
 * that provides support for Java Cryptographic signed hashes (HmacSHA1, etc.)
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: MacSyslogMessageModifier.java,v 1.3 2008/11/13 14:48:36 cvs Exp $
 */
public class MacSyslogMessageModifier implements SyslogMessageModifierIF {
	private static final long serialVersionUID = 7437022905854643762L;

	protected MacSyslogMessageModifierConfig config = null;

	protected Mac mac = null;

	public MacSyslogMessageModifier(final MacSyslogMessageModifierConfig config) throws SyslogRuntimeException {
		this.config = config;

		try {
			this.mac = Mac.getInstance(config.getMacAlgorithm());
			this.mac.init(config.getKey());
		} catch (GeneralSecurityException e) {
			throw new SyslogRuntimeException(e);
		}
	}

	public static MacSyslogMessageModifier createHmacSHA1(final Key key) {
		return new MacSyslogMessageModifier(MacSyslogMessageModifierConfig.createHmacSHA1(key));
	}

	public static MacSyslogMessageModifier createHmacSHA1(final String base64Key) {
		return new MacSyslogMessageModifier(MacSyslogMessageModifierConfig.createHmacSHA1(base64Key));
	}

	public static MacSyslogMessageModifier createHmacSHA256(final Key key) {
		return new MacSyslogMessageModifier(MacSyslogMessageModifierConfig.createHmacSHA256(key));
	}

	public static MacSyslogMessageModifier createHmacSHA256(final String base64Key) {
		return new MacSyslogMessageModifier(MacSyslogMessageModifierConfig.createHmacSHA256(base64Key));
	}

	public static MacSyslogMessageModifier createHmacSHA512(final Key key) {
		return new MacSyslogMessageModifier(MacSyslogMessageModifierConfig.createHmacSHA512(key));
	}

	public static MacSyslogMessageModifier createHmacSHA512(final String base64Key) {
		return new MacSyslogMessageModifier(MacSyslogMessageModifierConfig.createHmacSHA512(base64Key));
	}

	public static MacSyslogMessageModifier createHmacMD5(final Key key) {
		return new MacSyslogMessageModifier(MacSyslogMessageModifierConfig.createHmacMD5(key));
	}

	public static MacSyslogMessageModifier createHmacMD5(final String base64Key) {
		return new MacSyslogMessageModifier(MacSyslogMessageModifierConfig.createHmacMD5(base64Key));
	}

	public MacSyslogMessageModifierConfig getConfig() {
		return this.config;
	}

	public synchronized String modify(final SyslogIF syslog, final SyslogConfigIF syslogConfig, final int facility, final int level, final String message) {
		final byte[] messageBytes = SyslogUtility.getBytes(syslogConfig,message);
		final StringBuffer buffer = new StringBuffer(message);
		final byte[] macBytes = this.mac.doFinal(messageBytes);
		final String macString = Base64.encodeBytes(macBytes,Base64.DONT_BREAK_LINES);

		buffer.append(this.config.getPrefix());
		buffer.append(macString);
		buffer.append(this.config.getSuffix());

		return buffer.toString();
	}
}
