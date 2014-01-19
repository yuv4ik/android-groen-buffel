package com.groenbuffel;

public enum AdbStatus {
	
	CONNECTED("connected"),
	ALREADY_CONNECTED("already connected"),
	UNABLE_TO_CONNECT("unable to connect"),
	ERROR("adb is not recognized as an internal or external command");
	
	private String msg;
	
	AdbStatus(String msg) {
		this.msg = msg;
	}
	
	public String getMessage() {
		return this.msg;
	}
	
}
