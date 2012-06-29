package com.squaddigital.securityApp.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnection {
	static NetworkInfo wifiInformation, mobileInformation;
	static ConnectivityManager connection_manager;
	private static boolean isNetworkAvailable = false;
	
	public static boolean isNetworkAvailable(final Context cxt){
		connection_manager =(ConnectivityManager)cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiInformation = connection_manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		mobileInformation = connection_manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		 if(wifiInformation.isConnected() || mobileInformation.isConnected()){ 
			 isNetworkAvailable = true;
	     }else {
	    	 isNetworkAvailable = false;
	     }
		   return  isNetworkAvailable;
	 }
}
