package com.squaddigital.securityApp.application;

import android.app.Application;

public class SecurityAppApplication extends Application {

	public String[] emergencyContactList;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public String[] removeEmergencyContact() {

		return emergencyContactList;
	}

	public String[] addEmergencyContact() {

		return emergencyContactList;
	}
	
	public String[] editEmergencyContact(){
			
			return emergencyContactList;
		}

}
