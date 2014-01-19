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

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;


public class TransportUtils {

	public static String readPackageContent(DatagramPacket packet) {
		int realSize = packet.getLength();
		byte[] realPacket = new byte[realSize];
		System.arraycopy(packet.getData(), 0, realPacket, 0, realSize);
		String content = null;
		try {
			content = new String(realPacket, Settings.UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return content;
	}
	
}
