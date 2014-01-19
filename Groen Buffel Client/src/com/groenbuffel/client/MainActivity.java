package com.groenbuffel.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.groenbuffel.AdbStatus;
import com.groenbuffel.Settings;
import com.groenbuffel.TransportUtils;
import com.stericson.RootTools.RootTools;

public class MainActivity extends Activity implements OnClickListener {
	
	private TextView tvTimeLeft;
	private ImageView ivConnect;
	private ImageView ivDisconnect;
	
	private GBTimer gbTimer;
	
	private static final int SOCKET_TIMEOUT = 1000 * 30 * 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (RootTools.isRootAvailable())
		    showToastMsg(getString(R.string.root_is_availble));
		else {
			showToastMsg(getString(R.string.root_is_unavailble));
			finish();
		}
		
		tvTimeLeft = (TextView) findViewById(R.id.tvTimeLeft);
		
		ivConnect = (ImageView) findViewById(R.id.btnConnect);
		ivConnect.setOnClickListener(this);
		
		ivDisconnect = (ImageView) findViewById(R.id.btnDisconnect);
		ivDisconnect.setOnClickListener(this);
		
		gbTimer = new GBTimer(SOCKET_TIMEOUT, 1000);
	}
	
	private void connect() {
		new AsyncTask<Void, Void, AdbStatus>() {

			@Override
			protected void onPreExecute() {
				ivConnect.setImageResource(R.drawable.connection_in_progress);
				ivConnect.setEnabled(false);
				ivDisconnect.setEnabled(false);
			}

			@Override
			protected AdbStatus doInBackground(Void... params) {
				
				// Enable ADB
				ShellCmd enableAdbTcp = null;
				enableAdbTcp = new ShellCmd(0, new String[] { 
					"setprop service.adb.tcp.port " + Settings.ADB_PORT,
					"stop adbd",
					"start adbd"
					});
				
				if(!enableAdbTcp.execute()) { /*STOP*/ } 
				
				// Send a connection request & Get a Response
				DatagramSocket socket = null;
				
				try {
					
					socket = new DatagramSocket(Settings.CLIENT_PORT);
					socket.setSoTimeout(SOCKET_TIMEOUT);
					byte[] dataToSend = Settings.CONNECT.getBytes(Settings.UTF8);
					DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length);
					packetToSend.setAddress(InetAddress.getByName("255.255.255.255"));
					packetToSend.setPort(Settings.SERVER_PORT);
					socket.send(packetToSend);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							gbTimer.start();
						}
						
					});
					
					while(true) {
						byte[] dataToReceive = new byte[100];
						DatagramPacket packetToReceive = new DatagramPacket(dataToReceive, dataToReceive.length);
						socket.receive(packetToReceive);
						AdbStatus status = AdbStatus.valueOf(new String(TransportUtils.readPackageContent(packetToReceive)));
						return status;
					}
					
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showToastMsg(getString(R.string.exception) + e);
						}
					});
					
				} finally {
					socket.disconnect();
					socket.close();
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(AdbStatus result) {
				
				if(result != null) {
					
					switch (result) {
						case ALREADY_CONNECTED:
						case CONNECTED:
							ivConnect.setImageResource(R.drawable.connection_succeed);
							break;
						case UNABLE_TO_CONNECT:
						case ERROR:
							ivConnect.setImageResource(R.drawable.connection_failed);
							break;
					}

				} else {
					ivConnect.setImageResource(R.drawable.connection_failed);
					showToastMsg(getString(R.string.cant_find_server));
				}
				
				ivConnect.setEnabled(true);
				gbTimer.cancel();
				tvTimeLeft.setText("");
				ivDisconnect.setEnabled(true);
			}

		
		}.execute();
	}
	
	private void disconnect() {
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				ivConnect.setEnabled(false);
				ivConnect.setImageResource(R.drawable.connect);
				ivDisconnect.setEnabled(false);
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				ShellCmd disableAdbTcp = null;
				disableAdbTcp = new ShellCmd(1, new String[] { 
						"setprop service.adb.tcp.port -1",
					    "stop adbd",
					    "start adbd"
					});
				
				if(!disableAdbTcp.execute()) { /*STOP*/ } 
				
				DatagramSocket socket = null;
				
				try {
					
					socket = new DatagramSocket(Settings.CLIENT_PORT);
					socket.setSoTimeout(SOCKET_TIMEOUT);
					byte[] dataToSend = Settings.DISCONNECT.getBytes(Settings.UTF8);
					DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length);
					packetToSend.setAddress(InetAddress.getByName("255.255.255.255"));
					packetToSend.setPort(Settings.SERVER_PORT);
					socket.send(packetToSend);
					
				} catch(final Exception ex) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showToastMsg(getString(R.string.exception) + ex);
						}
					});
					
				} finally {
					socket.disconnect();
					socket.close();
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				ivConnect.setEnabled(true);
				ivDisconnect.setEnabled(true);
				showToastMsg(getString(R.string.disconnect));
			}
			
		}.execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnConnect:
				connect();
				break;
			case R.id.btnDisconnect:
				disconnect();
				break;
		}
		
	}
	
	private void showToastMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	class GBTimer extends CountDownTimer {

		public GBTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			tvTimeLeft.setText("");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tvTimeLeft.setText(getString(R.string.time_left) + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
		}
		
	}

}
