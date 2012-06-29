package com.squadigital.securityApp.activities;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GpsLocationListener implements LocationListener{

	Location loc = null;
	@Override
	public void onLocationChanged(Location arg0) {
		loc = arg0;
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}
	
	public Location getLocation(){
		return loc;
	}

}
