package com.squadigital.securityApp.activities;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GpsThread extends Thread{
	
	public final static int STATE_DONE = 0;
	public final static int STATE_RUNNING = 1;
	public final static int TIMEOUT = 10000;
	
	public static Location locat;


	public final static int SLEEP_TIME = 1000;
	
	String latitude ;
	String longitude ;

	int mState;
	int totalTime;
	int sleepTime;
	Handler mHandler;
	Location location;
	LocationManager locManager;
	GpsLocationListener locListener;
	boolean reachMaxTime = false;

	public GpsThread(Handler h, LocationManager lm, GpsLocationListener ll) {
		mHandler = h;
		locManager = lm;
		locListener = ll;
	}
	
	public GpsThread(Location loc,LocationManager lm, GpsLocationListener ll){
		location = loc;
		locManager = lm;
		locListener = ll;
		loc = locListener.getLocation();
		if (loc != null) {
			latitude = String.valueOf(loc.getLatitude());
			longitude = String.valueOf(loc.getLongitude());
		}
		locManager.removeUpdates(locListener);
	}

	public void run() {

		mState = STATE_RUNNING;
		totalTime = 0;
		Location loc;

		while (mState == STATE_RUNNING) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				Log.e("ERROR", "Thread Interrupted");
			}
			loc = locListener.getLocation();
			if (loc != null) {
				latitude = String.valueOf(loc.getLatitude());
				longitude = String.valueOf(loc.getLongitude());
			}

			if (mHandler != null) {
				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putInt("elapsedTime", totalTime);
				b.putBoolean("acquired", loc != null);
				b.putString("latitude", latitude);
				b.putString("longitude", longitude);
				msg.setData(b);

				if (!reachMaxTime) {
					mHandler.sendMessage(msg);
				}
			}
			reachMaxTime = totalTime >= TIMEOUT;

			totalTime += SLEEP_TIME;

		}
		locManager.removeUpdates(locListener);
	}

	public void setState(int state) {
		mState = state;
	}
	
	
}

