package com.groenbuffel.server.shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Command {
	
	private String cmd;
	private int exitCode = -1;
	private StringBuilder log;
	
	public Command(String cmd) {
		log = new StringBuilder();
		this.cmd = cmd;
	}

	public int getExitCode() {
		return exitCode;
	}

	public StringBuilder getLog() {
		return log;
	}
	
	public void execute() throws Exception {
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		BufferedReader rd = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = null;
		
		while((line = rd.readLine()) != null)
			log.append(line);
		
		exitCode = pr.exitValue();
	}

}
