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
