package org.productivity.java.syslog4j.server.impl.event.printstream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class FileSyslogServerEventHandler extends PrintStreamSyslogServerEventHandler {
	private static final long serialVersionUID = 979941585808969449L;

	protected static PrintStream createPrintStream(final String fileName, final boolean append) throws IOException {
		final File file = new File(fileName);
		final OutputStream os = new FileOutputStream(file,append);
		final PrintStream printStream = new PrintStream(os);
		return printStream;
	}
	
	public FileSyslogServerEventHandler(final String fileName) throws IOException {
		super(createPrintStream(fileName,true));		
	}
	
	public FileSyslogServerEventHandler(final String fileName, final boolean append) throws IOException {
		super(createPrintStream(fileName,append));
	}
}
