package org.productivity.java.syslog4j.impl.multiple;

import java.util.ArrayList;
import java.util.List;

import org.productivity.java.syslog4j.SyslogBackLogHandlerIF;
import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.SyslogMessageModifierIF;
import org.productivity.java.syslog4j.SyslogRuntimeException;

/**
 * MultipleSyslogConfig is a configuration Object for allowing a single
 * Syslog call to send to multiple Syslog implementations.
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: MultipleSyslogConfig.java,v 1.7 2009/07/22 15:54:23 cvs Exp $
 */
public class MultipleSyslogConfig implements SyslogConfigIF {
	private static final long serialVersionUID = 4629036421093131756L;
	protected List<String> syslogProtocols = null;

	public MultipleSyslogConfig() {
		this.syslogProtocols = new ArrayList<String>();
	}

	public MultipleSyslogConfig(final List<String> protocols) {
		if (protocols != null) {
			this.syslogProtocols = protocols;

		} else {
			this.syslogProtocols = new ArrayList<String>();
		}
	}

	public MultipleSyslogConfig(final String[] protocols) {
		if (protocols != null) {
			this.syslogProtocols = new ArrayList<String>(protocols.length);

			for(int i=0; i<protocols.length; i++) {
				this.syslogProtocols.add(protocols[i]);
			}

		} else {
			this.syslogProtocols = new ArrayList<String>();
		}
	}

	public List<String> getProtocols() {
		return this.syslogProtocols;
	}

	public void addProtocol(final String protocol) {
		this.syslogProtocols.add(protocol);
	}

	public void insertProtocol(final int index, final String protocol) {
		this.syslogProtocols.add(index,protocol);
	}

	public void removeProtocol(final String protocol) {
		this.syslogProtocols.remove(protocol);
	}

	public void removeAllProtocols() {
		this.syslogProtocols.clear();
	}

	public void addBackLogHandler(final SyslogBackLogHandlerIF backLogHandler) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void addMessageModifier(final SyslogMessageModifierIF messageModifier) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public Class<? extends SyslogIF> getSyslogClass() {
		return MultipleSyslog.class;
	}

	public String getCharSet() {
		return SyslogConstants.CHAR_SET_DEFAULT;
	}

	public int getFacility() {
		return SyslogConstants.SYSLOG_FACILITY_DEFAULT;
	}

	public String getHost() {
		return SyslogConstants.SYSLOG_HOST_DEFAULT;
	}

	public String getIdent() {
		return null;
	}

	public int getPort() {
		return SyslogConstants.SYSLOG_PORT_DEFAULT;
	}

	public int getMaxShutdownWait() {
		return SyslogConstants.MAX_SHUTDOWN_WAIT_DEFAULT;
	}

	public void setMaxShutdownWait(final int maxShutdownWait) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void insertBackLogHandler(final int index, final SyslogBackLogHandlerIF backLogHandler) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void insertMessageModifier(final int index, final SyslogMessageModifierIF messageModifier) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public boolean isCacheHostAddress() {
		return SyslogConstants.CACHE_HOST_ADDRESS_DEFAULT;
	}

	public boolean isIncludeIdentInMessageModifier() {
		return SyslogConstants.INCLUDE_IDENT_IN_MESSAGE_MODIFIER_DEFAULT;
	}

	public boolean isSendLocalName() {
		return SyslogConstants.SEND_LOCAL_NAME_DEFAULT;
	}

	public boolean isSendLocalTimestamp() {
		return SyslogConstants.SEND_LOCAL_TIMESTAMP_DEFAULT;
	}

	public boolean isThrowExceptionOnInitialize() {
		return SyslogConstants.THROW_EXCEPTION_ON_INITIALIZE_DEFAULT;
	}

	public boolean isThrowExceptionOnWrite() {
		return SyslogConstants.THROW_EXCEPTION_ON_WRITE_DEFAULT;
	}

	public void removeAllBackLogHandlers() {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void removeAllMessageModifiers() {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void removeBackLogHandler(final SyslogBackLogHandlerIF backLogHandler) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void removeMessageModifier(final SyslogMessageModifierIF messageModifier) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setCacheHostAddress(final boolean cacheHostAddress) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setCharSet(final String charSet) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setFacility(final int facility) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setFacility(final String facilityName) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setHost(final String host) throws SyslogRuntimeException {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setIdent(final String ident) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setIncludeIdentInMessageModifier(final boolean throwExceptionOnInitialize) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setPort(final int port) throws SyslogRuntimeException {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setSendLocalName(final boolean sendLocalName) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setSendLocalTimestamp(final boolean sendLocalTimestamp) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setThrowExceptionOnInitialize(final boolean throwExceptionOnInitialize) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public void setThrowExceptionOnWrite(final boolean throwExceptionOnWrite) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public int getMaxMessageLength() {
		return SyslogConstants.MAX_MESSAGE_LENGTH_DEFAULT;
	}

	public void setMaxMessageLength(final int maxMessageLength) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public boolean isTruncateMessage() {
		return SyslogConstants.TRUNCATE_MESSAGE_DEFAULT;
	}

	public void setTruncateMessage(final boolean truncateMessage) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}

	public boolean isUseStructuredData() {
		return SyslogConstants.USE_STRUCTURED_DATA_DEFAULT;
	}

	public void setUseStructuredData(final boolean useStructuredData) {
		throw new SyslogRuntimeException("MultipleSyslog is an aggregator; please set the individual protocols");
	}
}
