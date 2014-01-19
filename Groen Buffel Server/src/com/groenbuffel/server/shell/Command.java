/*
 * Copyright (C) 2014 yuv4ik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
