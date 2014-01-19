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

package com.groenbuffel.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.groenbuffel.AdbStatus;
import com.groenbuffel.Settings;
import com.groenbuffel.TransportUtils;
import com.groenbuffel.server.shell.Command;

public class Server {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			// Construct a datagram socket and bind it to the specified port on the developer machine
			DatagramSocket socket = new DatagramSocket(Settings.SERVER_PORT);
			System.out.println("Groen Buffel server is UP...");
			
			while(true) {
				// Wait for a datagram packets from this socket
				byte[] dataToReceive = new byte[100];
				DatagramPacket packetToReceive = new DatagramPacket(dataToReceive, dataToReceive.length);
				socket.receive(packetToReceive);
				
				// Check if connect or disconnect
				String remoteCmd = TransportUtils.readPackageContent(packetToReceive);
				byte[] dataToSend = null;
				InetAddress clientIP = packetToReceive.getAddress();
				
				if(remoteCmd.equals(Settings.CONNECT)) {
					String result = connectToClient(clientIP).name();
					dataToSend = result.getBytes(Settings.UTF8);
				} else if(remoteCmd.equals(Settings.DISCONNECT)) {
					boolean isDisconnected = disconnectFromClient();
					if(isDisconnected)
						System.out.println("disconnected from " + clientIP.getHostName());
					dataToSend = "OK".getBytes(Settings.UTF8);
				}
				
				DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length);
				packetToSend.setAddress(clientIP);
				packetToSend.setPort(Settings.CLIENT_PORT);
				socket.send(packetToSend);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static AdbStatus connectToClient(InetAddress ip) {

		final String cmd = String.format(Settings.CONNECT_COMMAND, ip.getHostName());
		Command connectCmd = new Command(cmd);
		try {
			connectCmd.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String execResult = connectCmd.getLog().toString();
		
		if(execResult.toString().contains("already"))
			return AdbStatus.ALREADY_CONNECTED;
		else if(execResult.toString().contains("unable"))
			return AdbStatus.UNABLE_TO_CONNECT;
		else if(execResult.toString().contains("connected"))
			return AdbStatus.CONNECTED;
			
		return AdbStatus.ERROR;
	}
	
	private static boolean disconnectFromClient() {
		Command disconnectCmd = new Command(Settings.DISCONNECT_COMMAND);
		try {
			disconnectCmd.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disconnectCmd.getExitCode() == 0? true: false;
	}

}
