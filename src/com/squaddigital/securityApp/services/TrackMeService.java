package com.squaddigital.securityApp.services;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.squaddigital.securityApp.net.AgencyList;
import com.squaddigital.securityApp.net.MyXMLHandler;
import com.squadigital.securityApp.activities.Global;

public class TrackMeService extends Service {
	
	private String userId;
	
	AgencyList agencyList;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences sharedPreferencesUserId = PreferenceManager
				.getDefaultSharedPreferences(this);
		userId = sharedPreferencesUserId.getString("USERID", "null");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		showMessage("Service stopped");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		String latitude = intent.getStringExtra("lat");
		String longitude = intent.getStringExtra("long");
		showMessage(latitude + " " + longitude);
		if (latitude != null && longitude != null) {
			connectWebservice(userId,latitude,longitude);
		}
		Log.i("trackmeservice", "service started");
	}

	private void fetchXml(String url) {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			URL sourceUrl = new URL(url);
			MyXMLHandler myXMLHandler = new MyXMLHandler();
			xr.setContentHandler(myXMLHandler);
			xr.parse(new InputSource(sourceUrl.openStream()));
		} catch (Exception e) {
			System.out.println("XML Parsing Exception = " + e);
		}
	}

	private void connectWebservice(String userid, String latitude,String longitude) {
		fetchXml(Global.url + "clients/raiseAlarm/" + userid + "/" + latitude+ "/" + longitude);
		agencyList = MyXMLHandler.agencyList;
		
		String[] statusList = new String[agencyList.getStatus().size()];
		statusList = agencyList.getStatus().toArray(statusList);
		String[]  messageList = new String[agencyList.getMessage().size()];
		messageList = agencyList.getMessage().toArray(messageList);
		String message = null;
		String status = null;
		for (int i = 0; i < statusList.length; i++) {
			status = statusList[i];
			message = messageList[i];
		}
		if (status.equals("true")) {
			showMessage(message);
		} else if (status.equals("false")) {
			showMessage(message);
		}
	}

	private void showMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

}
