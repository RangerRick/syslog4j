package org.productivity.java.syslog4j.impl.message.modifier.sequential;

import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageModifierIF;

/**
 * SequentialSyslogMessageModifier is an implementation of SyslogMessageModifierIF
 * that adds an incremented number at the end.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: SequentialSyslogMessageModifier.java,v 1.5 2009/03/29 17:38:58 cvs Exp $
 */
public class SequentialSyslogMessageModifier implements SyslogMessageModifierIF {
	private static final long serialVersionUID = 2989017637870074162L;

	protected SequentialSyslogMessageModifierConfig config = null;

	protected long currentSequence[] = new long[LEVEL_DEBUG + 1];

	public static final SequentialSyslogMessageModifier createDefault() {
		return new SequentialSyslogMessageModifier(SequentialSyslogMessageModifierConfig.createDefault());
	}

	public SequentialSyslogMessageModifier(final SequentialSyslogMessageModifierConfig config) {
		this.config = config;

		for(int i=0; i<(LEVEL_DEBUG + 1); i++) {
			this.currentSequence[i] = config.getFirstNumber();
		}
	}

	protected String pad(final long number) {
		final StringBuffer buffer = new StringBuffer(Long.toString(number));

		while (buffer.length() < this.config.getLastNumberDigits()) {
			buffer.insert(0,this.config.getPadChar());
		}

		return buffer.toString();
	}

	public void setNextSequence(final int level, final long nextSequence) {
		if (nextSequence >= this.config.getFirstNumber() && nextSequence < this.config.getLastNumber()) {
			synchronized (this) {
				this.currentSequence[level] = nextSequence;
			}
		}
	}

	protected String nextSequence(final int level) {
		long sequence = -1;

		synchronized(this) {
			sequence = this.currentSequence[level];

			if (this.currentSequence[level] >= this.config.getLastNumber()) {
				this.currentSequence[level] = this.config.getFirstNumber();

			} else {
				this.currentSequence[level]++;
			}
		}

		String _sequence = null;

		if (this.config.isUsePadding()) {
			_sequence = pad(sequence);

		} else {
			_sequence = Long.toString(sequence);
		}

		return _sequence;
	}

	public SequentialSyslogMessageModifierConfig getConfig() {
		return this.config;
	}

	public String modify(final SyslogIF syslog, final SyslogConfigIF syslogConfig, final int facility, final int level, final String message) {
		final StringBuffer buffer = new StringBuffer(message);

		buffer.append(this.config.getPrefix());
		buffer.append(nextSequence(level));
		buffer.append(this.config.getSuffix());

		return buffer.toString();
	}
}
