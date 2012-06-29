package com.squadigital.securityApp.activities;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squaddigital.securityApp.R;
import com.squaddigital.securityApp.locpoll.LocationPoller;
import com.squaddigital.securityApp.locpoll.LocationPollerParameter;
import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;
import com.squaddigital.securityApp.services.TrackMeService;
import com.squaddigital.securityApp.util.SecurityAppUtil;

public class Home extends BaseSecurityApp implements OnClickListener, android.content.DialogInterface.OnClickListener {

	private static final int PERIOD = 1000 * 60 * 1;
	private static final String TAG = "Home";

	public final static int SLEEP_TIME = 1000;

	private LocationManager locationManager;
	private LocationListener listenerCoarse;
	private LocationListener listenerFine;
	private Location currentLocation;

	private Button btnTrackMe;
	private Button btnRaiseAlarm;

	private ImageView imageViewLogout;
	private LinearLayout linearLayoutViewProfile;
	private LinearLayout linearLayoutSettings;

	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	private boolean isTrackMe;
	private boolean startTrackMe;

	private String longitude;
	private String latitude;
	private String userId;
	private String status;
	private String message;

	private AlarmManager alarmManager;
	private PendingIntent pendingIntent;

	GpsThread gpsThread;

	ProgressDialog progressDialog;
	GpsLocationListener locationListener;
	LocationManager lm;

	AgencyList agencyList;

	public void stopAlarms() {
		alarmManager.cancel(pendingIntent);
		Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
	}

	public void startRepeating() {
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Intent i = new Intent(this, LocationPoller.class);

		Bundle bundle = new Bundle();
		LocationPollerParameter parameter = new LocationPollerParameter(bundle);
		parameter.setIntentToBroadcastOnCompletion(new Intent(this, LocationReceiver.class));
		parameter.setProviders(new String[] { LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER });
		parameter.setTimeout(60000);
		i.putExtras(bundle);
		pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pendingIntent);

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		SharedPreferences sharedPreferencesUserId = PreferenceManager.getDefaultSharedPreferences(this);
		userId = sharedPreferencesUserId.getString("USERID", "null");

		SharedPreferences sharedPreferencesTrackMeStatus = PreferenceManager.getDefaultSharedPreferences(this);
		isTrackMe = sharedPreferencesTrackMeStatus.getBoolean("trackMe", false);

		btnRaiseAlarm = (Button) findViewById(R.id.buttonraisealarm);
		btnTrackMe = (Button) findViewById(R.id.buttontrackme);

		linearLayoutViewProfile = (LinearLayout) findViewById(R.id.linearviewprofile);
		linearLayoutSettings = (LinearLayout) findViewById(R.id.linearLayoutsettings);

		imageViewLogout = (ImageView) findViewById(R.id.imageViewLogout);

		linearLayoutSettings.setOnClickListener(this);
		linearLayoutViewProfile.setOnClickListener(this);

		btnRaiseAlarm.setOnClickListener(this);
		btnTrackMe.setOnClickListener(this);

		imageViewLogout.setOnClickListener(this);
		locationListener = new GpsLocationListener();


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menuhome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.account:
			startActivity(new Intent(this, Help.class));
			break;
		case R.id.profile:
			startActivity(new Intent(this, ManageProfile.class));
			break;
		case R.id.help:
			startHelp();
			break;
		case R.id.logout:
			showMessage();
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageViewLogout:
			showMessage();
			break;

		case R.id.linearviewprofile:

			startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
			break;

		case R.id.linearLayoutsettings:
			startActivity(new Intent(getApplicationContext(), Settings.class));
			break;

		case R.id.buttonraisealarm:
			initLocationlistener();
			try {
				gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			} catch (Exception ex) {
				Log.d(TAG, ex.toString());
			}
			try {
				network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			} catch (Exception ex) {
				Log.d(TAG, ex.toString());
			}
			if (!gps_enabled && !network_enabled) {
		 		AlertDialog.Builder builder = new Builder(this);
				builder.setTitle("Attention!");
				builder.setMessage("Sorry, location is not determined. Please enable location providers");
				builder.setPositiveButton("OK", this);
				builder.setNeutralButton("Cancel", this);
				builder.create().show();
			} else {
				btnRaiseAlarm.setBackgroundDrawable(getResources().getDrawable(R.drawable.raise_alarm_active));
				 registerLocationListeners();
//				new GpsTimeout().execute();

			}
			break;

		case R.id.buttontrackme:
			if (isTrackMe) {
				initLocationlistener();
				try {
					gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
				} catch (Exception ex) {
				}
				try {
					network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				} catch (Exception ex) {
				}
				if (!gps_enabled && !network_enabled) {
					AlertDialog.Builder builder = new Builder(this);
					builder.setTitle("Attention!");
					builder.setMessage("Sorry, location is not determined. Please enable location providers");
					builder.setPositiveButton("OK", this);
					builder.setNeutralButton("Cancel", this);
					builder.create().show();
				} else {
					if (!startTrackMe) {
						startRepeating();
						startTrackMe = true;
					} else if (startTrackMe) {
						startTrackMe = false;
						stopAlarms();
						if (isTrackMeServiceRunning()) {
							stopService(new Intent(getApplication(), TrackMeService.class));
						}

					}

				}

			} else {
				Toast.makeText(this, "You must enable track me", Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}
	}

