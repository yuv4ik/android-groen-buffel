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
