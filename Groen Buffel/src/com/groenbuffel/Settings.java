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