	private void connectWebservice() {
		SecurityAppUtil.fetchXml(Global.url + "clients/raiseAlarm/" + userId+ "/" + latitude + "/" + longitude);
		agencyList = MyXMLHandler.agencyList;
		String[] statusList;
		String[] messageList;
		if (agencyList != null) {
			statusList = new String[agencyList.getStatus().size()];
			statusList = agencyList.getStatus().toArray(statusList);
			messageList = new String[agencyList.getMessage().size()];
			messageList = agencyList.getMessage().toArray(messageList);
			for (int i = 0; i < statusList.length; i++) {
				status = statusList[i];
				message = messageList[i];
			}
			if (status.equals("true")) {
				showMessage(message);
			} else if (status.equals("false")) {
				showMessageError(message);
			}
		} else {
			showMessageError("Please try again when you regain connectivity"); 

		}

	}

	private void showMessageError(String msg) {
		new AlertDialog.Builder(this)
				.setMessage(msg)
				.setPositiveButton("OK",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((AlertDialog) dialog).getButton(which)
										.setVisibility(View.GONE);
								btnRaiseAlarm.setBackgroundDrawable(getResources().getDrawable(R.drawable.raise_alarm));
							}
						}).show();
	}

	private void showMessage(String msg) {
		new AlertDialog.Builder(this)
				.setMessage(msg)
				.setPositiveButton("OK",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((AlertDialog) dialog).getButton(which)
										.setVisibility(View.GONE);
								btnRaiseAlarm.setBackgroundDrawable(getResources().getDrawable(R.drawable.raise_alarm));
							}
						}).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "Onpause being called");

	}

	@Override
	protected void onRestart() {
		super.onRestart();

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "On resuming...");

	}

	private void registerLocationListeners() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Initialize criteria for location providers
		Log.d("GPS", "init");
		Criteria fine = new Criteria();
		fine.setAccuracy(Criteria.ACCURACY_FINE);
		Criteria coarse = new Criteria();
		coarse.setAccuracy(Criteria.ACCURACY_COARSE);
		// Get at least something from the device,
		// could be very inaccurate though
		currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(fine, true));
		if (listenerFine == null || listenerCoarse == null) {
			createLocationListeners();
			Log.d("Home", "creating locationlistener");
		}

		// Will keep updating about every 500 ms until
		// accuracy is about 1000 meters to get quick fix.
		locationManager.requestLocationUpdates(locationManager.getBestProvider(coarse, true), 500, 1000, listenerCoarse);
		// Will keep updating about every 500 ms until
		// accuracy is about 50 meters to get accurate fix.
		locationManager.requestLocationUpdates(locationManager.getBestProvider(fine, true), 500, 500,listenerFine);
	}

	private void createLocationListeners() {
		listenerCoarse = new LocationListener() {
			public void onStatusChanged(String provider, int status,Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
				Log.d("GPS", "Provider Enabled");
			}

			public void onProviderDisabled(String provider) {
				Log.d("GPS", "Provider Disabled");
			}

			public void onLocationChanged(Location location) {
				currentLocation = location;
				if (location.getAccuracy() > 1000 && location.hasAccuracy())
					locationManager.removeUpdates(this);
				latitude = String.valueOf(location.getLatitude());
				longitude = String.valueOf(location.getLongitude());
				if(latitude != null && longitude != null)
				connectWebservice();

			}
		};

		listenerFine = new LocationListener() {
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}

			public void onLocationChanged(Location location) {
				currentLocation = location;
				if (location.getAccuracy() > 1000 && location.hasAccuracy())
					locationManager.removeUpdates(this);
			}
		};
	}

	private boolean isTrackMeServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.squaddigital.securityApp.services.TrackMeService".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private class GpsTimeout extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(Home.this);

		protected void onPreExecute() {
			Dialog.setMessage("Acquiring Gps...");
			Dialog.show();

		}

		protected Void doInBackground(String... arg) {
			boolean running = true;
			int counter = 0;
			while (running){
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				 registerLocationListeners();
				 if (latitude != null) {
					 counter = 10;
					}
				 if(counter == 10){
					 running = false;
					 counter ++;
				 }

			}
			
			return null;

		}

		protected void onPostExecute(Void unused) {
			if (Dialog.isShowing()) {
				Dialog.dismiss();
			}
			if (message != null)
				showMessage(message);
			btnRaiseAlarm.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.raise_alarm));
		}

	}
	
	public void initLocationlistener(){
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
	}

}