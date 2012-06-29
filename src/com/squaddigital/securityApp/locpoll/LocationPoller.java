package com.squaddigital.securityApp.locpoll;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * BroadcastReceiver to be launched by AlarmManager. Simply
 * passes the work over to LocationPollerService, who arranges
 * to make sure the WakeLock stuff is done properly.
 */
public class LocationPoller extends BroadcastReceiver {
	
	/**
	 * @deprecated
	 */
	public static final String EXTRA_ERROR = LocationPollerResult.ERROR_KEY;
	
	/**
	 * @deprecated
	 */
	public static final String EXTRA_INTENT = LocationPollerParameter.INTENT_TO_BROADCAST_ON_COMPLETION_KEY;
	
	/**
	 * @deprecated
	 */
	public static final String EXTRA_PROVIDER = LocationPollerParameter.PROVIDER_KEY;
	
	/**
	 * @deprecated
	 */
	public static final String EXTRA_LOCATION = LocationPollerResult.LOCATION_KEY;

	/**
	 * @deprecated
	 */
	public static final String EXTRA_LASTKNOWN = LocationPollerResult.LASTKNOWN_LOCATION_KEY;

	/**
		* Standard entry point for a BroadcastReceiver. Delegates
		* the event to LocationPollerService for processing.
    */
	@Override
	public void onReceive(Context context, Intent intent) {
		LocationPollerService.requestLocation(context, intent);
	}
	
}
