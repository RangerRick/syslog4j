package org.productivity.java.syslog4j.impl.net.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;

import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.SyslogRuntimeException;
import org.productivity.java.syslog4j.impl.AbstractSyslog;
import org.productivity.java.syslog4j.impl.AbstractSyslogWriter;

/**
 * TCPNetSyslogWriter is an implementation of Runnable that supports sending
 * TCP-based messages within a separate Thread.
 * 
 * <p>When used in "threaded" mode (see TCPNetSyslogConfig for the option),
 * a queuing mechanism is used (via LinkedList).</p>
 * 
 * <p>Syslog4j is licensed under the Lesser GNU Public License v2.1.  A copy
 * of the LGPL license is available in the META-INF folder in all
 * distributions of Syslog4j and in the base directory of the "doc" ZIP.</p>
 * 
 * @author &lt;syslog4j@productivity.org&gt;
 * @version $Id: TCPNetSyslogWriter.java,v 1.15 2009/03/29 17:38:58 cvs Exp $
 */
public class TCPNetSyslogWriter extends AbstractSyslogWriter {
	private static final long serialVersionUID = 3235729770589454149L;

	protected TCPNetSyslog tcpNetSyslog = null;

	protected Socket socket = null;

	protected TCPNetSyslogConfigIF tcpNetSyslogConfig = null;

	public TCPNetSyslogWriter() {
		//
	}

	public void initialize(final AbstractSyslog abstractSyslog) {
		super.initialize(abstractSyslog);
		this.tcpNetSyslog = (TCPNetSyslog) abstractSyslog;
		this.tcpNetSyslogConfig = (TCPNetSyslogConfigIF) this.tcpNetSyslog.getConfig();
	}

	protected SocketFactory obtainSocketFactory() {
		return SocketFactory.getDefault();
	}

	protected Socket createSocket(final InetAddress hostAddress, final int port, final boolean keepalive) throws IOException {
		final SocketFactory socketFactory = obtainSocketFactory();
		final Socket newSocket = socketFactory.createSocket(hostAddress,port);

		if (this.tcpNetSyslogConfig.isSoLinger()) {
			newSocket.setSoLinger(true,this.tcpNetSyslogConfig.getSoLingerSeconds());
		}
		if (this.tcpNetSyslogConfig.isKeepAlive()) {
			newSocket.setKeepAlive(keepalive);
		}
		if (this.tcpNetSyslogConfig.isReuseAddress()) {
			newSocket.setReuseAddress(true);
		}

		return newSocket;
	}

	protected Socket getSocket() throws SyslogRuntimeException {
		if (this.socket != null && this.socket.isConnected()) {
			return this.socket;

		} else {
			try {
				final InetAddress hostAddress = this.tcpNetSyslog.getHostAddress();
				this.socket = createSocket(hostAddress,this.syslog.getConfig().getPort(),this.tcpNetSyslogConfig.isPersistentConnection());
			} catch (final IOException ioe) {
				throw new SyslogRuntimeException(ioe);
			}
		}

		return this.socket;
	}

	protected void closeSocket(final Socket socketToClose) {
		if (socketToClose == null) {
			return;
		}

		try {
			socketToClose.close();
		} catch (final IOException ioe) {
			if (!"Socket is closed".equalsIgnoreCase(ioe.getMessage())) {
				throw new SyslogRuntimeException(ioe);
			}
		} finally {
			if (socketToClose == this.socket) {
				this.socket = null;
			}
		}
	}

	public void write(final byte[] message) throws SyslogRuntimeException {
		Socket currentSocket = null;

		int attempts = 0;
		while(attempts != -1 && attempts < (this.tcpNetSyslogConfig.getWriteRetries() + 1)) {
			OutputStream os = null;
			try {
				currentSocket = getSocket();
				if (currentSocket == null) {
					throw new SyslogRuntimeException("No socket available");
				}

				os = currentSocket.getOutputStream();

				if (this.tcpNetSyslogConfig.isSetBufferSize()) {
					currentSocket.setSendBufferSize(message.length);
				}

				os.write(message);

				final byte[] delimiterSequence = this.tcpNetSyslogConfig.getDelimiterSequence();
				if (delimiterSequence != null && delimiterSequence.length > 0) {
					os.write(delimiterSequence);
				}

				this.syslog.setBackLogStatus(false);

				attempts = -1;

				if (!this.tcpNetSyslogConfig.isPersistentConnection()) {
					closeSocket(currentSocket);
				}
			} catch (final IOException ioe) {
				if (ioe.getMessage().toLowerCase().indexOf("software caused connection abort") > -1) {
					attempts = this.tcpNetSyslogConfig.getWriteRetries();
					closeSocket(currentSocket);
				}

				attempts++;
				closeSocket(currentSocket);

				if (attempts >= (this.tcpNetSyslogConfig.getWriteRetries() + 1)) {
					throw new SyslogRuntimeException(ioe);
				}
			} finally {
				if (!this.tcpNetSyslogConfig.isPersistentConnection()) {
					try {
						os.close();
					} catch (final IOException e) {
						//
					}
				}
			}
		}
	}

	public synchronized void flush() throws SyslogRuntimeException {
		if (this.socket == null) {
			return;
		}

		if (this.syslogConfig.isThreaded()) {
			this.shutdown();
			this.syslog.createWriterThread(this);

		} else {
			closeSocket(this.socket);
		}
	}

	public synchronized void shutdown() throws SyslogRuntimeException {
		if (this.socket == null) {
			return;
		}

		if (this.syslogConfig.isThreaded()) {
			this.shutdown = true;

			final long timeStart = System.currentTimeMillis();
			boolean done = false;

			while(!done) {
				if (this.socket == null || this.socket.isClosed()) {
					done = true;

				} else {
					final long now = System.currentTimeMillis();

					if (now > (timeStart + this.tcpNetSyslogConfig.getMaxShutdownWait())) {
						closeSocket(this.socket);
						this.thread.interrupt();
						done = true;
					}

					if (!done) {
						try {
							Thread.sleep(SyslogConstants.SHUTDOWN_INTERVAL);
						} catch (final InterruptedException ie) {
							//
						}
					}
				}
			}

		} else {
			closeSocket(this.socket);
		}
	}

	protected void runCompleted() {
		closeSocket(this.socket);
	}
}
