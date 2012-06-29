package com.squadigital.securityApp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.squaddigital.securityApp.locpoll.LocationPollerResult;
import com.squaddigital.securityApp.services.TrackMeService;

public class LocationReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle b = intent.getExtras();
		LocationPollerResult locationResult = new LocationPollerResult(b);

		Location loc = locationResult.getLocation();
		String latitude = null;
		String longitude = null;
		String message = null;

		if (loc == null) {
			loc = locationResult.getLastKnownLocation();
			if (loc == null) {
				message = locationResult.getError();
			} else {
				message = loc.toString();
			}
		} else {
			latitude = String.valueOf(loc.getLatitude());
			longitude = String.valueOf(loc.getLongitude());
		}

		Intent i = new Intent(context, TrackMeService.class);
		i.putExtra("lat", latitude);
		i.putExtra("long", longitude);
		context.startService(i);
	}
}