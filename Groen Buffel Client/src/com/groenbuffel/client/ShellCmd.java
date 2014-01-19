package com.groenbuffel.client;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

/**
 * Execute shell cmd using RootTools and wait till cmd returns.
 * @author yuv4ik
 *
 */
public class ShellCmd extends CommandCapture {

	public ShellCmd(int id, String[] command) {
		super(id, command);
	}
	
	public synchronized boolean execute() {
		try {
			RootTools.getShell(true).add(this);
			while (!isFinished())
				wait();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
