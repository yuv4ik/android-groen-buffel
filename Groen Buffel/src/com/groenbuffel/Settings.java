package com.groenbuffel;

public class Settings {

	public static final String UTF8 = "UTF-8";
	public static final int SERVER_PORT = 3339;
	public static final int CLIENT_PORT = 4003;
	public static final int ADB_PORT = 5555;
	public static final String CONNECT_COMMAND = "adb connect %1$s:" + ADB_PORT;
	public static final String DISCONNECT_COMMAND = "adb disconnect";
	public static final String CONNECT = "connect";
	public static final String DISCONNECT = "disconnect";
	
}
